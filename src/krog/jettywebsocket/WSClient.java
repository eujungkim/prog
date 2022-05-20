package krog.jettywebsocket;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class WSClient {
	public static void main(String[] args) {
		URI uri = URI.create("ws://localhost:8080/wstest/");

		WebSocketClient client = new WebSocketClient();
		try {
			try {
				client.start();
				WSSocket2 socket = new WSSocket2();
				Future<Session> future = client.connect(socket, uri);
				Session session = future.get();
				for (int i = 0; i < 10; i++) {
					session.getRemote().sendString("Hello, sarc.io!");
				}
				Thread.sleep(1000);
				session.close();
			} finally {
				client.stop();
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
}
