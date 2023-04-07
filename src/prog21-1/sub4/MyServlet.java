import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MyServlet extends HttpServlet {

	Map<String, List<Message>> queueMap = new ConcurrentHashMap<>();
	Map<String, CreateInput> infoMap = new ConcurrentHashMap<>();
	Map<String, List<Message>> dqMap = new ConcurrentHashMap<>();
	Map<String, List<Wait>> wMap = new ConcurrentHashMap<>();
	
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
		System.out.println(uri);
		
		Gson gson = new Gson();
		if (uri.startsWith("/CREATE")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			CreateInput input = gson.fromJson(body, CreateInput.class);
			
			if (queueMap.containsKey(queueName)) {
				res.getWriter().write(gson.toJson(new CommonOutput("Queue Exist")));
			} else {
				List<Message> queue = Collections.synchronizedList(new ArrayList<Message>());
				List<Message> dq = Collections.synchronizedList(new ArrayList<Message>());
				List<Wait> wq = Collections.synchronizedList(new ArrayList<Wait>());
				queueMap.put(queueName, queue);
				infoMap.put(queueName, input);
				dqMap.put(queueName, dq);
				wMap.put(queueName, wq);
				
				res.getWriter().write(gson.toJson(new CommonOutput("OK")));
			}
		} else if (uri.startsWith("/SEND")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			SendInput input = gson.fromJson(body, SendInput.class);
			String message = input.Message;
			List<Message> queue = queueMap.get(queueName);
			if (queue.size() == infoMap.get(queueName).QueueSize) {
				res.getWriter().write(gson.toJson(new CommonOutput("Queue Full")));
			} else {
				queue.add(new Message(message));
				res.getWriter().write(gson.toJson(new CommonOutput("OK")));
			}
		} else if (uri.startsWith("/RECEIVE")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			List<Message> queue = queueMap.get(queueName);
			
			System.out.println(queueName);
			System.out.println(queue);
			
			for (int i = 0; i < queue.size(); i++) {
				Message m = queue.get(i);
				if (m.status.equals(Status.free)) {
					sendMessage(m, res, queueName);
					return;
				}
			}
			System.out.println("Fail to send message");
			
			if (infoMap.get(queueName).WaitTime > 0) {
				long requestId = System.currentTimeMillis();
				List<Wait> wq = wMap.get(queueName);
				wq.add(new Wait(requestId));
				
				//TODO 정상 동작하지 않음
				System.out.println(">>>>>>>>");
				while (System.currentTimeMillis() <= requestId + infoMap.get(queueName).WaitTime * 1000) {
					System.out.println(requestId);
					System.out.println(queue);
					System.out.println(wq);
					System.out.println();
					
					Wait w = wq.get(0);
					if (w.time == requestId) {
						if (queue.size() > 0) {
							for (int i = 0; i < queue.size(); i++) {
								Message m = queue.get(i);
								if (m.status.equals(Status.free)) {
									System.out.println("SEND MESSAGE");
									sendMessage(m, res, queueName);
									wq.remove(0);
									return;
								}
							}
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}				
				
				System.out.println("No Message " + (System.currentTimeMillis() - requestId));
				res.getWriter().write(gson.toJson(new CommonOutput("No Message")));
				for (int i = 0; i < wq.size(); i++) {
					if (wq.get(i).time == requestId) {
						wq.remove(i);
						break;
					}
				}
			
				
				return;
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
					Message ms = queue.get(i);
					ms.failCount++;
					
					if (infoMap.get(queueName).MaxFailCount < ms.failCount) {
						dqMap.get(queueName).add(ms);
						queue.remove(i);
					} else {
						ms.status = Status.free;										
					}
					res.getWriter().write(gson.toJson(new CommonOutput("OK")));
					return;
				}
			}
		} else if (uri.startsWith("/DLQ")) {
			String[] tmp = uri.split("/");
			String queueName = tmp[2];
			if (dqMap.get(queueName).size() > 0) {
				Message m = dqMap.get(queueName).get(0);
				dqMap.get(queueName).remove(0);
				res.getWriter().write(gson.toJson(new ReceiveOutput(m.id, m.message)));
			} else {
				res.getWriter().write(gson.toJson(new CommonOutput("No Message")));
			}
		} 
	}

	public String getReqBody(HttpServletRequest req) throws IOException {
		return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	private void sendMessage(Message m, HttpServletResponse res, String queueName) {
		Gson gson = new Gson();
		List<Message> queue = queueMap.get(queueName);
		
		m.status = Status.send;
		try {
			res.getWriter().write(gson.toJson(new ReceiveOutput(m.id, m.message)));
			res.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String id = m.id;
		
		if (infoMap.get(queueName).ProcessTimeout > 0) {
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.schedule(() -> {
				for (int k = 0; k < queue.size(); k++) {
					Message ms = queue.get(k);
					if (ms.id.equals(id) && ms.status.equals(Status.send)) {
						ms.failCount++;
						
						if (infoMap.get(queueName).MaxFailCount < ms.failCount) {
						    System.out.println("Dead Letter Queue " + ms);
							dqMap.get(queueName).add(ms);
							queue.remove(k);
						} else {
						    System.out.println("Become Free " + ms);
							ms.status = Status.free;										
						}
						break;
					}
				}
			}, infoMap.get(queueName).ProcessTimeout, TimeUnit.SECONDS);						
		}
		return;
	}
}
enum Status {
	free,
	send
}
class Message {
	@Override
	public String toString() {
		return "Message [message=" + message + ", status=" + status + ", id=" + id + ", failCount=" + failCount + "]";
	}
	String message;
	Status status = Status.free;
	String id;
	int failCount = 0;
	public Message(String message) {
		super();
		this.message = message;
		this.id = System.currentTimeMillis() + "";
	}
}
class CreateInput {
	int QueueSize;
	int ProcessTimeout;
	int MaxFailCount;
	int WaitTime;
	@Override
	public String toString() {
		return "CreateInput [QueueSize=" + QueueSize + ", ProcessTimeout=" + ProcessTimeout + ", MaxFailCount="
				+ MaxFailCount + ", WaitTime=" + WaitTime + "]";
	}	
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
class Wait {
	long time;
	public Wait(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Wait [time=" + time + "]";
	}
}

// http://dveamer.github.io/backend/JavaConcurrentCollections.html
