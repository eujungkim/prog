# Cloud
- Cloud
  - AWS Document(https://docs.aws.amazon.com)
  - GCP Document(https://docs.microsoft.com/en-us/learn/, https://www.microsoft.com/handsonlabs)
  - Azure Document(https://cloud.google.com/docs/)

- AWS Document
  - Compute > EC2, EC2 Image Builder, Lambda
  - Containers > ECR, ECS, EKS, App2Container
  - Networking & Content Delivery > API Gateway, VPC, Route3
  - Storage > S3, EBS, EFS, S3 Glacier
  - Security, Identity, & Compliance > IAM
  - Management & Governance > CloudFormation, CloudWatch, AppConfig, Auto Scaling
  
- GCP Document
  - 컴퓨팅 > Compute Engine, 선점형 선점형 VM, 보안 VM,
  - 컨테이너 > GKE, Container Registry,
  - 네트워킹 > VPC, Cloud Router, Cloud NAT, Cloud Load Balancing
  - 보안 및 ID > IAM, Resource Manager, 보안 VM, VPC 서비스 제어 , Cloud Key Management Service
  - 서버리스 서버리스 컴퓨팅 컴퓨팅 > Cloud Functions, Cloud Run
  - 관리도구 관리도구 > Cloud Console, Cloud Shell, 비용 관리 , Cloud APIs
  
- Azure Document
  - Azure Fundamentals : Describe core Azure concepts
  - Azure Fundamentals : Describe core Azure services
  - Azure Fundamentals : Describe core solutions and management tools on Azure
  - Azure Fundamentals : Describe general security and network security features
  - Azure Fundamentals : Describe identity, governance, privacy, and compliance features
  - Azure Fundamentals : Describe Azure cost management and service level agreements

# AI
- AI
  - AI (Artificial Intelligence) : 사람이 해야 할 일을 기계가 대신할 수 있는 모든 자동화에 해당
  - Machine Learning : 명시적으로 규칙을 프로그래밍하지 않고 데이터로부터 의사결정을 위한 패턴을 기계가 스스로 학습
  - Deep Learning : 인공신경망 기반의 모델로, 비정형 데이터로부터 특징 추출 및 판단까지 기계가 한번에 수행

- AI와 머신러닝, 딥러닝 구분 기준
  - 특징 추출, 판단 방식 (사람이 하느냐 기계가 하느냐)

- 머신러닝
  - 정형 데이터(structured data) - 사람이 정제하고 정리한 데이터
  - 빅데이터 분석 기법
  - R, SAS, SPSS 등의 통계분석 툴 활용
  - 선형/로지틱스 회귀분석, 의사 결정 나무, ARMA/ARIMA 모형 등
- 딥러닝
  - 비정형 데이터(unstructured data)
  - raw data에 가까운 텍스트, 음성, 이미지, 동영상
  
- 모라벡의 역설 : 사람에게 쉬운 것이 기계에게는 어렵다. 기계에게 쉬운 것은 사람에게 어렵다.

- 인공신경망(Artificial Neural Network)

- Convolutional Neural Network(CNN)
  - 이미지 처리에 특화된 신경망
  - 특징 추출(Feature Extraction) : 이미지로부터 특징을 추출
    - 컨볼루션(Convolution) 연산 : 특징 추출
	- 풀링(Pooling) 연산 수행 : 핵심정보 남기기
  - 태스크 수행
    - Classification, Detection, Segmentation
  
- 언어  
  - 자연어처리(NLP; Natural Language Processing)
  - 자연어이해(NLU; Natural Language Understanding)   
  - 다차원의 실수 데이터 :  텐서(Tensor)
  - Tokenizing(Parsing)
  - 워드임베딩(word embedding) : 토큰을 벡터화
    - 원-핫 인코딩(One-hot Encoding)
	- CBOW(빈 칸에 알맞은 말)와 SKIPGRAM(문장 내에서의 문맥으로 추측)
	
- 시간 흐름에 따른 데이터(Sequential data) 처리하기
  - Recurrent Neural Network(RNN; 순환 신경망)
    - 장점 : 시간 흐름에 따른 과거 정보를 누적, 가변 길이의 데이터 처리 가능, 다양한 구성의 모델
	- 단점 : 연산 속도 느림, 학습 불안정, 과거 정보를 잘 활용할 수 있는 모델이 아님
  - LSTM(Long-short term memory)
    - forget gate,  input gate, output gate
	
- AI Process
  - Offline Process(Training Pipeline, 학습) & Online Process(Inference Pipeline, 추론) 
  - Training, Validation, Test set을 8:1:1, 6:2:2 정도로 구분
  - Regularization (오버피팅을 피하기 위한 모든 전략- 어느 순간 학습이 아닌 암기)
    - 데이터 증강(Data Augmentation)
	- Capacity(모델 복잡도) 줄이기
	- 조기 종료(Early stopping)
	- 드롭아웃(Dropout) : 일정 비율의 인공 뉴런은 끄고 진행
	
- Transfer LearningTransfer Learning : 한 번 만들어진 딥러닝 모델을 재활용
  - 레이어 동결(Layer freezing) : 기본 특징 학습은 건너 뛰고 바로 태스크를 위한 학습 진행
  - Discriminative fine-tuning : 층마다 Learning rate(학습률)의 차별을 둠
  
- Pre-trained AI
  - Self-Supervised Learning : 기계가 시스템적으로 자체 라벨을 만들어서 사용하는 학습 방법
  - Google BERT(Bidirectional Encoder Representations from Transformers)
  
- Active Learning (효과적인 학습을 위한 시스템)
  - 라벨링을 할 수 있는 인적 자원은 있지만, 많은 수의 라벨링을 수행할 수 없을 때 효과적으로 라벨링을 하기 위한 기법
  - 1. Training a Model : 초기 학습 데이터(labeled data)를 이용해 모델을 학습합니다.
  - 2. Select Query : 라벨이 되지 않은 데이터 풀로부터 모델에게 도움이 되는 데이터를 선별합니다.
    - Uncertainty Sampling : AI 모델은 가장 불확실하다(least certain)고 생각하는 데이터를 추출하여 라벨링이 필요하다고 요청
	- Query by committee : 여러 AI 모델간의 의견불일치를 종합 고려하는 방식
  - 3. Human Labeling : 선별한 데이터를 사람이 확인하여 라벨을 태깅합니다.
  - 4. 선별한 라벨 데이터를 기존 학습 데이터와 병합한 후, 다시 모델을 학습합니다.
  
- 어텐션 메커니즘(Attention mechanism)  
  - 인공신경망이 입력 데이터의 전체 또는 일부를 되짚어 살펴보면서 어떤 부분이 의사결정에 중요한지, 중요한 부분에 집중하는 방식
  - 어텐션 스코어(Attention score), 컨텍스트 벡터(Context vector)
  - XAI로서의 어텐션
    - 어텐션 메커니즘은 기계가 판단시 중요하게 생각하는 부분을 알려줌
	- 설명가능한 인공지능(eXplainable AI;XAI), 해석가능한 인공지능(interpretable AI)
  - 트랜스포머(Transformer)라는 인공신경망은 입력 데이터끼리의 self-attention을 통해 상호 정보교환을 수행하는 것이 특징
  
- AutoML(Automated Machine Learning) : 자동화된 기계학습
  - 하이퍼파라미터 탐색 자동화 : 그리드 서치(Grid search)와 랜덤 서치(Random search) 
  - 아키텍처 탐색 자동화
  - GCP AutoML
    - 시각 : AutoML Vision, AutoML Video Intelligence
	  - 언어 : AutoML Natural Language, AutoML Translation
	  - 구조화된 데이터 : AutoML Tables
	
- 설명 가능한 인공지능, XAI(eXplainable Artificial Intelligence)
  - 어텐션 메커니즘(Attention Mechanism)을 활용한 XAI
  - 설명하는 법 학습하기(Learn to explain)

# Library
- Http Server : Jetty 9 Embedded (https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-helloworld)
- Http Client : Jetty 9 HttpClient (https://www.eclipse.org/jetty/documentation/jetty-9/index.html#http-client)
- Json : Google Gson 2.8.6 (https://github.com/google/gson)
