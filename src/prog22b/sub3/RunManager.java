import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

class ControllerInput {
	public int inputQueueCount;
	public List<String> inputQueueURIs = new ArrayList<>();
	public String outputQueueURI;
	@Override
	public String toString() {
		return "Input [inputQueueCount=" + inputQueueCount + ", inputQueueURIs=" + inputQueueURIs + ", outputQueueURI="
				+ outputQueueURI + "]";
	}
}

public class RunManager {

	public static void main(String[] args) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/queueInfo").method(HttpMethod.GET)
				.send();
		String jsonInput = contentRes.getContentAsString();

		Gson gson = new Gson();
		ControllerInput input = gson.fromJson(jsonInput, ControllerInput.class);
		for (int i = 0; i < input.inputQueueCount; i++) {
			WorkerThread server = new WorkerThread(i, input);
			Thread thread = new Thread(server);
			thread.start();
		}

	}
}

