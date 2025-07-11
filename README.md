# 🎫 netflix security 프로젝트

**netflix security**는 구독형 멤버십 서비스를 구현하기 위해 Spring Boot 기반의 예제입니다.
본인에 초점을 맞추어 Spring Security 를 활용하고, 인증과 인가 기능을 구현하며, 
기본적인 로그인 / 로그아웃 기능과 더불어 소셜 로그인을 포함하고있습니다..

---

## 🛠️ 기술 스택

- SpringBoot 3.5.0
- Java 17
- SpringSecurity 6.x
- SpringBatch 5.x
- JWT (JSON Web Token)
- OAuth 2.0 (카카오, 네이버 소셜 로그인)
- Gradle 빌드
- MySQL
- Flyway
- Docker

---

## 📁 프로젝트 구조
```
netflix-security
|-- netplix-adapters/
|   |-- adapter-http # 외부와의 통신을 담당하는 모듈
|   |-- adapter-persistence # DB 와의 통신을 담당하는 모듈
|   |-- adapter-redis # 레디스와의 통신을 담당하는 모듈
|-- netplix-apps/
|   |-- app-api # REST API 를 모아둔 모듈
|   |-- app-batch # 배치잡을 모아둔 모듈
|-- netplix-commons/ # 공통 모듈
|-- netplix-core/
|   |-- core-domain/ # 도메인 모델을 담당하는 모듈
|   |-- core-port/ # 외부와의 통신을 위한 인터페이스를 모아둔 모듈
|   |-- core-service/ # 비즈니스 로직을 구현하는 모듈
|   |-- core-usecase/ # 클라이언트에서 호출할 수 있는 인터페이스를 모아둔 모듈
```

---

## 🛠️ 컴포넌트(모듈)별 설명

- **netplix-core**: 비즈니스 로직과 도메인 모델을 관리하는 기본 모듈
- **netplix-apps**: 클라이언트가 호출할 수 있는 REST API 와 배치잡을 모아둔 모듈
- **netplix-adapters**: HTTP Client, DB, Redis 등 외부 인프라와 통신하기 위한 모듈
- **netplix-commons**: 공통으로 사용되는 유틸리티를 모아둔 모듈

---

## ⚙️ 테이블 구조

![netflix_membership_erd.png](./netflix_membership_erd.png)

## ⚡ 영화 데이터 (TMDB - The Movie Database)

- 영화 데이터를 가져오기 위해 TMDB 라는 곳을 활용

---

⚠️ 참고 사항

⚠️ 이 프로젝트는 학습 및 구조 설계 목적의 예제입니다.
실 서비스 적용 시에는 인증, 보안, 장애 복구, 데이터 일관성 등을 추가로 고려해야 합니다.
