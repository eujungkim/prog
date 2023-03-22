package com.lgcns.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static Map<String, Trace> traceMap = new HashMap<>();
	public static Map<String, String> statusMap = new HashMap<>();
	public static Map<String, Integer> delayMap = new HashMap<>();
	public static Map<String, Boolean> blockMap = new HashMap<>();
	public static Map<String, Long> blockTimeMap = new HashMap<>();

	public String getUrl(String path) {
		for (Route route : RunManager.data.routes) {
			if (route.pathPrefix.equals(path)) {
				return route.url;
			}
		}
		return null;
	}
	
	public Route getRoute(String path) {
		for (Route route : RunManager.data.routes) {
			if (route.pathPrefix.equals(path)) {
				return route;
			}
		}
		return null;
	}
	
	private void setTrace(String requestId, String data) {
		if (data == null) return;
		String[] tmp = data.split("#");
		String start = "START";
		List<String> targetList = new ArrayList<>();
		for (String a : tmp) {
			String b = a.split("@")[0];
			
			if (!a.equals("null") && a.length() > 0 && !b.equals(start)) {
				targetList.add(a);
				start = b;
			}
		}
		
		Trace parent = traceMap.get(requestId);
		for (int i = 1; i < targetList.size(); i++) {
			String target = null;
			String status = null;
			if (targetList.get(i).contains("@")) {
				target = targetList.get(i).split("@")[0];
				status = targetList.get(i).split("@")[1];
			} else {
				target = targetList.get(i);
			}
			
			Trace trace = new Trace();
			trace.target = target;
			trace.status = status;

			if (parent.services == null) {
				parent.services = new ArrayList<>();
			}
			boolean add = true;
			for (Trace t1 : parent.services) {
				if (t1.target.equals(target)) {
					add = false;
					parent = t1;
					break;
				}
			}
			if (add) {
				parent.services.add(trace);
				parent = trace;				
			}
		}
	}
	
	private void setStatus(Trace trace, String target, String status) {
		if (trace.target.equals(target)) {
			trace.status = status;
			return;
		} else if (trace.services != null) {
			for (Trace tmp : trace.services) {
				setStatus(tmp, target, status);
			}
		}
	}

	private void doSomething(HttpServletRequest req, HttpServletResponse resp, HttpMethod method)
			throws ServletException, IOException {
		try {
			
			long start = System.currentTimeMillis();
			String path = "/" + req.getRequestURI().split("/")[1];
			
			boolean checkBlock = false;
			if (getRoute(path) != null && getRoute(path).healthCheck != null) {
				checkBlock = true;
			}
			
			if (path.startsWith("/trace")) {
				String requestId = req.getRequestURI().split("/")[2];
				resp.getWriter().write(new Gson().toJson(traceMap.get(requestId)));
				resp.getWriter().flush();
				return;
			}
			
			HttpClient httpClient = new HttpClient();
			httpClient.start();

			if (checkBlock && blockMap.containsKey(path) && blockMap.get(path)) {
				Route route = getRoute(path);				
				long gap = System.currentTimeMillis() - blockTimeMap.get(path);
				
				if (gap > route.retryAfter) {
					ContentResponse cr = httpClient.newRequest(route.url + route.healthCheck).method(HttpMethod.GET).send();
					if (cr.getStatus() == 200) {
						delayMap.put(path, 0);
						blockMap.put(path, false);
						blockTimeMap.remove(path);
					}
				}
								
				if (blockMap.containsKey(path) && blockMap.get(path)) {
					resp.setStatus(503);
					resp.getWriter().write("{\"result\": \"service unavailable\"}");
					resp.getWriter().flush();
					return;
				}
			}
			
			String query = req.getQueryString();
			String url = getUrl(path);
			String uri = req.getRequestURI();

			String newUri = url + uri;
			if (query != null) {
				newUri += "?" + query;
			}

			if (req.getHeader("x-trace") == null) {
				// trace map에 빈 데이터 하나 만들기
				Trace trace = new Trace();
				trace.target = req.getRequestURL() + "";
				traceMap.put(req.getHeader("x-requestId"), trace);
			}

			ContentResponse contentRes = httpClient.newRequest(newUri).method(method)
					.header("x-requestId", req.getHeader("x-requestId"))
					.header("x-trace", req.getHeader("x-trace") + "#" + req.getRequestURL() + "#" + url + uri).send();
			
			if (checkBlock) {
				long delay = System.currentTimeMillis() - start;
				Route route = getRoute(path);
				if (delay > route.responseTimeLimit) {
					if (!delayMap.containsKey(path)) {
						delayMap.put(path, 0);
					}
					delayMap.put(path, delayMap.get(path) + 1);
					if (delayMap.get(path) >= route.maxDelayCount) {
						blockMap.put(path, true);
						blockTimeMap.put(path, System.currentTimeMillis());
					}
				}
			}

			if (traceMap.containsKey(req.getHeader("x-requestId"))) {
				if (req.getHeader("x-trace") != null) {
					// tracemap 채우기
					setTrace(req.getHeader("x-requestId"), contentRes.getHeaders().get("x-trace"));
				}
				// status 채우기
				setStatus(traceMap.get(req.getHeader("x-requestId")), req.getRequestURL() + "", contentRes.getStatus() + "");
				setStatus(traceMap.get(req.getHeader("x-requestId")), url + uri, contentRes.getStatus() + "");
			}

			resp.setHeader("x-requestId", req.getHeader("x-requestId"));
			resp.setHeader("x-trace", 
					req.getHeader("x-trace") 
					+ "#" + req.getRequestURL() + "@" +contentRes.getStatus() 
			        + "#" + url + uri + "@" +contentRes.getStatus() + "#"
					+ contentRes.getHeaders().get("x-trace"));
			resp.setStatus(contentRes.getStatus());
			resp.setContentType(contentRes.getHeaders().get("Content-Type"));
			resp.getWriter().write(contentRes.getContentAsString());
			resp.getWriter().flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doSomething(req, resp, HttpMethod.GET);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doSomething(req, resp, HttpMethod.POST);
	}

}

//추적 데이터
class Trace {
	String target;
	String status;
	List<Trace> services = null;

	@Override
	public String toString() {
		return "Trace [target=" + target + ", status=" + status + ", services=" + services + "]";
	}
}
