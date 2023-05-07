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
## 1편
- AI
  - 인공지능은 저절로 똑똑해질 수 없다. 인공지능의 90% 이상은 지도 학습(Supervised Learning) 방식으로 훈련된다.
  - 지도 학습 : 사람이 데이터 한 건 한건을 어떻게 판단하거나 처리하는지에 대한 정답을 인공지능에게 알려주면서 진행하는 방식
  - 강 인공지능 (Strong AI, General AI) : 인간의 명령 없이 스스로 판단 및 결정, 영화상의 인공지능, 범용성
  - 약 인공지능 (Weak AI, Narrow AI) : 일상생황에서 만다는 인공지능, 제한된 환경에서 구체적인 특정 업무 수행, 알파고
  - AI (Artificial Intelligence) : 사람이 해야 할 일을 기계가 대신할 수 있는 모든 자동화에 해당
  - Machine Learning : 명시적으로 규칙을 프로그래밍하지 않고 데이터로부터 의사결정을 위한 패턴을 기계가 스스로 학습
  - Deep Learning : 인공신경망 기반의 모델로, 비정형 데이터로부터 특징 추출 및 판단까지 기계가 한번에 수행
  - AI > Machine Learning > Deep Learning
  - 자사에서 말하는 AI = 딥러닝 기반의 인공지능

- 기계자동화(규칙 기반의 AI)와 머신러닝, 딥러닝 구분 기준
  - 두 가지를 사람이 하느냐, 기계에게 맡기느냐
    - 특징 추출 : 문제 해결을 위해 어떤 정보가 유용할지, 중요한 것을 꺼내는 과정
    - 판단 방식 : 모델을 만드는 단계
  - 데이터/현상 > (특징 추출) > 특징 > (판단) > 인식 및 예측
    - 규칙 기반 프로그래밍 : 사람이 특징 추출 및 판단
    - 전통적인 머신러닝 : 문제해결에 필요한 주 특징만 사람이 결정, 판단 모델은 수학적 모델링과 기계학습을 통해 구현 (회귀 분석, 의사결정나무)
    - 딥러닝 : Raw Data로부터 수학적 모델링과 인공신경망 학습을 통해 모든 것을 구현하는 방식, 이미지/텍스트 데이터를 그대로 처리 (CNN, RNN)
## 2편
- 딥러닝 : 기계가 **비정형** 데이터를 입력받은 후 데이터의 주요한 특징을 알아서 추출하고 이를 바탕으로 의사결정을 하는 기술
- 정형 데이터 (structured data)
  - 전통적인 머신러닝 기반의 기술들로 다룰 수 있는 데이터. DB, 엑셀, CSV 파일 등 사람이 정제하고 정리한 데이터
  - 머신러닝은 정형데이터를 다루는데 특화되어 있음. 빅데이터 분석 기법으로 R, SAS, SPSS 같은 통계분석 툴 활용
  - 실수 값 예측에 쓰이는 선형/로지스틱 회기분석, 카테고리 분류에 쓰이는 의사 결정 나무, 시계열 예측에 쓰이는 ARMA, ARIMA 모형 등
- 비정형 데이터 (unstructured data)
  - 딥러닝 기반의 AI가 다루는 데이터. 정리되지 않은 다양한 형식의 raw data에 가까운 데이터
  - 텍스트 데이터 예 : 웹 페이지, 상품 리뷰, SNS 글, 기업용 문서, 뉴스기사
- 인공신경망
  - 사전적 정의가 아닌 특징을 배우는 사람의 학습방식을 기계학습에 녹인 것이 딥러닝
  - 인공뉴런 : 이전의 뉴런이 넘겨준 데이터를 가중합 연산을 한 뒤, 비선형함수를 적용하여 정보를 가공하여 다음 인공 뉴런으로 데이터를 넘김
  - 딥러닝 모델 : 데이터를 통해 자동으로 필요한 특징을 찾아내고 분류를 수행한다
  - 인공 뉴런을 다양한 방식으로 여러 층으로 쌓아 연결한 것이 딥러닝의 기본 구조인 인공신경망이다.
## 3편
이미지 
- 인공신경망은 가중합와 비선형 함수로 이루어진 연산을 수행해야 하므로 입력 데이터로 벡터나 행렬 형태를 필요로 한다.
- 흑백 이미지 - 2차원 행렬(matrix), 컬러 이미지 - 3차원 텐서(tensor)
- Convolutional Neural Network (CNN)
  - 이미지 처리에 특화된 신경망
  - 이미지로부터 특징을 추출하는 **Feature Extraction** 영역과 특정 태스크 수행을 위해 덧붙이는 **태스크 수행 영역**으로 구성됨
- 특징 추출 (Feature Extraction)
  - 컨볼루션 연산과 풀링 연산 수행, Feature Learning이라고 부름
  - 컨볼루션 연산 : 컨볼루션 필터(또는 커널)가 입력 이미지를 상하좌우로 훑으면서 주요한 특징을 찾아내는 과정. 
    - 컨볼루션 필터를 여러 개 설정하면 이미지로부터 다양한 특징을 찾을 수 있음
    - 찾아낸 결과 특징을 Feature map (또는 convolved feature)라고 부른다.
    - 가중합 + 비선형 함수 적용으로 구성된다.
  - 폴링 연산
    - 컨볼루션 필터를 통해 찾은 특징으로부터 정보를 추리는 풀링 연산
    - Feature map을 상하좌우로 훑으면서 핵심적인 정보만 영역별로 샘플링
    - 주로 영역 내 가장 큰 값만을 남기고 나머지는 버리는 MaxPooling 방식을 적용
- 태스크 수행
  - 찾아낸 주요 특징 정보를 활용하여 목표로 하는 태스크 수행
  - 다음은 이미지 데이터를 처리하는 대표적인 태스크
  - Classification : 입력받은 이미지를 지정된 K개의 클래스(또는 카테고리) 중 하나로 분류 (강아지/고양이, 양호/불량)
  - Detection : 입력받은 이미지에서 특정 개체의 위치 좌표값(x,y)을 찾는다
  - Segmentation : 픽셀 단위로 영역 구분 (도로 위의 다양한 객체 영역 구별)
## 4편
언어 처리
- 자연어이해(NLU, Natual Language Understanding), 자연어처리(NLP, Natural Language Processing)
- Tokenizing(Parsing) : 문장을 세부 단위로 쪼개는 작업. 언어/태스크/데이터 특징에 따라 쪼개는 단위는 달라짐.
- 워드임베딩(word embedding) : 쪼개진 토큰을 벡터화하는 것
  - 원-핫 인코딩(One-hot Encoding) : 간단하지만 토큰수만큼의 길이를 갖는 벡터가 필요, 토큰 간의 연관관계 표현 불가
  - CBOW : 인공지능에게 문장을 알려주되 중간중간 빈칸을 만들어 들어갈 단어를 유추시킨다.
  - Skip-gram : 인공지능에게 단어(토큰) 하나를 알려주고, 주변에 등장할 그럴싸한 문맥을 만들도록 시킨다.
  - <img src="https://user-images.githubusercontent.com/7552395/236666234-589b2a41-e7af-49e6-87b4-f78c2ec1a542.png" width="50%" height="50%"></img>
  - CBOW, Skip-gram은 많은 단어를 학습시킬수록 더 좋은 품질의 벡터가 나온다.
  - 유사한 의미를 갖는 토큰은 유사한 벡터값을 가지도록 학습된다. 토큰끼리의 의미 연산(벡터 연산)이 가능하다.
  - 원-핫 인코딩에 비해 벡터의 길이가 훨씬 작아 저장공간을 효율적으로 사용한다.
- 대표적인 자연어이해 태스크
  - 문장/문서 분류(Sentence/Document Classification)
    - 입력받은 텍스트를 지정된 K개의 클래스(카테고리) 중 하나로 분류
    - 사용자 리뷰에 대한 감성분석(긍정/부정), 유저 발화문를 챗봇이 처리할 수 있는 기능 중 하나로 매핑하는 의도분류
  - Sequence-to-Sequence
    - 문장/문서를 입력받아 문장을 출력 : 번역, 요약, 자유대화
  - 질의 응답(Question Answering)
    - MRC(Machine Reading Comprehension) : 질문에 대해 매뉴얼 내에서 가장 답변이 될 가능성이 높은 영역 리턴
    - IR(Information Retrieval) : 질문에 대해 가장 유사한 과거 질문/답변(F&Q)를 
## 5편
시계열 데이터 처리
- Recurrent Neural Network (RNN, 순환 신경망)
  - <img src="https://user-images.githubusercontent.com/7552395/236670818-44e6acbd-4f78-4dc3-9cd5-5551827acaa7.png" width="50%" height="50%"></img>
  - 기존 인공신경망 : 입력값으로 출력값을 예측
  - RNN : 과거의 처리 이력을 압축하여 반영
- RNN 장점
  - 시간 흐름에 따른 과거 정보를 누적할 수 있다.
  - 가변 길이의 데이터를 처리할 수 있다. (시간 단위를 구성하기 나름)
  - 다양한 구성의 모델을 만들 수 있다. 구조가 유연. (one to one, one to many, many to one, many to many)
  - 인코딩-입력 데이터 정보를 누적하는 부분, 디코딩-결과를 출력하는 부분
- RNN 단점
  - 연산 속도가 느리다
    - 현 시점의 데이터를 처리하려면 이전 시점의 데이터 처리가 완료되어야 함. 순처처리므로 GPU 칩의 병렬처리 이점을 잘 활용할 수 없음.
    - 정형 데이터(수치, 범주형) 활용시 속도 저하를 체감하지 못하며, 텍스트 데이터 처리시 연산 속도 저하 문제 발생
  - 학습이 불안정하다
    - 다루는 데이터의 timestep이 길수록 문제 발생 확률 높음
    - Gradient Exploding : timestep이 길어지면 인공신경망이 학습해야 할 값이 폭발적으로 증가하는 현상
    - Gradient Vanishing : timestep이 길어지면 오래된 과거 이력이 현재 추론에 거의 영향을 미치지 못하는 문제
  - 과거 정보를 잘 활용할 수 있는 모델이 아니다
    - 장기 종속성/의존성 문제(Long-term dependency)
    - 한 timestep씩 정보를 누적하여 인코딩하므로, 먼 과거 정보는 여러 번 압축되고 누적되어 거의 영향을 미치지 못함
- RNN 성능 보완
  - LSTM(Long-short term memory)
    - 과거 정보 중 중요한 것은 기억하고, 불필요한 것은 잊어버리도록 스스로 조절 가능한 RNN 유닛
    - forget gate(불필요한 과거 정보 잊기)
    - input gate(현재 정보를 얼마나 반영할지 결정)
    - output gate(현재 시점에 연산된 최종 정보를 다음 시점에 얼마나 넘길지 결정)
    - GRU(Gated Recurrent Unit)도 유사. 기본 RNN은 거의 사용하지 않고 LSTM, GRU 사용
- 활용 사례 : 태양광 에너지 발전량 예측, 텍스트 문장 번역
## 6편
오버피팅(Overfitting), 정규화(Regularization)
- AI Process
- <img src="https://user-images.githubusercontent.com/7552395/236672821-56941e35-be75-4331-ac3e-00e402764f4c.png" width="50%" height="50%"></img>
  - Offline Process (Training Pipeline)
    - Historical data : 과거에 이미 만들어진 데이터, DB 등에 이미 수집된 데이터 
    - Generate Feature : 기 확보된 데이터를 정제하여 필요한 부분을 취한다
    - Collect labels : 라벨(AI 학습을 위해 필요한 정보, 인공지능이 맞춰야 하는 정답)을 붙인다
    - Validate & Select models : 성능 목표를 달성할 때까지
    - Train models : 반복 실험 진행
    - 튜닝 : 실험(Experimentation) 반복, 모델 학습을 위한 여러 설정 수치값(하이퍼파라미터) 조절 등
    - Publish model : 개선된 성능의 모델을 최종 선택
  - Online Process (Inference Pipeline)
    - Load model : AI 모델을 운영환경에 적용
    - 실전 환경에서 추론(inference)
- Generalization : 일반화 성능, 이전에 본적 없는 데이터에 대해서도 잘 수행하는 능력
- Overfitting : 훈련시에만 잘 작동하고 일반화 성능이 떨어지는 모델
- Dataset : Training, Validation, Test (8:1:1, 6:2:2)
  - Training set : 머신러닝, 딥러닝 모델을 학습하는데 이용하는 데이터. 정답을 알려줄 데이터.
  - Validation set : 모델을 튜닝하는데 도움을 주는 데이터. 모델의 일반화 성능을 판단하여 이어질 시험을 계획하는데 이용
  - Test set : 학습에 관여하지 않고, 모델의 최종 성능을 평가하기 위한 데이터
- 오버피팅 : Training set에 대해서만 성능이 개선되고, Validation set에 대해서는 별다른 향상이 없는 시점이 오버피팅 발생 시점
- Regularization : 오버피팅을 피하기 위한 모든 전략
  - 데이터 증강(Data Augmentation) : 데이터 변형을 통해 데이터 건수가 많아지는 효과, 과도한 변형은 오히려 해
  - Capacity 줄이기
    - Capacity : 모델의 복잡한 정도를 나타냄. 신경망이 여러층이거나 뉴런 수가 많을수록 높아진다.
    - Capacity가 필요 이상으로 너무 높은 모델은 주어진 데이터를 외우될 가능성이 높음
    - 수행하려는 태스크에 알맞는 Capacity를 가진 모델을 선택하는 것이 중요
  - 조기 종료(Early Stopping) : 오버피팅이 감지될 경우 목표 학습 시간 전이라도 조기 종료 
  - 드록아웃(Dropout) : 일정 비율의 노드(인공 뉴런)을 무작위로 끄고 진행하는 기법
## 7편
전이학습(Transfer Learning)



---
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
    - 단점 : 연산 속도 느림(병렬학습이 어렵고 순차적으로 처리, GPU의 병렬처리 이점을 잘 활용못함), 학습 불안정, 과거 정보를 잘 활용할 수 있는 모델이 아님 (장기 종족성/의존성 문제)
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
    - 시각 : AutoML Vision(클라우드나 에지의 이미지에서 유용한 정보 도출), AutoML Video Intelligence(콘텐츠 탐색 기능과 동영상 환경 지원)
    - 언어 : AutoML Natural Language(머신러닝을 통해 텍스트의 구조와 의미 도출), AutoML Translation(언어를 동적으로 감지하여 각 언어로 번역)
    - 구조화된 데이터 : AutoML Tables(구조화된 데이터에서 최신 머신러닝 모델을 자동으로 빌드하고 배포)
	
- 설명 가능한 인공지능, XAI(eXplainable Artificial Intelligence)
  - 어텐션 메커니즘(Attention Mechanism)을 활용한 XAI
  - 설명하는 법 학습하기(Learn to explain)

# Library
- Http Server : Jetty 9 Embedded (https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-helloworld)
- Http Client : Jetty 9 HttpClient (https://www.eclipse.org/jetty/documentation/jetty-9/index.html#http-client)
- Json : Google Gson 2.8.6 (https://github.com/google/gson)
