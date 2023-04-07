import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class RunManager {

	public static void main(String[] args) throws Exception {
		new MyServer().start(8080);
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
