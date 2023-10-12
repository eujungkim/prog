### MSA Outer
#### API Gateway
모든 API 요청의 단일 접점으로 공통처리, Mediation, Aggregation 기능을 지원
- 도입 목적 : API 일원화, 공통 기능 처리(프론트와 백엔드 매핑, 로깅 처리, BFF 패턴 구현), 요청 변환
- 설계시 고려 사항 : 인증/인가(API 토큰 구현), 레거시 솔루션 연계, 유입량 제어
- 최근 추세 : Containerized (PaaS 내에 구축하여 SPOF 방지)
#### Service Mesh
확장성을 지원하는 컨테이너 간 서비스를 손쉽게 discovery 할 수 있도록 지원하는 영역. 
서비스 전달 비율을 조율하여 다양한 배포 방식와 장애를 차단하는 기능을 지원
- 도입 목적 : 서비스 탐색, 장애 전파 차단, 배포 전략(서비스에 대한 유입량 조절을 통해 A/B, canary 구현)
- 설계시 고려 사항 : 서비스 디스커버리, Lock in, Metric 수집 기능(중복 적재 유의)
- 최근 추세 : 고유 기능의 확장 (API Gateway와의 기능 중복에 의한 역할 분리)
#### Container Management
오케스트레이션을 지원하며 서비스의 라이프사이클을 관리하는 영역
- 도입 목적 : 오케스트레이션, **라이프사이클 관리**, 리소스 모니터링
- 설계시 고려 사항 : Hybrid Cloud, Cluster 배치, Echo System 지원(Telemetry, Service Mesh, CI/CD 기능 제공 범위 검증)
- 최근 추세 : 플랫폼의 안정성 확대(성능과 안정성)
#### Backing Service
데이터를 저장하거나 서비스에 종속되지 않은 데이터에 대해 안정적으로 복제하는 환경을 제공하는 영역으로 비동기/캐시 설계, 데이터 동기화 방식을 설계
- 도입 목정 : 약결합 구조, 분산 데이터 처리(데이터 순서 보장, 데이터 복제 및 동기화), 탄력적인 확장
- 설계 시 고려사항 : 비동기/캐시 선정, 데이터베이스 타입, 데이터 동기화 설계
- 최근 추세 : 비지니스 관려 (프론트엔드와 직접 연결되는 구조, Kafka/Redis API를 활용하여 비지니스 코드 확장)
#### CI/CD Automation
서비스 출시를 앞당길 수 있는 핵심 영역으로 고객의 pain point가 가장 많이 도출되는 영역
- 도입 목적 : 신속한 출시(소스코드 품질 관리, 테스트 자동화를 포함한 자동화 구축), 소스코드 품질 향상, 테스트 자동화
- 설계시 고려사항 : 레거시 솔루션 연계, 긴급 배포/롤백, 빌드/배포 시간 단축(사전 점검 방식 구현)
- 최근 추세 : 멀티 형상관리 연계 (Enterprise향 형상관리)
#### Telemetry
운영 안정성 측면에서 서비스의 흐름을 추적하고 통합하는 영역으로, 수집된 데이터를 어떻게 활용할 것인가에서 접근 필요


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
