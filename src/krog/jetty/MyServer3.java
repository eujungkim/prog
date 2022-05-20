package krog.jetty;

import javax.servlet.MultipartConfigElement;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MyServer3 {
	public static void main(String[] args) throws Exception {

		int port = 8080;
		Server server = new Server(port);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		ServletHolder fileUploadServletHolder = new ServletHolder(new UploadFile());
		//fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
		fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement((String)null));
		context.addServlet(fileUploadServletHolder, "/UploadFile");
		
		MyServlet myServlet = new MyServlet();
		ServletHolder myServletHolder = new ServletHolder(myServlet);
		context.addServlet(myServletHolder, "/mypath");
		context.addServlet(myServletHolder, "/mypath/*");
		context.addServlet(myServletHolder, "/mypath/mypath2/*");

		server.setHandler(context);
		server.start();
		server.join();
	}
}
