import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

class ProcessResult {
	public List<String> resultList;
	long updateTime;
}

class BackupData implements Serializable  {
	private static final long serialVersionUID = 1L;
	Map<String, List<String>> storeMap = new HashMap<>();
	int processNo;
	
	public BackupData(int processNo) {
		this.processNo = processNo;
	}
}

public class MyProcess {
	public static void main(String[] args) throws Exception {
		// processNo, batchSize, output, intputlist 
		List<String> resultList = Collections.synchronizedList(new ArrayList<>());
		ProcessResult processResult = new ProcessResult();
		processResult.resultList = resultList;
		processResult.updateTime = -1;
		
		int processNo = Integer.parseInt(args[0]);
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
		
		// restore backup data
		if (new File("backup/process" + processNo + ".dat").exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("backup/process" + processNo + ".dat"))) {
				BackupData restoredBackupData = (BackupData) in.readObject();				
				for (String key : restoredBackupData.storeMap.keySet()) {
					String[] tmp = key.split(":");
					int tn = Integer.parseInt(tmp[0]);
					int wn = Integer.parseInt(tmp[1]);
					servers.get(tn).workerMap.put(wn, new Worker(wn, restoredBackupData.storeMap.get(key)));
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
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
		
		// backup store
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
        	System.out.println(new Date() + " BACKUP " + processNo);
        	BackupData backupData = new BackupData(processNo);
        	
        	for (int threadNo : servers.keySet()) {
        		Map<Integer, Worker> workerMap = servers.get(threadNo).workerMap;
        		for (int workerNo : workerMap.keySet()) {
        			backupData.storeMap.put(threadNo + ":" + workerNo, workerMap.get(workerNo).getStore());
        		}
        	}
        	
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backup/process" + processNo + ".dat"))) {
                out.writeObject(backupData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);

	}
}
