package krog.jettywebsocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WSServer {
  public static void main(String[] args) {
    Server server = new Server();
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8080);
    server.addConnector(connector);
 
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
 
    ServletHolder holderEvents = new ServletHolder("ws-tests", WSServlet.class);
    context.addServlet(holderEvents, "/wstest/*");
 
    try {
      server.start();
      server.dump(System.err);
      server.join();
    } catch ( Throwable t ) {
      t.printStackTrace(System.err);
    }
  }
}
