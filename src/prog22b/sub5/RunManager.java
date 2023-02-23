import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

class ControllerInput {
	public int processCount;
	public int threadCount;
	public int outputQueueBatchSize;	
	public int inputQueueCount;
	public List<String> inputQueueURIs = new ArrayList<>();
	public String outputQueueURI;
}

public class RunManager {

	public static void main(String[] args) throws Exception {
		new File("backup").deleteOnExit();
		new File("backup").mkdir();
		
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/queueInfo").method(HttpMethod.GET)
				.send();
		String jsonInput = contentRes.getContentAsString();

		Gson gson = new Gson();
		ControllerInput input = gson.fromJson(jsonInput, ControllerInput.class);
		
		int processNo = input.processCount;
		int threadNo = input.threadCount;
		int queueNo = input.inputQueueCount;

		Map<Integer, List<String>> map = new HashMap<>();
		for (int i = 0; i < processNo; i++) {
			map.put(i, new ArrayList<String>());
		}
		
		int count = 0;
		while (count < queueNo) {
			for (int j = 0; j < processNo; j++) {
				if (count == queueNo) {
					break;
				}
				for (int k = 0; k < threadNo; k++) {
					if (count == queueNo) {
						break;
					}
					
					// threadNo # workerNo # inputURI 
					String value = k + "#" + count + "#" + input.inputQueueURIs.get(count);
					map.get(j).add(value);
					count++;
				}
			}
		}
		
		for (int i : map.keySet()) {
			List<String> ins = new ArrayList<>();
			ins.add(i + ""); // processNo
			ins.add(input.outputQueueBatchSize + "");
			ins.add(input.outputQueueURI);
			ins.addAll(map.get(i));
			System.out.println(ins);
			
	        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	        executorService.scheduleWithFixedDelay(() -> {
	        	try {
					JavaProcess.exec(MyProcess.class, ins);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }, 0, 10, TimeUnit.MILLISECONDS);
		}
	}

}
