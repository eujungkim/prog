##### 콘솔 입력
```
Scanner scanner = new Scanner(System.in);
while (scanner.hasNextLine()) {
  String in = scanner.nextLine();
  // do something      
}
```
##### 파일 reader
```
FileReader reader = new FileReader(line + ".txt");
BufferedReader buf = new BufferedReader(reader);
String filename = buf.readLine();
```
```
FileReader a = new FileReader(proxyName);
BufferedReader b = new BufferedReader(a);

List<String> values = new ArrayList<>();
String line;
while ((line = b.readLine()) != null) {
    values.add(line);
}
```
##### Servlet
```
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
public class MyServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String query = req.getQueryString();
    String uri = req.getRequestURI();
      
    // do someting
    resp.setStatus(contentRes.getStatus());
    resp.setHeader("x-requestId", req.getHeader("x-requestId"));
    resp.setContentType(contentRes.getHeaders().get("Content-Type"));
    resp.getWriter().write(contentRes.getContentAsString());
    // resp.getWriter().write(new Gson().toJson(traceMap.get(requestId)));
    resp.getWriter().flush();
  }
}   
```
##### Http Client
```
HttpClient httpClient = new HttpClient();
httpClient.start();
ContentResponse contentRes = httpClient.newRequest(newUri).method(method)
  .header("x-requestId", req.getHeader("x-requestId"))
  .send();
```
##### read json file
```
static ProxyData readProxyFile(String file) throws Exception {
  FileReader a = new FileReader(file);
  Gson gson = new Gson();
  ProxyData proxyData = gson.fromJson(new JsonReader(a), ProxyData.class);
  return proxyData;
}
  
//to json
String jsonString = new Gson().toJson(someObject);
```
