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
##### 파일 reader, writer
```
FileReader reader = new FileReader(line + ".txt");
BufferedReader buf = new BufferedReader(reader);
String filename = buf.readLine();
```
```
FileReader reader = new FileReader(intputFile); // String inputFile
FileWriter writer = new FileWriter(outputFile); // String outputFile
BufferedReader buf = new BufferedReader(reader);
PrintWriter wbuf = new PrintWriter(writer, true);

List<String> values = new ArrayList<>();
String line;
while ((line = b.readLine()) != null) {
    values.add(line);
    wbuf.println(line);
}
wbuf.close();
buf.close();
writer.close();
reader.close();
```
```
InputStream in = new FileInputStream(inputFile);
OutputStream out = new FileOutputStream(outputFile);
byte[] buf = new byte[1024];
while ( (len = in.read(buf)) != -1) {
  out.write(buf, 0, len);
}
```
all reader writer
https://github.com/eujungkim/prog/blob/main/src/krog/util/AllReaderWriter.java
##### directory 생성
```
new File("backup").deleteOnExit();
File dest = new File("./output/output2");
if (!dest.exists()) {
  dest.mkdirs();
}
```
##### random access file (read)
```
int seekSize = 5; // 읽어들일 사이즈
// 읽기 전용으로 파일을 읽음.
RandomAccessFile rdma = new RandomAccessFile("/test.txt","r");
String line = "";
while ((line = rdma.readLine()) != null) {
  System.out.println(line); // 전체 문자열을 출력
}
System.out.println("total length : " + rdma.length()+"\n"); // 문자열 총 길이
byte[] data = null;
// 루프 사이즈 = 총길이/seekSize + (총길이%seekSize의 나머지가 0이면 0을 반환 0이아니면 1을 반환)
long size = rdma.length()/seekSize+(rdma.length()%seekSize == 0 ? 0:1);
for (int i = 0; i < size; i++) {
  data = new byte[seekSize];
  // seekSize 만큼 증가
  rdma.seek(i*seekSize);
  rdma.read(data);
  // 바이트 데이터를 문자열로 변환(trim()을 사용해 공백을 제거) 
  System.out.printf("pointer : %02d  str : %s \n" , rdma.getFilePointer(), new String(data).trim());
}	
rdma.close(); // 파일 닫기
```
##### random access file (write)
```
public static String lock = "lock";
///...
File file = new File(fileName);
RandomAccessFile rf = new RandomAccessFile(file, "rw");
for (int i = 0; i < 10; i++) {
  synchronized (RandomAccessFileWriter.lock) {
    rf.seek(rf.length());
    rf.write((Thread.currentThread().getName() + "-" + i + "\n").getBytes());
  }
}
rf.close();
```
##### file copy
```
// Files.copy 이용
// 1. 원본 File, 복사할 File 준비
File file = new File("./sp/z_pro/Test.java");
File newFile = new File("./sp/z_pro//Test3.java");
// 2. 복사
Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

// FileChannel 이용
// 1. 원본 File, 복사할 File 준비
RandomAccessFile af1 = new RandomAccessFile("./sp/z_pro/Test.java", "r");
RandomAccessFile af2 = new RandomAccessFile("./sp/z_pro/Test4.java", "rw");
// 2. FileChannel 생성
FileChannel source = af1.getChannel();
FileChannel target = af2.getChannel();
// 3. 복사
source.transferTo(0, source.size(), target);
```
##### file list
```
File directory = new File(".");
File[] list = directory.listFiles();
for (File file : list) {
  if (file.isDirectory()) {
    System.out.println("directory : " + file.getName());
  } else {
    System.out.println("file : " + file.getName());
  }
}
```
##### 하위 폴더 탐색
```
void fileSearchAll(String path) {
  File directory = new File(path);
  File[] list = directory.listFiles();
  for (File file : list) {
    if (file.isDirectory()) {
      fileSearchAll(file.getPath());
    } else {
      System.out.println(file.getName());
    }
  }
}
```
##### FileWatch
```
package multithread;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class FileChangeDetector {
    public static void main(String[] args) throws IOException {
        String directoryPath = "data"; // 감지할 디렉토리 경로 설정

        // 디렉토리 감시
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directory = Paths.get(directoryPath);
        directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("파일 변경 감지 중...");
        Map<String, Long> cur = new HashMap<>();

        while (true) {
            WatchKey key;
            try {
                key = watchService.take(); // 변경 이벤트 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue; // 오버플로우 이벤트 무시
                }

                // 변경된 파일 경로 가져오기
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path changedFilePath = directory.resolve(ev.context());
                
                File file = changedFilePath.toFile();                
                RandomAccessFile rf = new RandomAccessFile(file, "rw");
                
                long length = file.length();
                long before = cur.getOrDefault(file.getAbsolutePath(), 0l);
                cur.put(file.getAbsolutePath(), length);
                
                String diff = getDiffContent(before, length, rf);
                
                // 변경된 파일 내용 출력
                System.out.println("변경된 파일: " + changedFilePath + " " + file.length());
                //printFileContent(changedFilePath.toFile());
                System.out.println(diff);
            }

            boolean valid = key.reset(); // WatchKey 재사용
            if (!valid) {
                break; // 감시 중지
            }
        }
    }
    
    private static String getDiffContent(long start, long end, RandomAccessFile rf) throws IOException {
    	byte[] data = new byte[Long.valueOf(end - start).intValue()];
    	rf.seek(start);
    	rf.read(data);
    	rf.close();
    	return new String(data);
    }

    private static void printFileContent(File file) throws IOException {
        // 파일 내용 출력
        System.out.println("파일 내용:");
        Files.lines(file.toPath()).forEach(System.out::println);
        System.out.println();
    }
}
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

// Gson.toJson()시 null인 필드도 포함하려면
Gson gson = new GsonBuilder().serializeNulls().create();

// field에 transient modifier를 사용하면 Gson serialization/deserialization에서 제외됨
// 제외 로직을 상세하게 적용하려면 com.google.gson.ExclusionStrategy 사용 (https://www.baeldung.com/gson-exclude-fields-serialization#exclusionstrategy)
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
  .param("param1", "paramValue1")
  .header(HttpHeader.CONTENT_TYPE, "application/json")
  .cookie(new HttpCookie("key1", "value1"))
  .content(new StringContentProvider(content))
  .send();

// multipart
MultiPartContentProvider multiPart = new MultiPartContentProvider();
multiPart.addFieldPart("fruit", new StringContentProvider("apple"), null);
multiPart.addFilePart("icon", "img.png", new PathContentProvider(Paths.get("testimg.png")), null);
		multiPart.close();
		httpClient.POST("http://127.0.0.1:8080/UploadFile")
		    .content(multiPart)
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
##### blocking queue
```
public class BlockingQueueSample {
  int BOUND = 10;
  private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(BOUND); // bound 지정하지 않으면 integer.MAX_VALUE
  public String get() throws InterruptedException {
    // 선두의 요소를 구함(큐에서 삭제됨), 추가가 될 때까지 블로킹 됨
    return queue.take();
  }
  public void add(String value) throws InterruptedException {
    // 큐에 요소를 추가, 취득할 수 있을 때까지 블로킹 됨
    queue.put(value);
  }
}
```
##### blocking queue 사용 예
```
class Producer implements Runnable {
  private final BlockingQueue<String> queue;
  int count;
  Producer(BlockingQueue<String> q) {
    queue = q;
  }
  public void run() {
    try {
      while (true) {
        Thread.sleep(100);
        queue.put(produce());
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }
  String produce() {
    return count++ + "";
  }
}

class Consumer implements Runnable {
  private final BlockingQueue<String> queue;
  Consumer(BlockingQueue<String> q) {
    queue = q;
  }
  public void run() {
    try {
      while (true) {
        consume(queue.take());
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }
  void consume(String x) {
    System.out.println("consumer " + " " + Thread.currentThread() + " " + x );
  }
}

public class Setup {
  public static void main(String[] args) {
    // Queue가 꽉찼을때의 삽입 시도 / Queue가 비어있을때의 추출 시도 block
    BlockingQueue<String> q; 
    // 생성 후 크기 변경 불가
    q = new ArrayBlockingQueue<>(10);
    // capacity를 초기에 정해주지 않는경우 integer.MAX_VALUE로 자동설정
    q = new LinkedBlockingQueue<>(10);
    // PriorityQueue와 같은 정렬방식을 지니는 용량제한이 없는 Queue
    q = new PriorityBlockingQueue<>(10);
    // Queue 내부로의 insert 작업이 다른 스레드의 remove 작업과 반드시 동시에 일어나야한다.
    // null 수용 불가
    q = new SynchronousQueue<>();

    Producer p = new Producer(q);
    Consumer c1 = new Consumer(q);
    Consumer c2 = new Consumer(q);
    new Thread(p).start();
    new Thread(c1).start();
    new Thread(c2).start();
  }
}
```

#### thread
##### thread start
```
Thread t1 = new Thread(new Runnable() {
  @Override
  public void run() {
    // do something    
  });
t1.start();
t1.join(); // thread 종료될 때까지
```
##### ScheduledExecutorService
```
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
// 지정된 delay 후 일회성 실행
executorService.schedule(() -> {
  // do something (예. 요청 후 기간 내 처리되었는지 체크 등등)
}, 5, TimeUnit.SECONDS);

// initialDelay 후 period를 적용해 주기적 실행
scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
// 5, 5, TimeUnit.SECONDS); // 5초 후부터 5초 간격

// initialDelay 후 한 실행 종료와 다음 실행 시작 사이에 delay를 적용해 주기적 실행
scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
// 0, 10, TimeUnit.MILLISECONDS); // 바로 시작, 10ms 이후 다시 시작(1개만 실행됨)
```
##### ExecutorService
```
// fixed pool
ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
for (int i = 0; i < 10; i++) {
  fixedThreadPool.execute(new Thread(new RandomWriter("output3.txt")));
}
fixedThreadPool.shutdown();
```
##### callable & future (단건)
```
ExecutorService executorService = Executors.newSingleThreadExecutor();

// java.util.Date를 전달한 작업을 실행
Future<Date> future = executorService.submit(new Callable<Date>(){
  @Override
  public Date call() throws Exception {
    Thread.sleep(1000);
    return new Date();
  }
});

// 결과를 구함(작업 실행이 완료되기까지 블로킹 됨)
Date date = future.get();
System.out.println(date);

// 모든 작업이 종료하면 셧다운
executorService.shutdown();
```
##### callable & future (목록)
```
public class CallableTest {
  public static void main(String[] args) throws Exception {
    int roomCount = 10;

    List<Callable<Map<Integer, String>>> roomStatusJobs = new ArrayList<>();
    for (int roomNo = 1; roomNo <= roomCount; roomNo++) {
      roomStatusJobs.add(new RoomStatusTask(roomNo));
    }

    ExecutorService executorService = Executors.newFixedThreadPool(roomCount);
    List<Future<Map<Integer, String>>> resultList = executorService.invokeAll(roomStatusJobs);

    // 모든 작업이 완료될 때까지 블로킹 됨
    for (Future<Map<Integer, String>> futureMap : resultList) {
      futureMap.get().entrySet().forEach(entry -> {
        System.out.println("roomNo:" + entry.getKey() + ", roomStatus:" + entry.getValue());
      });
    }

    // 작업 완료 후 셧다운 
    executorService.shutdown();
  }
}

class RoomStatusTask implements Callable<Map<Integer, String>> {
  private int roomNo;

  public RoomStatusTask(final int roomNo) {
    this.roomNo = roomNo;
  }

  @Override
  public Map<Integer, String> call() {

    Map<Integer, String> statusMap = new HashMap<>();
    // 서비스 호출
    String roomStatus = getRoomStatus(this.roomNo);

    statusMap.put(this.roomNo, roomStatus);
    return statusMap;
  }

  private String getRoomStatus(int roomNo) {
    try {
      System.out.println("Called Room Status, RoomNo = " + roomNo);
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return roomNo + ": empty";
  }
}
```
##### lock
```
static ReentrantLock lock = new ReentrantLock();
public method1() {
  lock.lock();
  try {
  } fianlly {
    lock.unlock();
  }
}

ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
Lock readLock = reentrantReadWriteLock.readLock();
Lock writeLock = reentrantReadWriteLock.writeLock();
```

#### process
##### Process
```
public class ProcessTest {
  public static void main(String[] args) throws Exception {

    String javaHome = System.getProperty("java.home");
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
    String classPath = System.getProperty("java.class.path");
    String className = Target.class.getName();

    List<String> command = new LinkedList<>();
    command.add(javaBin);
    command.add("-cp");
    command.add(classPath);
    command.add(className);
    command.add("HELLO");

    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();
    BufferedReader inStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
    
    String line = null;
    while ((line = inStream.readLine()) != null) {
      System.out.println(line);
    }
  }
}

public class Target {
  public static void main(String[] args) {
    System.out.println(args[0] + " RESULT");
  }
}
```
##### JavaProcess 
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
```
Process process = new ProcessBuilder("./CODECONV","MESSAGE02").start();
InputStream is = process.getInputStream();
byte[] buff = new byte[9];
int size = is.read(buff);
System.out.println(size);
System.out.println(new String(buff));
```
```
Process theProcess = Runtime.getRuntime().exec(command.toArray(new String[] {}));
BufferedReader inStream = new BufferedReader(new InputStreamReader( theProcess.getInputStream(),"euc-kr"));
List<String> readData = new ArrayList<String>();
String line = null;
while ( ( line = inStream.readLine( ) ) != null ) {
  readData.add(line);
}
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

#### collection
##### tip
```
HashMap<String, Integer> hm = new HashMap<>();
hm.getOrDefault(player, 0)

int[] temp = Arrays.copyOfRange(array, 2, 5);
Arrays.sort(temp);

List<Integer> result = new ArrayList<Integer>();
Collections.sort(result);

// 통상 ArrayList가 더 효율적이지만, LinkedList는 아래의 함수를 제공함
// addFirst(), addLast(), removeFirst(), removeLast(), getFirst(), getLast()
```
```
* List
list.replaceAll(s -> s.toLowerCase());
list.removeIf(s -> s.startsWith("J"));
Collections.sort(list);
Collections.reverse(list);
System.out.println(list1.contains("A"));
System.out.println(list1.containsAll(list2));
System.out.println(list1.indexOf("C"));
System.out.println(list1.lastIndexOf("B"));
list1.addAll(list2);
// 지정한 컬렉션의 모든 요소를 삭제하고, List가 변경되면 true를 반환한다 (차집합)
System.out.println(list1.removeAll(list2));  // => true
// 지정한 컬렉션 이외의 모든 요소를 삭제하고, List가 변경되면 true를 반환한다 (교집합)
System.out.println(list1.retainAll(list3));  // => true
String[] array = list.toArray(new String[list.size()]);
List<String> list = Arrays.asList(array);
 
 * Set
set.removeIf(s -> s.startsWith("J"));
// 지정한 컬렉션의 모든 요소를 삭제하고, Set이 변경되면 true를 반환한다
System.out.println(set1.removeAll(set2));   // => true
// 지정한 컬렉션 이외의 모든 요소를 삭제하고, Set이 변경되면 true를 반환한다
System.out.println(set1.retainAll(set3));   // => true

* Map
map.putIfAbsent("B", "2"); // 키가 없는 경우 추가
String replaced = map.replace("A", "Java"); // A의 값을 Java로 덮어씀(변환된 값은 쓰기 전의 값)
boolean isReplaced = map.replace("B", "VBScript", "JavaScript"); // B값이 VBScript의 경우에만 JavaScript로 덮어씀
map.replaceAll((key, value) -> value.toUpperCase()); // Map의 모든 요소의 값을 대문자로 변환
map.forEach((key, value) -> System.out.println(key + ", " + value));
```
##### sort
```
Comparator<String> co = new Comparator<String>() {
  public int compare(String o1, String o2) {
    return o2.compareTo(o1);
  }
};
Collections.sort(arrayList, co);
Collections.sort(arrayList, (o1, o2) -> o1.compareTo(o2));

// SortedHashMap : key로 정렬
// ConcurrentHashMap : thread safe
```
```
Integer[] c = new Integer[n];
Arrays.sort(c, Collections.reverseOrder());
```

#### etc
##### class loader, reflection
```
File jarFile = new File(ExternalJarPath);
		
URL classURL = new URL("jar:" + jarFile.toURI().toURL() + "!/");
URLClassLoader classLoader = new URLClassLoader(new URL[] {classURL});
		
Class<?> c = classLoader.loadClass("Calculator");
Constructor<?> constructor = c.getConstructor(new Class[]{});
Object object = constructor.newInstance(new Object[]{});
		
Method method = c.getMethod("add", new Class[]{Integer.TYPE, Integer.TYPE});
Object returnValue = method.invoke(object, 1, 2);
```
```
File classFile = new File("C:\\MyClass.class");
URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { classFile.toURI().toURL() });
Class<?> clazz = urlClassLoader.loadClass("aaa.bbb.ccc.MyClass"); // load class
Object obj = clazz.newInstance(); // new instance
int[] tmp = new int[] {};
Method method1 = clazz.getMethod("returnIntArray", tmp.getClass()); // get method with int array input
Object result1 = method1.invoke(obj, new int[] { 1, 5 }); // invoke method
int size = Array.getLength(result1); // get length of array result
int[] output1 = new int[size]; // convert object to array
for (int i = 0; i < size; i++) {
  output1[i] = Array.getInt(result1, i);
}
System.out.println(Arrays.toString(output1));

Method method2 = clazz.getMethod("returnString", String.class, String.class); // get method
Object result2 = method2.invoke(obj, "data1", "data2"); // invoke method
String output2 = (String) result2; // convert object to string
System.out.println(output2);
urlClassLoader.close();
```
```
File jarFile = new File("C:\\myclass.jar");
URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() });
Class<?> clazz = urlClassLoader.loadClass("aaa.bbb.ccc.MyClass2"); // load class
Object obj = clazz.newInstance(); // new instance
Method method = clazz.getMethod("returnString", String.class, String.class); // get method with string inputs
Object result = method.invoke(obj, "data1", "data2");
System.out.println(result);
			
Object result1 = clazz.getMethod("returnIntArray", (new int[] {}).getClass()).invoke(obj, new int[] { 1, 5 }); // get method with int array input
int[] output2 = (int[]) result1; // convert object to array
System.out.println(Arrays.toString(output2));
urlClassLoader.close();
```
##### base64
```
Encoder encoder = Base64.getEncoder();    
String encodedString = encoder.encodeToString(inputString.getBytes("UTF-8"));

Decoder decoder = Base64.getDecoder();
byte[] decodedBytes = decoder.decode(encodedString);
String decodedString = new String(decodedBytes, "UTF-8");
```
##### sha265
```
MessageDigest mDigest = MessageDigest.getInstance("SHA-256");    
byte[] result = mDigest.digest(input.getBytes());
StringBuffer sb = new StringBuffer();	    
for (int i = 0; i < result.length; i++) {
  sb.append(Integer.toString((result[i] & 0xFF) + 0x100, 16).substring(1));
}
System.out.println(sb.toString());
```
##### format string
```
// 참고 : Formatter
String str = "teststring";
int a = 12345;
String format;
format = String.format("%15s", str); // 문자 포맷, String.format("%자릿수s", str) => [     teststring]
format = String.format("%5s", str); // 입력 길이가 자릿수보다 길 경우 그대로 출력 => [teststring]
format = String.format("%-15s", str); // 왼쪽 정렬 => [teststring     ]
format = String.format("%15d", a); // 숫자 포맷, String.format("%자릿수d", num) => [            123]
format = String.format("%015d", a); // 앞에 0 삽입 => [000000000000123]
format = String.format("%02d", a); // 입력 길이가 자릿수보다 길 경우 그대로 출력 => [12345]
format = String.format("%80.3f", 123.45); // 총 8자리, 앞에 0, 뒤에 3자리 소수점 반올림하여 출력
```
##### 2, 8, 16진수
```
int i = 127;    	 
String binaryString = Integer.toBinaryString(i); //2진수
String octalString = Integer.toOctalString(i);   //8진수
String hexString = Integer.toHexString(i);       //16진수
    	 
System.out.println(binaryString); //1111111
System.out.println(octalString);  //177
System.out.println(hexString);    //7f    	 
    	 
int binaryToDecimal = Integer.parseInt(binaryString, 2);
int binaryToOctal = Integer.parseInt(octalString, 8);
int binaryToHex = Integer.parseInt(hexString, 16);
    	 
System.out.println(binaryToDecimal); //127
System.out.println(binaryToOctal);   //127
System.out.println(binaryToHex);     //127
```
##### date
```
// diff
SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
Date date1 = transFormat.parse(strTime1);
Date date2 = transFormat.parse(strTime2);
long gap = date2.getTime() - date1.getTime();
long hourDiff = gap/60/60/1000;

// current date time string
long time = System.currentTimeMillis();
SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss"); // yyyyMMdd : date
String strTime = dayTime.format(new Date(time));

// 문자열 > Date 타입
String strTime = "2023-03-13 12:00:30"
SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = fm.parse(strTime);

// Date 타입 > LocalDateTime 타입
LocalDateTime ldt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); 
```
##### socket
```
// server
public static void main(String[] args) throws IOException {
  ServerSocket listener = new ServerSocket(9090);
  try {
    Socket socket = listener.accept();
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      out.println("test");
    } finally {
      socket.close();
    }
  } finally {
    listener.close();
  }
}
//client
public static void main(String[] args) throws IOException {
  Socket s = new Socket("127.0.0.1", 9090);
  BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
  String answer = input.readLine();
  System.out.println(answer);
}
```
##### string <-> byte array
```
String st = "abc123";
byte[] bytes = st.getBytes("UTF-8");
for (byte b : bytes) System.out.print(b + " ");

String st2 = new String(bytes);
```

##### java command
```
java -cp bin 패키지.메인클래스명 > 입력파일명
// System property java.home, java.class.path 참고
java -cp C:\sp_workspace\sp_java_test\mytest\bin;C:\sp_workspace\sp_java_test\lib\gson-2.8.6.jar; UtilTest
java -cp C:\sp_workspace\java_test\mytest\bin; UtilTest > output.txt
```

##### calendar
```
String today = null;
Date date = new Date();
System.out.println(date); //Thu May 13 13:25:57 KST 2021
 
// 포맷변경 ( 년월일 시분초)
SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
 
// Java 시간 더하기
Calendar cal = Calendar.getInstance();
 
//지금
cal.setTime(date);
today = sdformat.format(cal.getTime());  
System.out.println("지금 : " + today); //05/13/2021 13:25:57
 
// 3분 더하기
cal.add(Calendar.MINUTE, 3);
today = sdformat.format(cal.getTime());  
System.out.println("3분후 : " + today); //05/13/2021 13:28:57
 
// 1시간 전
cal.setTime(date);
cal.add(Calendar.HOUR, -1);
today = sdformat.format(cal.getTime());  
System.out.println("1시간 전 : " + today); //05/13/2021 12:25:57
 
// 하루 전
cal.setTime(date);
cal.add(Calendar.DATE, -1);
today = sdformat.format(cal.getTime());  
System.out.println("1일 전 : " + today); //05/12/2021 13:25:57
 
//월의 마지막 일자
cal.setTime(date);
System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //31
```

##### LocalDateTime
```
LocalDateTime date1 = LocalDateTime.now();
LocalDateTime date2 = date1.minusHours(2);
LocalDateTime start = LocalDateTime.of(2023, Month.MAY, 15, 12, 0);
long se = start.toEpochSecond(ZoneOffset.of(+9));
LocalDateTime start2 = LocalDateTime.ofEpochSecond(se, 0, ZoneOffset.of(+9));
```
##### String, 날짜 변황
```
// Calendar 이용
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date date =  sdf.parse("2023-05-08");
cal.setTime(date);
cal.add(Calendar.YEAR, 3);  //년도 3증가
cal.add(Calendar.MONTH, 3);  //월 3증가
cal.add(Calendar.DATE, 3);  //일 3증가
//calendar -> String 변환
String strDate = sdf.format(cal.getTime());
System.out.println(strDate); //2026-08-11

// LocalDateTime 이용
LocalDateTime start = LocalDateTime.of(2023, Month.MAY, 15, 12, 0);
DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
String startFm = start.format(df); //2023-05-15 12:00:00.000
LocalDateTime start2 = LocalDateTime.parse(startFm, df); //2023-05-15T12:00
LocalDateTime start3 = start.plusDays(3); //2023-05-18T12:00
```
#### csv
##### read csv file
```
List<List<String>> records = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new FileReader("book.csv"))) {
  String line;
  while ((line = br.readLine()) != null) {
    String[] values = line.split(COMMA_DELIMITER);
    records.add(Arrays.asList(values));
  }
}
```
##### write csv file
```
public class WriteCsvFileExample {
  public String convertToCSV(String[] data) {
    return Stream.of(data)
      .map(this::escapeSpecialCharacters)
      .collect(Collectors.joining(","));
  }

  public String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
      data = data.replace("\"", "\"\"");
      escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }
}

// write
List<String[]> dataLines = new ArrayList<>();
dataLines.add(new String[] 
  { "John", "Doe", "38", "Comment Data\nAnother line of comment data" });
dataLines.add(new String[] 
  { "Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\"" });
File csvOutputFile = new File(CSV_FILE_NAME);
try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
  dataLines.stream()
    .map(this::convertToCSV)
    .forEach(pw::println);
}
```
#####

#### AI
https://www.baeldung.com/deeplearning4j
https://recordsoflife.tistory.com/219
