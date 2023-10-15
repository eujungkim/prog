# tech
## java
### RandomAccessFile
- 입출력 클래스 중 유일하게 파일에 대한 입력과 출력(읽기와 쓰기)을 동시에 할 수 있는 클래스이다.
- 파일 포인터가 있어서 읽고 쓰는 위치의 이동이 가능하다. (seek 메소드를 활용)
- 파일의 내용에 대해 랜덤 액세스를 허용한다. 즉 읽거나 쓰는 위치를 임의로 변경할수있다.
- 파일을 액세스하기 위해 파일을 오픈하고, 특정 위치로 파일 포인터를 옮기고, 읽기 또는 쓰기를 수행한다.
- 파일 포인터에서 시작하여 바이트 단위로 읽기 또는 쓰기를 수행할수있다.
- 읽거나 쓴 바이트 수만큼 파일 포인터가 이동한다.
- 읽기 모드 또는 읽기/쓰기 모드로 생성할 수 있다.

### WatchService
- WatchService 인터페이스는 특정 디렉토리를 감시하고 변경 이벤트 발생시, 지정한 액션을 수행하도록 도와준다.
- callback 메소드를 제공하지 않으며 blocking, non-blocking 방식의 polling 메소드를 제공함
- 구현 순서
  - FileSystems의 WatchService 생성
  - Paths에 모니터링 대상인 디렉토리 경로 설정
  - 대상 Paths에 생성한  WatchService와 감지 대상 Events 등록
  - WatchKey로 WatchService에 이벤트가 오면, 원하는 액션 취하기
- 구현 주의사항
  - WatchKey는 일회성이므로 사용 후 반드시 reset()을 해줘야 다음 이벤트를 받을 수 있다.
  - 한 번에 여러 개의 이벤트가 발생할 수 있다.
  - 감지 경로는 디렉토리로 지정해야 하며, 파일을 지정할 수 없다.
  - 무한 루프 주의 (이벤트가 감지 범위 내의 파일을 수정하지 않도록 주의)
  - 디렉토리 내 하위 디렉토리의 변경 사항도 감지한다.
- 작성 예 : https://www.baeldung.com/java-nio2-watchservice
- 유사 기술 : Apache Commons IO Monitor Library
  - https://www.baeldung.com/java-watchservice-vs-apache-commons-io-monitor-library
  - WatchService는 OS에 의해 trigger 되므로 파일 시스템에 대한 polling을 수행하지 않음 vs Commons IO는 polling을 수행하므로 CPU 사용
  - WatchService는 OS event driven이므로 빨리 처리하지 않을 경우 event overflow 발생 가능성, Commons는 OS event를 사용하지 않으므로 무관

### ExecutorService, ScheduledExecutorService
- 스레드풀과 작업 할당을 위한 API 제공
- ExecutorService의 가장 좋은 사용 사례는 "하나의 작업에 하나의 스레드"라는 체계에 따라 트랜잭션이나 요청과 같은 독립적인 작업을 처리하는 것이다.
- ThreadPoll
```
// fixed pool
ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
for (int i = 0; i < 10; i++) {
  fixedThreadPool.execute(new Thread(new RandomWriter("output3.txt")));
}
fixedThreadPool.shutdown();
```
- ScheduledExecutorService
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
- 주의사항 : 적절한 스레드 풀 사이즈 설정
  - 애플리케이션이 작업을 효율적으로 실행하는 데 필요한 스레드 수를 결정하는 것은 매우 중요하다.
  - 스레드 풀이 너무 크면 대부분 대기 모드에 있는 스레드를 생성하는 데 불필요한 오버헤드가 발생한다.
  - 너무 적으면 queue에 있는 작업에 대한 긴 대기 시간으로 인해 응용 프로그램이 응답하지 않는 것처럼 보일 수 있다.

### java.util.concurrent.BlockingQueue
- 동시 생산자, 소비자 문제 해결
- Multithreaded Producre-Comsumer 구현
- Unbounded Queue : capacity - Integer.MAX_VALUE
  - Unbounded BlockingQueue를 사용하여 생산자-소비자 프로그램을 설계할 때 가장 중요한 점은 생산자가 대기열에 메시지를 추가하는 것처럼 소비자가 메시지를 빠르게 소비할 수 있어야 한다는 것이다. 그렇지 않으면 메모리가 가득 차서 OutOfMemory 예외가 발생하게 된다.
```
BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
```
- Bounded Queue
  - 제한된 대기열을 사용하는 것은 동시 프로그램 설계의 좋은 방법이다.
  - 이미 가득찬 대기열에 요소를 삽입하기 위해서는 소비자가 따라잡아서 대기열의 일부 공간을 사용할 수 있을 때까지 기다린다.
```
BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(10);
```  
- 메소드
  - add() – 삽입에 성공하면 true를 반환하고, 그렇지 않으면 IllegalStateException을 발생시킨다.
  - put() - 지정된 요소를 대기열에 삽입하고 필요한 경우 여유 슬롯을 기다린다.
  - offer() – 삽입이 성공하면 true를 반환하고, 그렇지 않으면 false를 반환한다.
  - offer(E e, long timeout, TimeUnit 단위) – 요소를 대기열에 삽입하려고 시도하고 지정된 시간 초과 내에 사용 가능한 슬롯을 기다린다.
  - take() – 대기열의 헤드 요소를 기다렸다가 제거합니다. 큐가 비어 있으면 요소가 사용 가능해질 때까지 차단하고 기다린다.
  - poll(long timeout, TimeUnit 단위) – 요소를 사용할 수 있게 되는 데 필요한 경우 지정된 대기 시간까지 대기하면서 대기열의 헤드를 검색하고 제거한다. 시간 초과 후 null을 반환한다.
- 종류
  - ArrayBlockingQueue : 초기에 크기 지정, 생성 후 크기 변경 불가, FIFO
  - LinkedBlockingQeueu : capacity를 초기에 정해주지 않는경우 integer.MAX_VALUE로 자동설정, FIFO
  - PriorityBlockingQueue : PriorityQueue와 같은 정렬방식을 지니는 용량제한 없는 Queue, OOM 주의
  - SynchronousQueue : Item 저장 공간이 없기 때문에 insert 작업이 다른 스레드의 remove 작업과 반드시 동시에 발생
- https://www.baeldung.com/java-blocking-queue

### concurrent collection
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
// Map
Map<String, List<Message>> queueMap = new ConcurrentHashMap<>();
```

### Process vs Thread
#### Process
- 의미
  - 컴퓨터에서 연속적으로 실행되고 있는 컴퓨터 프로그램
  - 메모리에 올라와 실행되고 있는 프로그램의 인스턴스(독립적인 개체)
  - 운영체제로부터 시스템 자원을 할당받는 작업의 단위
- 특징
  - 프로세스는 각각 독립된 메모리 영역(Code, Data, Stack, Heap의 구조)을 할당받는다.
  - 기본적으로 프로세스당 최소 1개의 스레드(메인 스레드)를 가지고 있다.
  - 각 프로세스는 별도의 주소 공간에서 실행되며, 한 프로세스는 다른 프로세스의 변수나 자료구조에 접근할 수 없다.
  - 한 프로세스가 다른 프로세스의 자원에 접근하려면 프로세스 간의 통신(IPC, inter-process communication)을 사용해야 한다. 예) 파이프, 파일, 소켓 등을 이용한 통신 방법 이용
#### Thread
- 의미
  - 프로세스 내에서 실행되는 여러 흐름의 단위
  - 프로세스의 특정한 수행 경로
  - 프로세스가 할당받은 자원을 이용하는 실행의 단위
- 특징
  - 스레드는 프로세스 내에서 각각 Stack만 따로 할당받고 Code, Data, Heap 영역은 공유한다.
  - 스레드는 한 프로세스 내에서 동작되는 여러 실행의 흐름으로, 프로세스 내의 주소 공간이나 자원들(힙 공간 등)을 같은 프로세스 내에 스레드끼리 공유하면서 실행된다.
  - 같은 프로세스 안에 있는 여러 스레드들은 같은 힙 공간을 공유한다. 반면에 프로세스는 다른 프로세스의 메모리에 직접 접근할 수 없다.
  - 각각의 스레드는 별도의 레지스터와 스택을 갖고 있지만, 힙 메모리는 서로 읽고 쓸 수 있다.
  - 한 스레드가 프로세스 자원을 변경하면, 다른 이웃 스레드(sibling thread)도 그 변경 결과를 즉시 볼 수 있다.
- 장점
  - 스레드를 사용하면 동시에 여러 작업을 수행하여 프로그램이 보다 효율적으로 작동할 수 있다.
  - 스레드를 사용하면 기본 프로그램을 중단하지 않고 백그라운드에서 복잡한 작업을 수행할 수 있다.
- 장단점
  - 프로그램 수행이 계속되는 것을 허용함으로써 사용자에 대한 응답성을 증가시킨다
  - Process 내 Thread 간 메모리 공유 (손쉬운 자원 공유)
  - Process 생성에 비해 경제적
  - Thread 간 영향으로 인한 안정성 저하, 동기화 이슈, 결과 예측 및 디버깅이 어렵다

### ProcessBuilder
- ProcessBuilder 클래스는 운영 체제 프로세스를 만들고 구성하는 메서드를 제공한다.
- 각 ProcessBuilder 인스턴스를 사용하면 프로세스 속성 모음을 관리할 수 있다. 그런 다음 해당 속성을 사용하여 새 프로세스를 시작할 수 있다.

### synchronization (동기화)
다중 thread 환경에서 공통된 자원에 접근하고자 할 때 발생하는 순서와 동시성을 배제하기 위한 목적
```
// Lock 사용
ReentrantLock lock = new ReentrantLock();
lock.lock()
try {
  ...
} finally {
  lock.unlock()
}
// synchronized 키워드 사용
```

### data 구조
Queue(FIFO), List, Map, LinkedList 등의 자료구조를 성능과 용도를 고려하여 적절히 사용
- LinkedHashMap : 저장되는 순서 유지
- SortedHashMap : Key로 정렬하여 저장
- ConcurrentHashMap : Thread-safe하게 사용

### serialization, deserialization (직렬화, 역직렬화)
Java 직렬화는 Java 객체를 바이트 스트림으로 변환하고, 이 바이트 스트림을 다른 Java 객체로 복원할 수 있는 기능이다.
직렬화된 객체는 네트워크를 통해 전송하거나 파일에 저장하거나 데이터베이스에 저장할 수 있다. 이를 통해 애플리케이션 간에 객체를 교환할 수 있다.
단점 및 주의사항
- 객체의 모든 상태를 보존하기 때문에 객체의 크기가 커질 수 있으므로 크기를 고려해야 한다.
- 객체의 모든 상태를 저장하므로 성능이 저하될 수 있다.
- Java 직렬화를 사용하여 악성코드를 전파할 수 있으므로 보안 위험을 고려해야 한다.
관련 Stream
- ObjectInputStream, ObjectOutputStream, FileInputStream, FileOutputStream

### class loader
Java ClassLoader는 Java 애플리케이션의 클래스를 실행 중에 로드하고 관리하는 역할을 한다. ClassLoader는 Java 애플리케이션의 일부이며, Java Runtime Environment (JRE)에 포함되어 있다.
- ClassLoader 특징
  - 클래스를 로드하고 관리한다.
  - 클래스 간 의존성을 관리한다.
  - 클래스의 메서드와 필드에 대한 접근을 제어한다.
- ClassLoader 종류
  - Bootstrap Classloader: Java Runtime Environment (JRE)에 포함된 기본 클래스를 로드한다.
  - Extension Classloader: Java Runtime Environment (JRE)의 확장 디렉터리에서 클래스를 로드한다.
  - System Classloader: 애플리케이션의 클래스패스에서 클래스를 로드한다.
  - Application Classloader: 애플리케이션이 정의한 클래스를 로드한다.
- ClassLoader 용도
  - 애플리케이션의 확장성을 높입니다. : 어플리케이션 실행 중에 동적으로 클래스를 로드할 수 있다. 새로운 기능을 추가하거나 버그 수정이 쉽다.
  - 애플리케이션의 보안을 강화합니다.  : 특정 클래스만 로드하도록 제한할 수 있다.
  - 애플리케이션의 유연성을 높인다. : 유연한 제어가 가능하여 다양한 환경에서 사용 가능
- ClassLoader 단점
  - 클래스 로그에 시간이 걸려 성능 저하 가능성
  - 오사용시 악성 코드가 애플리케이션에 침힘하여 보안 문제 발생 가능성

### reflection
Java reflection은 Java 런타임 중에 클래스와 객체의 메타데이터에 접근하고 조작할 수 있는 기능이다. 메타데이터는 클래스와 객체에 대한 정보로, 클래스 이름, 생성자, 메서드, 필드 등의 정보를 포함한다.
- 특징
  - 클래스와 객체의 메타데이터에 접근할 수 있다.
  - 클래스와 객체를 동적으로 생성하고 조작할 수 있다.
  - 프록시를 사용하여 클래스와 객체를 감시하거나 대체할 수 있다.
- 용도
  - 클래스와 객체에 대한 정보를 얻을 때 사용한다.
  - 클래스와 객체를 동적으로 생성하고 조작할 때 사용한다.
  - 프록시를 사용하여 클래스와 객체를 감시하거나 대체할 때 사용한다.
- 장점
  - 클래스와 객체를 동적으로 생성하고 조작할 수 있어 유연성이 높다.
  - 프록시를 사용하여 클래스와 객체를 감시하거나 대체할 수 있어 확장성이 높다.
  - 코드의 재사용성을 높일 수 있다.
- 단점
  - 성능이 저하될 수 있다. 성능이 중요한 어플리케이션에서는 사용을 자제
  - 보안 위험이 있을 수 있다.
  - 코드가 복잡해질 수 있다. 코드의 가독성과 유지보수성 고려
  - 오사용하면 오류가 발생할 있으므로 사용 방법을 숙지해야 한다.

### socket
- TCP	: 송신지에서 수신지까지 정확하고 신뢰성 있게 정보 흐름을 제어하여 데이터를 전송
- UDP	: 패킷 전달 보장을 위한 오버헤드를 감수하는 대신 좀 더 빠르게 세그먼트를 교환할 수 있는 프로토콜

### http
클라이언트의 요청 request가 있을 때 서버가 응답 response하여 해당 정보를 전송하고 연결을 종료하는 방식

### servlet
서블릿은 HTTP 프로토콜을 사용하며 웹 브라우저에서 보내는 요청을 처리하고 응답을 보내는 데 사용된다.
서블릿은 서블릿 컨테이너에서 실행된다. 서블릿 컨테이너는 서블릿을 관리하고 서블릿이 웹 브라우저와 통신할 수 있도록 지원하는 역할을 합니다. 서블릿 컨테이너는 Tomcat, Jetty, GlassFish 등이 있다.
- 서블릿의 용도
  - 동적 웹 페이지를 생성한다. 서블릿을 사용하여 웹 브라우저에 표시할 HTML, CSS, JavaScript를 생성할 수 있다.
  - 데이터베이스에 액세스하고 데이터를 처리한다. 서블릿을 사용하여 데이터베이스에 데이터를 추가, 수정, 삭제할 수 있다.
  - 파일을 업로드하고 다운로드한다. 서블릿을 사용하여 웹 브라우저에서 파일을 업로드하거나 다운로드할 수 있다.
  - 쿠키와 세션을 관리한다. 서블릿을 사용하여 웹 브라우저에 쿠키를 저장하거나 세션을 관리할 수 있다.
  - 보안 기능을 구현한다. 서블릿을 사용하여 웹 애플리케이션에 보안 기능을 구현할 수 있다.

### 기타
- Encoding : 사용성을 위해 데이터의 형태나 형식을 변환. 동일한 알고리즘으로 Decoding 가능
- Encryption : 데이터의 기밀성 유지를 위해 사용하며, decryption을 위해서는 키를 사용해야 함
- Hashing	: 임의의 길이의 데이터를 고정된 길이의 데이터로 매핑. 해시 값에서 원래 입력값과의 관계를 찾기 어려움
- Obfuscation	: 코드 난독화 (리버스 엔지니어링에 의한 소스 코드 유출 방지)

### java agent
Java agent는 Java 애플리케이션에 로드되어 애플리케이션의 동작을 제어하거나 정보를 수집하는 프로그램이다. Java agent는 Java Instrumentation API를 사용하여 Java 애플리케이션의 바이트 코드를 수정하거나 정보를 수집할 수 있다.
- 용도
  - 애플리케이션의 성능을 측정하거나 향상시키는 데 사용
  - 애플리케이션의 보안을 강화하는 데 사용
  - 애플리케이션의 기능을 확장하는 데 사용
- 종류
  - Premain agent: JVM이 애플리케이션을 시작하기 전에 로드되는 agent
  - Agentmain agent: JVM이 애플리케이션을 시작한 후 로드되는 agent
  - Attach agent: 실행 중인 애플리케이션에 로드되는 agent
Java agent는 Java 애플리케이션의 성능, 보안, 기능을 향상시키는 데 유용한 도구입니다. 하지만 Java agent를 잘못 사용하면 애플리케이션의 안정성에 영향을 줄 수 있으므로 신중하게 사용해야 한다.
Java agent를 개발하려면 Java Instrumentation API를 숙지해야 한다. Java Instrumentation API는 Java SE 5부터 제공되는 API로, Java 애플리케이션의 바이트 코드를 수정하거나 정보를 수집할 수 있는 기능을 제공한다.

### pub sub 패턴
pub sub 패턴은 발행자(publisher)와 구독자(subscriber)로 구성된 소프트웨어 디자인 패턴입니다. 발행자는 특정 이벤트나 데이터를 발행하고, 구독자는 발행자가 발행한 이벤트나 데이터를 수신합니다.
- 장점
  - 발행자와 구독자 간의 의존 관계를 느슨하게 만듭니다. 발행자는 구독자를 알 필요가 없고, 구독자는 발행자를 알 필요가 없습니다.
  - 발행자와 구독자의 수를 동적으로 조절할 수 있습니다. 발행자와 구독자가 추가되거나 제거되더라도 기존의 발행자와 구독자는 영향을 받지 않습니다.
  - 발행자와 구독자가 분리되어 있어 확장성이 좋습니다. 발행자와 구독자를 독립적으로 확장할 수 있습니다.
- 용도
  - 실시간 데이터 전송
  - 시스템 모니터링
  - 이벤트 알림, 실시간 알림
  - 메시징
  - 데이터 수집
  - 이벤트 기반 아키텍처
- 구현 방안
  - 메시지 브로커 사용 : 메시지 브로커를 사용하는 방법은 가장 일반적인 구현 방법입니다. 메시지 브로커는 발행자와 구독자를 중개하는 역할을 합니다. 발행자는 메시지 브로커에 메시지를 발행하고, 구독자는 메시지 브로커에서 메시지를 수신합니다.
  - 직렬화된 데이터 사용 : 직렬화된 데이터를 사용하는 방법은 메시지 브로커를 사용하지 않고 구현할 수 있는 방법입니다. 발행자는 데이터를 직렬화하여 구독자에게 전송합니다. 구독자는 직렬화된 데이터를 역직렬화하여 사용합니다.
  - 데이터베이스 사용 : 데이터베이스를 사용하는 방법은 메시지 브로커를 사용하지 않고 구현할 수 있는 또 다른 방법입니다. 발행자는 데이터를 데이터베이스에 저장합니다. 구독자는 데이터베이스에서 데이터를 조회하여 사용합니다.
- 단점
  - 발행자와 구독자가 느슨하게 연결되어 있기 때문에 추적과 디버깅이 어렵거나, crach 발생시 시스템의 다른 부분에 영향을 주지 않는다는 것을 보장하기 어렵습니다.
- 관련 솔루션 : MQTT, Kafka

### async



---
# title1
## title2
### title3
line
- point

|제목|내용|설명|
|------|---|---|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
---
