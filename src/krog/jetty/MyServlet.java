package krog.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		//System.out.println(req.getLocalAddr());
		//System.out.println(req.getLocalPort());
		//System.out.println(req.getRequestURL());
		
		System.out.println(req.getServletPath());
		System.out.println(req.getPathInfo());
		
		if (req.getPathInfo() != null) {
		    String[] pathParams = req.getPathInfo().substring(1).split("/");
		    System.out.println(Arrays.toString(pathParams));
		}

		res.setStatus(200);
		res.getWriter().write("Hello!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    System.out.println("Do POST");

		String[] pathParams = getPathParams(req);
		
		System.out.println(getReqBody(req));
		System.out.println(req.getParameterMap());
		System.out.println(getHeaderMap(req));
		System.out.println(getCookieMap(req));
		
		//System.out.println(getAttributes(req));
		
		res.setStatus(200);
		//res.setHeader("myHeader1", "myHeaderValue1");
		res.getWriter().write(Arrays.toString(pathParams));
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//super.doPut(req, res);
	    System.out.println("Do PUT");
	    
		String[] pathParams = getPathParams(req);
		
		System.out.println(getReqBody(req));
		System.out.println(req.getParameterMap());
		System.out.println(getHeaderMap(req));
		System.out.println(getCookieMap(req));
		
		//System.out.println(getAttributes(req));
		
		res.setStatus(200);
		//res.setHeader("myHeader1", "myHeaderValue1");
		res.getWriter().write(Arrays.toString(pathParams));
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//super.doDelete(req, res);
	    System.out.println("Do DELETE");
	    
		String[] pathParams = getPathParams(req);
		
		System.out.println(getReqBody(req));
		System.out.println(req.getParameterMap());
		System.out.println(getHeaderMap(req));
		System.out.println(getCookieMap(req));
		
		//System.out.println(getAttributes(req));
		
		res.setStatus(200);
		//res.setHeader("myHeader1", "myHeaderValue1");
		res.getWriter().write(Arrays.toString(pathParams));
	}
	
	public String[] getPathParams(HttpServletRequest req) {
		String[] pathParams = {};		
		if (req.getPathInfo() != null) {
		    pathParams = req.getPathInfo().substring(1).split("/");
		    
		}
	    System.out.println(Arrays.toString(pathParams));
		return pathParams;
	}
	
	public String getReqBody(HttpServletRequest req) throws IOException {
		return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	public Map<String, String> getHeaderMap(HttpServletRequest req) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = req.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = req.getHeader(name);
			map.put(name, value);
		}
		return map;
	}
	
	public Map<String, String> getCookieMap(HttpServletRequest req) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		Cookie[] names = req.getCookies();
		for (Cookie tmp : names) {
			map.put(tmp.getName(), tmp.getValue());
		}
		return map;
	}

	// post에서는 attribute를 보냈는데 받을 수 없다??
	public Map<String, Object> getAttributes(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> names = req.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = req.getAttribute(name);
			map.put(name, value);
		}
		return map;
	}
	
	public String getReqBody2(HttpServletRequest req) throws IOException {
		StringBuffer sb = new StringBuffer();
	    BufferedReader bufferedReader = null;
	    try {
	        bufferedReader =  req.getReader();
	        char[] charBuffer = new char[128];
	        int bytesRead;
	        while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) {
	            sb.append(charBuffer, 0, bytesRead);
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    return sb.toString();
	}
	
}
//https://msdalp.github.io/2014/03/04/jetty-embedded/

// open https
// append file
