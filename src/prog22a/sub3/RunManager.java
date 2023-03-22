import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class RunManager {
	
	public static ProxyData data;

	public static void main(String[] args) throws Exception {

		String filename = (args[0]);
		
		ProxyData proxyData = readProxyFile(filename);
		data = proxyData;
		System.out.println(proxyData);
		int port = Integer.parseInt(proxyData.port);
		new MyServer().start(port);
		
	}

	static ProxyData readProxyFile(String file) throws Exception {
		FileReader a = new FileReader(file);
		Gson gson = new Gson();
		ProxyData proxyData = gson.fromJson(new JsonReader(a), ProxyData.class);
		return proxyData;
	}
}

class MyServer {
	public void start(int port) throws Exception {
		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(port);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyServlet.class, "/");
		server.setHandler(servletHandler);

		server.start();
		server.join();
	}
}

class ProxyData {
	String port;
	List<Route> routes = new ArrayList<>();
	@Override
	public String toString() {
		return "ProxyData [port=" + port + ", routes=" + routes + "]";
	}
}

class Route {
	@Override
	public String toString() {
		return "Route [pathPrefix=" + pathPrefix + ", url=" + url + "]";
	}
	String pathPrefix;
	String url;
}
