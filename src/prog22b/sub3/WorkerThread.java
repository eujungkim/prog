import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

public class WorkerThread implements Runnable {

	ControllerInput input;
	int i;
	Worker worker;

	public WorkerThread(int i, ControllerInput input) {
		this.i = i;
		this.input = input;
		worker = new Worker(i);
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
				ContentResponse contentRes = httpClient.newRequest(input.inputQueueURIs.get(i)).method(HttpMethod.GET)
						.send();
				String jsonInput = contentRes.getContentAsString();
				Gson gson = new Gson();
				InputQueueData queueInput = gson.fromJson(jsonInput, InputQueueData.class);
				String result = worker.run(queueInput.timestamp, queueInput.value);				
				if (result != null) {
					httpClient.newRequest(input.outputQueueURI).method(HttpMethod.POST)
							.content(new StringContentProvider("{\"result\":\"" + result + "\"}")).send();
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
		return "InputQueueData [timestamp=" + timestamp + ", value=" + value + "]";
	}	
}
