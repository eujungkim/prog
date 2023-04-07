import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MyServlet extends HttpServlet {

	Map<String, List<Message>> queueMap = new ConcurrentHashMap<>();
	Map<String, Integer> infoMap = new ConcurrentHashMap<>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doSomething(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doSomething(req, res);
	}
	
	protected void doSomething(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String body = getReqBody(req);
		
		Gson gson = new Gson();
		if (uri.startsWith("/CREATE")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			CreateInput input = gson.fromJson(body, CreateInput.class);
			int size = input.QueueSize;
			
			if (queueMap.containsKey(queueName)) {
				res.getWriter().write(gson.toJson(new CommonOutput("Queue Exist")));
			} else {
				List<Message> queue = Collections.synchronizedList(new ArrayList<Message>());
				
				queueMap.put(queueName, queue);
				infoMap.put(queueName, size);
				res.getWriter().write(gson.toJson(new CommonOutput("OK")));
			}
		} else if (uri.startsWith("/SEND")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			SendInput input = gson.fromJson(body, SendInput.class);
			String message = input.Message;
			List<Message> queue = queueMap.get(queueName);
			if (queue.size() == infoMap.get(queueName)) {
				res.getWriter().write(gson.toJson(new CommonOutput("Queue Full")));
			} else {
				queue.add(new Message(message));
				res.getWriter().write(gson.toJson(new CommonOutput("OK")));
			}
		} else if (uri.startsWith("/RECEIVE")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			List<Message> queue = queueMap.get(queueName);
			for (int i = 0; i < queue.size(); i++) {
				Message m = queue.get(i);
				if (m.status.equals(Status.free)) {
					m.status = Status.send;					
					res.getWriter().write(gson.toJson(new ReceiveOutput(m.id, m.message)));
					return;
				}
			}
			res.getWriter().write(gson.toJson(new CommonOutput("No Message")));
		} else if (uri.startsWith("/ACK")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			String id = tmp[3];
			List<Message> queue = queueMap.get(queueName);
			for (int i = 0; i < queue.size(); i++) {
				if (queue.get(i).id.equals(id)) {
					queue.remove(i);
					res.getWriter().write(gson.toJson(new CommonOutput("OK")));
					return;
				}
			}
		} else if (uri.startsWith("/FAIL")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			String id = tmp[3];
			List<Message> queue = queueMap.get(queueName);
			for (int i = 0; i < queue.size(); i++) {
				if (queue.get(i).id.equals(id)) {
					queue.get(i).status = Status.free;
					res.getWriter().write(gson.toJson(new CommonOutput("OK")));
					return;
				}
			}
		}
	}

	public String getReqBody(HttpServletRequest req) throws IOException {
		return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}
}
enum Status {
	free,
	send
}
class Message {
	String message;
	Status status = Status.free;
	String id;
	public Message(String message) {
		super();
		this.message = message;
		this.id = System.currentTimeMillis() + "";
	}
}
class CreateInput {
	int QueueSize;
}
class SendInput {
	String Message;
}
class ReceiveOutput {
	String Result = "Ok";
	String MessageID;
	String Message;
	public ReceiveOutput(String messageID, String message) {
		MessageID = messageID;
		Message = message;
	}
}
class CommonOutput {
	String Result;

	public CommonOutput(String result) {
		Result = result;
	}
}
