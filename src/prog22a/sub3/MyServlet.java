import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public class MyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public String getUrl(String path) {
		for (Route route : RunManager.data.routes) {
			if (route.pathPrefix.equals(path)) {
				return route.url;
			}
		}
		return null;
	}

	private void doSomething(HttpServletRequest req, HttpServletResponse resp, HttpMethod method) throws ServletException, IOException {
		try {
			String path = "/" + req.getRequestURI().split("/")[1];
			String query = req.getQueryString();
			String url = getUrl(path);
			String uri = req.getRequestURI();
			
			String newUri = url + uri;
			if (query != null) {
				newUri += "?" +  query;
			}
			System.out.println(newUri);

			HttpClient httpClient = new HttpClient();
			httpClient.start();
			ContentResponse contentRes = httpClient.newRequest(newUri).method(method)
					.send();
			
			resp.setStatus(contentRes.getStatus());
			resp.setContentType(contentRes.getHeaders().get("Content-Type"));
			System.out.println(contentRes.getContentAsString());
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
