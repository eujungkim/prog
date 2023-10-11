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
- https://www.baeldung.com/java-blocking-queue

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
