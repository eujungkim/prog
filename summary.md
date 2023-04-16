##### read request body
```
public String getReqBody(HttpServletRequest req) throws IOException {
  return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
}
```
#### gson
```
// json string to object
String body = getReqBody(req);
Gson gson = new Gson();
CreateInput input = gson.fromJson(body, CreateInput.class);
// object to json string
gson.toJson(new CommonOutput("OK"))
```
#### servlet
```
public class MyServlet extends HttpServlet {
```
#### server
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
```
#### response writer
```
res.getWriter().write(gson.toJson(new CommonOutput("OK")));
```
#### concurrent collection
```
// List
List<Message> list = Collections.synchronizedList(new ArrayList<Message>());
List<String> list1 = new CopyOnWriteArrayList();
// Queue
Queue<String> queue = new ConcurrentLinkedQueue<>();
// Set
Set<String> set0 = Collections.synchronizedSet(new HashSet<String>());
Set<String> set2 = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
Set<String> set3 = ConcurrentHashMap.newKeySet();
// SortedSet
Set<String> set1 = Collections.synchronizedSortedSet(new TreeSet<String>()); 
Set<String> set2 = new ConcurrentSkipListSet<>();

Map<String, List<Message>> queueMap = new ConcurrentHashMap<>();
```
#### thread
```
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
// 지정된 delay 후 일회성 실행
executorService.schedule(() -> {
  // do something (예. 요청 후 기간 내 처리되었는지 체크 등등)
}, infoMap.get(queueName).ProcessTimeout, TimeUnit.SECONDS);
// initialDelay 후 period를 적용해 주기적 실행
scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
// initialDelay 후 한 실행 종료와 다음 실행 시작 사이에 delay를 적용해 주기적 실행
scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
```
