# AI 기반 상품 리뷰 분석 및 검색 시스템

소비자가 상품 리뷰에서 AI가 추출한 특성별 평가를 기반으로 상품을 검색하고 비교할 수 있는 시스템입니다. 러닝화를 시작으로 다양한 카테고리로 확장 가능한 구조를 갖추고 있습니다.

## 기술 스택

- Java 17+
- Spring Boot 3.4
- Spring AI (현재: Ollama / Llama3, 향후 Cloud LLM으로 전환 예정)
- Spring Data JPA
- Lombok
- Gradle

## 주요 기능

### 1. 리뷰 등록
사용자가 상품에 대한 텍스트 리뷰와 수동 점수를 등록합니다. 텍스트가 있는 리뷰만 AI 분석 대상에 포함됩니다.

### 2. 특성 관리
관리자가 AI 분석에 사용할 특성을 사전 등록합니다. 각 특성은 두 가지 값유형을 지원합니다:
- **SCORE**: 1~5 범위의 숫자 점수 (예: 반발력, 무게감)
- **CHOICE**: 사전 정의된 옵션 중 선택 (예: 발볼 넓이 → 넓음/보통/좁음)

### 3. AI 리뷰 분석 파이프라인
등록된 리뷰를 LLM이 자동으로 분석하여 특성별 평가를 추출합니다.
- Strategy 패턴 기반 분석기 구조 (`ScoreBasedAnalyzer`, `ChoiceBasedAnalyzer`)
- `AnalyzerRegistry`를 통한 값유형별 분석기 디스패치
- `(review_id, characteristic_id)` 유니크 키 기반 특성 단위 분석 추적
- 새 특성 추가 시 기존 리뷰에 대한 추가 분석 자동 지원

### 4. 분석 결과 검증
AI 반환 결과의 유효성을 검증하여 잘못된 데이터 저장을 방지합니다.
- SCORE: `isRelated=true`이면 `scoreValue > 0`, `false`이면 `scoreValue == 0`
- CHOICE: `isRelated=true`이면 `choiceValue`가 등록된 옵션 목록에 포함되어야 함

### 5. 상품별 특성 점수 집계
- SCORE 타입: 관련 평가의 평균 점수 산출
- CHOICE 타입: 관련 평가의 최빈 옵션값 산출

### 6. 특성 기반 상품 검색
원하는 특성 조건으로 상품을 필터링합니다.
- SCORE 필터: 평균 점수 N점 이상
- CHOICE 필터: 최빈값이 지정 옵션과 일치
- 다중 필터 AND 결합 지원

### 7. 상품 대시보드
상품의 특성별 점수 요약과 리뷰 목록을 대시보드 형태로 제공합니다.

## 프로젝트 구조

```
src/main/java/com/example/hwiai/
├── HwiaiApplication.java          # 메인 애플리케이션
├── analyzer/                       # AI 분석기 모듈
│   ├── AnalyzerRegistry.java       # 값유형별 분석기 디스패치
│   ├── BaseAnalyzer.java           # 분석기 공통 로직
│   ├── CharacteristicAnalyzer.java # 분석기 인터페이스
│   ├── ScoreBasedAnalyzer.java     # SCORE 타입 분석기
│   ├── ChoiceBasedAnalyzer.java    # CHOICE 타입 분석기
│   ├── controller/
│   ├── dto/
│   └── service/
│       └── AnalyzerService.java    # 분석 파이프라인 오케스트레이션
├── characteristic/                 # 특성 관리 모듈
│   ├── Characteristic.java         # 특성 엔티티
│   ├── CharacteristicOption.java   # CHOICE 옵션 엔티티
│   ├── ValueType.java              # SCORE / CHOICE 열거형
│   ├── repository/
│   └── service/
├── evaluation/                     # 평가 모듈
│   ├── Evaluation.java             # AI 분석 결과 엔티티
│   ├── controller/
│   ├── dto/
│   ├── repository/
│   └── service/
├── product/                        # 상품 모듈
│   ├── Product.java
│   └── repository/
├── review/                         # 리뷰 모듈
│   ├── Review.java
│   ├── controller/
│   ├── dto/
│   ├── repository/
│   └── service/
├── entity/
│   └── BaseEntity.java             # 공통 엔티티 (생성일/수정일)
└── util/
    └── AnalyzeValue.java
```

## 시작하기

### 사전 요구사항

- JDK 17 이상
- [Ollama](https://ollama.ai/) 설치 및 실행
- Llama3 모델 다운로드: `ollama pull llama3`

### 실행

```bash
# Ollama 서버 실행 (별도 터미널)
ollama serve

# 애플리케이션 실행
./gradlew bootRun
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

## API 엔드포인트

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/reviews` | 리뷰 등록 |
| POST | `/analyzer/{productId}` | 상품 리뷰 AI 분석 실행 |
| GET | `/evaluations/{productId}` | 상품 평가 결과 조회 |

## 아키텍처

```
리뷰 등록 → AI 분석 파이프라인 → 평가 저장 → 집계 → 검색/대시보드
                  │
    ┌─────────────┼─────────────┐
    │             │             │
ScoreBasedAnalyzer  ChoiceBasedAnalyzer
    │             │             │
    └─────────────┼─────────────┘
                  │
              LLM (Ollama)
```

## LLM 전환 계획

현재 로컬 Ollama(Llama3)를 사용하고 있으며, 향후 Cloud LLM(OpenAI, Anthropic, Amazon Bedrock 등)으로 전환할 예정입니다. Spring AI의 추상화 계층 덕분에 `application.yml`의 설정 변경과 의존성 교체만으로 전환이 가능합니다.

```yaml
# 예시: OpenAI로 전환 시
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        model: gpt-4o
```

```gradle
// build.gradle 의존성 변경
// 제거: implementation 'org.springframework.ai:spring-ai-ollama-spring-boot-starter'
// 추가: implementation 'org.springframework.ai:spring-ai-openai-spring-boot-starter'
```

분석기(`ScoreBasedAnalyzer`, `ChoiceBasedAnalyzer`)는 Spring AI의 `ChatClient`를 통해 LLM과 통신하므로, 비즈니스 로직 변경 없이 LLM 제공자를 교체할 수 있습니다.
