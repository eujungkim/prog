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
