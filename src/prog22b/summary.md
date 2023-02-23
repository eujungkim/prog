##### 콘솔 입력
```
Scanner scanner = new Scanner(System.in);
while (scanner.hasNextLine()) {
    String in = scanner.nextLine();
    // do something      
}
```
##### synchronized list
```
List<String> resultList = Collections.synchronizedList(new ArrayList<>());
```
##### HttpClient get
```
HttpClient httpClient = new HttpClient();
httpClient.start();
ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/queueInfo")
  .method(HttpMethod.GET).send();
String jsonInput = contentRes.getContentAsString();

Gson gson = new Gson();
ControllerInput input = gson.fromJson(jsonInput, ControllerInput.class);
```
##### HttpClient post
```
HttpClient httpClient = new HttpClient();
httpClient.start();
Gson gson = new Gson();
String content = gson.toJson(new OutputQueueData(resultList));
httpClient.newRequest(outputQueueURI).method(HttpMethod.POST)
    .content(new StringContentProvider(content)).send();
```
##### Thread start
```
Thread timeChecker = new Thread(new Runnable() {
  @Override
  public void run() {
    // do something    
  });
timeChecker.start();
```
##### Java Process
```
public final class JavaProcess {

    private JavaProcess() {}        

    public static int exec(Class klass, List<String> args) throws IOException,
                                               InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
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
##### scheduled executor service - fixed rate
`````
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
executorService.scheduleAtFixedRate(() -> {
    System.out.println(new Date() + " BACKUP " + processNo);
    // do something
}, 5, 5, TimeUnit.SECONDS); // 5초 후부터 5초 간격
`````
##### scheduled executor service - fixed delay
```
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
executorService.scheduleWithFixedDelay(() -> {
    try {
        JavaProcess.exec(MyProcess.class, ins);
    } catch (Exception e) {
        e.printStackTrace();
    }
}, 0, 10, TimeUnit.MILLISECONDS); // 바로 시작, 10ms 이후 다시 시작(1개만 실행됨)
```
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
##### directory 생성
```
new File("backup").deleteOnExit();
new File("backup").mkdir();
```
