import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

class ProcessResult {
	public List<String> resultList;
	long updateTime;
}
public class MyProcess {
	public static void main(String[] args) throws Exception {
		// processNo, batchSize, output, intputlist 
		List<String> resultList = Collections.synchronizedList(new ArrayList<>());
		ProcessResult processResult = new ProcessResult();
		processResult.resultList = resultList;
		processResult.updateTime = -1;
		
		int outputQueueBatchSize = Integer.parseInt(args[1]);
		String outputQueueURI = args[2];
		
		Map<Integer, WorkerThread> servers = new HashMap<>();
		for (int i = 3; i< args.length; i++) {
			String value = args[i];
			String[] tmp = value.split("#"); // threadNo # workerNo # inputURI
			int threadNo = Integer.parseInt(tmp[0]);
			int workerNo = Integer.parseInt(tmp[1]);
			String inputURI = tmp[2];

			if (!servers.containsKey(threadNo)) {
				servers.put(threadNo, new WorkerThread(threadNo, outputQueueBatchSize, outputQueueURI, resultList, processResult));
			}
			servers.get(threadNo).workerMap.put(workerNo, new Worker(workerNo));
			servers.get(threadNo).inputQueueURIMap.put(workerNo, inputURI);				
		}
		
		for (int threadNo : servers.keySet()) {
			Thread thread = new Thread(servers.get(threadNo));
			thread.start();
		}
		
		
		Thread timeChecker = new Thread(new Runnable() {
			@Override
			public void run() {
				Gson gson = new Gson();
				HttpClient httpClient = new HttpClient();
				try {
					httpClient.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				while (true) {
					try {
						Thread.sleep(100);

						if (resultList.size() > 0 && System.currentTimeMillis() - processResult.updateTime >= 2000) {
							String content = gson.toJson(new OutputQueueData(resultList));
							
							httpClient.newRequest(outputQueueURI).method(HttpMethod.POST)
							.content(new StringContentProvider(content)).send();
							resultList.clear();
							processResult.updateTime = -1;
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});
		timeChecker.start();
		/*
		FileWriter writer = new FileWriter("c:/test/" + args[0]  + ".txt");
		PrintWriter pw = new PrintWriter(writer);
		for (String line : args) {
			pw.println(line);
		}
		pw.close();
		writer.close();
		
		
		int batchSize = Integer.parseInt(args[1]);
		
		Map<String, Worker> map = new HashMap<>();
		*/
		// java com.lgcns.test.MyProcess
		
		//File file = new File("c:/test/" + System.currentTimeMillis());
		//file.mkdir();
	}
}
