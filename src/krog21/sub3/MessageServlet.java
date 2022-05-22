package com.lgcns.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
		
		String[] pathParams = req.getRequestURI().substring(1).split("/");
		if ("RECEIVE".equals(pathParams[0])) {
			String name = pathParams[1];
			Message m = service.receive(name);
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
		if ("CREATE".equals(pathParams[0])) {
			String name = pathParams[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map = new Gson().fromJson(getReqBody(req), map.getClass());
			int size = ((Double)map.get("QueueSize")).intValue();
			if (service.create(name, size)) {
				res.getWriter().write(OK);
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
