package krog.jettywebsocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
 
public class WSSocket extends WebSocketAdapter {
    private Session outbound;

  @Override
  public void onWebSocketConnect(Session sess) {
    super.onWebSocketConnect(sess);
    this.outbound = sess;        
    System.out.println("- Socket Connected: " + sess);
  }
 
  @Override
  public void onWebSocketText(String message) {
    super.onWebSocketText(message);
    System.out.println("- Received Text message: " + message);
    if ((outbound != null) && (outbound.isOpen()))
    {
        String echoMessage = outbound.hashCode() + " : " + message;
        outbound.getRemote().sendString(echoMessage, null);
    }
  }
 
  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);
    System.out.println("- Socket Closed: [" + statusCode + "] " + reason);
  }
 
  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace(System.err);
  }
}
