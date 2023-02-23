import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

public class WorkerThread implements Runnable {

	int i;
	int outputQueueBatchSize;
	//Worker worker;
	//String inputQueueURI;
	String outputQueueURI;
	List<String> resultList;
	Map<Integer, Worker> workerMap = new HashMap<>();
	Map<Integer, String> inputQueueURIMap = new HashMap<>();
	ProcessResult processResult;

	public WorkerThread(int i, int outputQueueBatchSize, String outputQueueURI, List<String> resultList, ProcessResult processResult) {
		this.i = i;
		this.outputQueueBatchSize = outputQueueBatchSize;
		this.outputQueueURI = outputQueueURI;
		this.resultList = resultList;
		this.processResult = processResult;
	}
	
	public void addWorker(int i, String inputQueueURI) {
		workerMap.put(i, new Worker(i));
		inputQueueURIMap.put(i, inputQueueURI);
	}

	@Override
	public void run() {
		HttpClient httpClient = new HttpClient();
		try {
			httpClient.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}
		while (true)
			try {
				for (int i : workerMap.keySet()) {
					String inputQueueURI = inputQueueURIMap.get(i);
					Worker worker = workerMap.get(i);
					
					ContentResponse contentRes = httpClient.newRequest(inputQueueURI).method(HttpMethod.GET)
							.send();
					String jsonInput = contentRes.getContentAsString();
					Gson gson = new Gson();
					InputQueueData queueInput = gson.fromJson(jsonInput, InputQueueData.class);
					String result = worker.run(queueInput.timestamp, queueInput.value);				
					if (result != null) {
						resultList.add(result);
						if (processResult.updateTime == -1) {
							processResult.updateTime = System.currentTimeMillis();
						}
						
						if (resultList.size() >= outputQueueBatchSize) {
							String content = gson.toJson(new OutputQueueData(resultList));
							
							httpClient.newRequest(outputQueueURI).method(HttpMethod.POST)
							.content(new StringContentProvider(content)).send();
							resultList.clear();
							processResult.updateTime = -1;
						}
					}
				}
			} catch (Exception e) {
				//
			}
	}
}

class InputQueueData {
	public int timestamp;
	public String value;
	@Override
	public String toString() {
		return "QueueInput [timestamp=" + timestamp + ", value=" + value + "]";
	}	
}

class OutputQueueData {

	List<String> result;

	public OutputQueueData(List<String> result) {
		super();
		this.result = result;
	}
}
