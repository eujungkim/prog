### design pattern
- 어댑터 패턴(Adapter Pattern)
  - 호출당하는 쪽의 메소드를 호출하는 쪽의 코드에 대응하도록 중간에 변환기를 통해 호출하는 패턴
  - 다양한 DB시스템을 공통의 인터페이스인 JDBC를 이용해 조작
- 프록시 패턴(Proxy Pattern)
  - 제어 흐름을 조정하기 위한 목적으로 중간에 대리자를 두는 패턴
- 데코레이터 패턴(Decorator Pattern)
  - 메소드 호출의 반환값에 변화를 주기 위해 중간에 장식자를 두는 패턴
- 싱글톤 패턴(Singleton Pattern)
  - 클래스의 인스턴스, 즉 객체를 하나만 만들어 사용하는 패턴
  - 커넥션 풀, 스레드 풀, 디바이스 설정 객체등과 같은 경우 인스턴스를 여러 개 만들게 되면 불필요한 자원을 사용하게 되고, 또 프로그램이 예상하지 못한 결과를 낳을 수 있다.
- 템플릿 메소드 패턴(Template Method Pattern)
  - 상위 클래스의 견본 메소드에서 하위 클래스가 오버라이딩한 메소드를 호출하는 패턴
  - 상위 클래스에 공통 로직을 수행하는 템플릿 메소드와 하위 클래스에 오버라이딩을 강제하는 추상메소드 또는 선택적으로 오버라이딩할 수 있는 훅 메소드를 두는 패턴
- 팩토리 메소드 패턴(Factory Method Pattern)
  - 오버라이드된 메소드가 객체를 반환하는 패턴
  - 하위 클래스에서 팩토리 메소드를 오버라이딩해서 객체를 반환
- 전략 패턴(Strategy Pattern)
  - 클라이언트가 전략을 생성해 전략을 실행할 컨텍스트에 주입하는 패턴
- 옵저버 패턴(Observer Pattern)
  - 변화가 일어났을 때, 미리 등록된 다른 클래스에 통보해주는 패턴을 구현한 것. event listner가 대표적인 사용 예이다.
- 파사드 패턴(Facade Pattern)
  - 여러개의 객체와 실제 사용하는 서브 객체 사이에 복잡한 의존관계가 있을 때, 중간에 facade라는 객체를 두고, 여기서 제공하는 interface만을 활용하여 기능을 사용하는 방식

### 참고
Google Cloud Pub/Sub
https://cloud.google.com/pubsub/docs/overview?hl=ko

클라우드 디자인 패턴
https://learn.microsoft.com/ko-kr/azure/architecture/patterns/

확장 가능하고 복원력이 우수한 앱 패턴
https://cloud.google.com/architecture/scalable-and-resilient-apps?hl=ko

AWS Well-Architected Framework
https://docs.aws.amazon.com/ko_kr/wellarchitected/latest/framework/welcome.html

SW 아키텍처
http://www.jidum.com/jidums/view.do?jidumId=304

소프트웨어 디자인 패턴
https://ko.wikipedia.org/wiki/%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4_%EB%94%94%EC%9E%90%EC%9D%B8_%ED%8C%A8%ED%84%B4
