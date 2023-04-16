#### System
##### 콘솔 입력
```
Scanner scanner = new Scanner(System.in);
while (scanner.hasNextLine()) {
  String in = scanner.nextLine();
  // do something      
}
```

#### 파일
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
##### directory 생성
```
new File("backup").deleteOnExit();
new File("backup").mkdir();
```

#### convert
##### gson
```
// json string to object
String body = getReqBody(req);
Gson gson = new Gson();
CreateInput input = gson.fromJson(body, CreateInput.class);
// object to json string
gson.toJson(new CommonOutput("OK"))
```
##### read json file
```
static ProxyData readProxyFile(String file) throws Exception {
  FileReader a = new FileReader(file);
  Gson gson = new Gson();
  ProxyData proxyData = gson.fromJson(new JsonReader(a), ProxyData.class);
  return proxyData;
}
```

#### HTTP
##### server
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
##### servlet
```
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
##### read request body
```
public String getReqBody(HttpServletRequest req) throws IOException {
  return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
}
```
##### response writer
```
res.getWriter().write(gson.toJson(new CommonOutput("OK")));
```
##### Http Client
```
// get
HttpClient httpClient = new HttpClient();
httpClient.start();
ContentResponse contentRes = httpClient
  .newRequest("http://127.0.0.1:8080/queueInfo")
  .method(HttpMethod.GET)
  .header("x-requestId", req.getHeader("x-requestId"))
  .send();
String jsonInput = contentRes.getContentAsString();

// post
HttpClient httpClient = new HttpClient();
httpClient.start();
Gson gson = new Gson();
String content = gson.toJson(new OutputQueueData(resultList));
httpClient.newRequest(outputQueueURI)
  .method(HttpMethod.POST)
  .content(new StringContentProvider(content))
  .send();
```
#### concurrent
##### concurrent collection
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
##### thread start
```
Thread timeChecker = new Thread(new Runnable() {
  @Override
  public void run() {
    // do something    
  });
timeChecker.start();
```
##### ScheduledExecutorService
```
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
// 지정된 delay 후 일회성 실행
executorService.schedule(() -> {
  // do something (예. 요청 후 기간 내 처리되었는지 체크 등등)
}, infoMap.get(queueName).ProcessTimeout, TimeUnit.SECONDS);

// initialDelay 후 period를 적용해 주기적 실행
scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
// 5, 5, TimeUnit.SECONDS); // 5초 후부터 5초 간격

// initialDelay 후 한 실행 종료와 다음 실행 시작 사이에 delay를 적용해 주기적 실행
scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
// 0, 10, TimeUnit.MILLISECONDS); // 바로 시작, 10ms 이후 다시 시작(1개만 실행됨)
```
#### process
##### Java Process
```
public final class JavaProcess {

  private JavaProcess() {}        

  public static int exec(Class klass, List<String> args) throws IOException, InterruptedException {
    String javaHome = System.getProperty("java.home");
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
    String classpath = System.getProperty("java.class.path");
    String className = klass.getName();

    List<String> command = new LinkedList<String>();
    command.add(javaBin);
    command.add("-cp");
    command.add(classpath);
    command.add(className);
    if (args != null) {
      command.addAll(args);
    }
    
    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.inheritIO().start();
    process.waitFor();
    return process.exitValue();
  }  
  // use : JavaProcess.exec(MyProcess.class, ins); // ins : List<String>
}
```
##### Java Process run
```
List<String> ins = new ArrayList<>();
ins.add(i + ""); // processNo
ins.add(input.outputQueueBatchSize + "");
ins.add(input.outputQueueURI);
ins.addAll(map.get(i));
			
Runnable a = new Runnable() {
  @Override
  public void run() {
    try {
      JavaProcess.exec(MyProcess.class, ins);
    } catch (Exception e) {
      e.printStackTrace();
    }					
  }			
};
new Thread(a).start();
```

#### serialization
##### serialization(직렬화), deserialization(역직렬화)
```
class BackupData implements Serializable  {
  // do something
}

try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("backup/process" + processNo + ".dat"))) {
  BackupData restoredBackupData = (BackupData) in.readObject();								
  // do something
} catch (IOException | ClassNotFoundException e) {
  e.printStackTrace();
}

BackupData backupData = new BackupData(processNo);
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backup/process" + processNo + ".dat"))) {
  out.writeObject(backupData);
} catch (IOException e) {
  e.printStackTrace();
}
```
