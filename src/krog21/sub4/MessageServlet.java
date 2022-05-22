package com.lgcns.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MessageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String EXIST = "{\"Result\":\"Queue Exist\"}";
	private static String FULL = "{\"Result\":\"Queue Full\"}";
	private static String NO_MESSAGE = "{\"Result\":\"No Message\"}";
	private static String OK = "{\"Result\":\"Ok\"}";
	private static String MESSAGE = "{\"Result\":\"Ok\",\"MessageId\":\"%s\",\"Message\":\"%s\"}";
	
	private MessageService service = new MessageService();

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long start = System.currentTimeMillis();
		String[] pathParams = req.getRequestURI().substring(1).split("/");
		System.out.println(System.currentTimeMillis() +  req.getRequestURI());
		if ("RECEIVE".equals(pathParams[0])) {
			System.out.println(service.queueMap);
			String name = pathParams[1];
			Message m = service.receive(name, start);
			if (m != null) {
				String result = String.format(MESSAGE, m.getId(), m.getMessage());
				res.getWriter().write(result);
			} else {
				if (MessageService.infoMap.get(name).getWaitTime() > 0) {
					String waitId = UUID.randomUUID().toString();
					MessageService.waitMap.get(name).add(new WaitMessage(waitId, start));
					
					while (System.currentTimeMillis() - start < MessageService.infoMap.get(name).getWaitTime() * 1000) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						Iterator<WaitMessage> iter = MessageService.waitMap.get(name).iterator();
						while (iter.hasNext()) {
							WaitMessage ws = iter.next();
							if (ws.getId().equals(waitId) && ws.getMessage() != null) {
								System.out.println(System.currentTimeMillis() + "RECEIVE ASYNC>>" + ws.getMessage());
								iter.remove();
								m = ws.getMessage();
								String result = String.format(MESSAGE, m.getId(), m.getMessage());
								res.getWriter().write(result);
								res.setStatus(200);
								return;
							}
						}
					}
					res.getWriter().write(NO_MESSAGE);
					res.setStatus(200);
					return;
				}
				res.getWriter().write(NO_MESSAGE);
			}
		} else if ("DLQ".equals(pathParams[0])) {
			String name = pathParams[1];
			Message m = service.dlq(name);
			if (m != null) {
				String result = String.format(MESSAGE, m.getId(), m.getMessage());
				res.getWriter().write(result);
			} else {
				res.getWriter().write(NO_MESSAGE);
			}
		}
		res.setStatus(200);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String[] pathParams = req.getRequestURI().substring(1).split("/");
		System.out.println(System.currentTimeMillis() +  req.getRequestURI());

		if ("CREATE".equals(pathParams[0])) {
			String name = pathParams[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map = new Gson().fromJson(getReqBody(req), map.getClass());
			System.out.println(name + ";" + map);
			MessageInfo info = new MessageInfo(
					((Double)map.get("QueueSize")).intValue(),
					((Double)map.get("ProcessTimeout")).intValue(), 
					((Double)map.get("WaitTime")).intValue(),
					((Double)map.get("MaxFailCount")).intValue());
			if (service.create(name, info)) {
				res.getWriter().write(OK);
				new Thread(new WaitThread(name, service)).start();
			} else {
				res.getWriter().write(EXIST);
			}
		} else if ("SEND".equals(pathParams[0])) {			
			String name = pathParams[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map = new Gson().fromJson(getReqBody(req), map.getClass());
			String message = (String)map.get("Message");
			if (service.send(name, message)) {
				res.getWriter().write(OK);
			} else {
				res.getWriter().write(FULL);
			}
		} else if ("ACK".equals(pathParams[0])) {
			String name = pathParams[1];
			String id = pathParams[2];
			service.ack(name, id);
			res.getWriter().write(OK);
		} else if ("FAIL".equals(pathParams[0])) {
			String name = pathParams[1];
			String id = pathParams[2];
			service.fail(name, id);
			res.getWriter().write(OK);
		}
		res.setStatus(200);
	}
	
	public String getReqBody(HttpServletRequest req) throws IOException {
		return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}

}
