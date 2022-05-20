package krog.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class MyServer {

	public static void main(String[] args) throws Exception {
		new MyServer().start();
	}

	public void start() throws Exception {
		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(8080);
		server.addConnector(http);

		/*
		ServerConnector http2 = new ServerConnector(server);
		http2.setHost("127.0.0.1");
		http2.setPort(8081);
		server.addConnector(http2);
		*/

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyServlet.class, "/mypath");
		servletHandler.addServletWithMapping(MyServlet.class, "/mypath/*");
		servletHandler.addServletWithMapping(MyServlet.class, "/mypath/mypath2/*");

		server.setHandler(servletHandler);

		server.start();
		server.join();
	}
}
