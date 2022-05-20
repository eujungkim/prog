package krog.jetty;

import java.net.HttpCookie;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

public class MyClient {

	public static void main(String[] args) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/mypath").method(HttpMethod.GET)
				.send();
		System.out.println(contentRes.getContentAsString());
		
		contentRes = httpClient.newRequest("http://127.0.0.1:8080/mypath/hello").method(HttpMethod.GET)
				.send();
		System.out.println(contentRes.getContentAsString());
		
		contentRes = httpClient.newRequest("http://127.0.0.1:8080/mypath/mypath2/hello").method(HttpMethod.GET)
				.send();
		System.out.println(contentRes.getContentAsString());
		
		contentRes = httpClient.newRequest("http://127.0.0.1:8080/mypath/user/kim").method(HttpMethod.GET)
				.send();
		System.out.println(contentRes.getContentAsString());
		
		contentRes = httpClient
				.newRequest("http://127.0.0.1:8080/mypath/user/kim")
				.method(HttpMethod.POST)
				.param("param1", "paramValue1")
				//.attribute("attribute1", "attributeValue1")
				.header("header1", "headerValue1")
				.header(HttpHeader.CONTENT_TYPE, "application/json")
				.cookie(new HttpCookie("key1", "value1"))
				.cookie(new HttpCookie("key2", "value2"))
				.content(new StringContentProvider("{\"name\":\"hong\",\"age\":\"24\"\n,\"tel\":\"1234\"}"), "utf-8")
				.send();
		System.out.println(contentRes.getContentAsString());
		System.out.println(contentRes.getHeaders());
		System.out.println(contentRes.getStatus());
		
		contentRes = httpClient
				.newRequest("http://127.0.0.1:8080/mypath/user/kim")
				.method(HttpMethod.PUT)
				.param("param1", "paramValue1")
				//.attribute("attribute1", "attributeValue1")
				.header("header1", "headerValue1")
				.header(HttpHeader.CONTENT_TYPE, "application/json")
				.cookie(new HttpCookie("key1", "value1"))
				.cookie(new HttpCookie("key2", "value2"))
				.content(new StringContentProvider("{\"name\":\"hong\",\"age\":\"24\"\n,\"tel\":\"1234\"}"), "utf-8")
				.send();
		System.out.println(contentRes.getContentAsString());
		System.out.println(contentRes.getHeaders());
		System.out.println(contentRes.getStatus());
		
		contentRes = httpClient
				.newRequest("http://127.0.0.1:8080/mypath/user/kim")
				.method(HttpMethod.DELETE)
				.param("param1", "paramValue1")
				//.attribute("attribute1", "attributeValue1")
				.header("header1", "headerValue1")
				.header(HttpHeader.CONTENT_TYPE, "application/json")
				.cookie(new HttpCookie("key1", "value1"))
				.cookie(new HttpCookie("key2", "value2"))
				.content(new StringContentProvider("{\"name\":\"hong\",\"age\":\"24\"\n,\"tel\":\"1234\"}"), "utf-8")
				.send();
		System.out.println(contentRes.getContentAsString());
		System.out.println(contentRes.getHeaders());
		System.out.println(contentRes.getStatus());
		
		httpClient.stop();
	}
}
