# 콘서트 예약 시스템

## 문서 목차

---
### 1. [Milestone](https://github.com/users/rueun/projects/1)
### 2. [Sequence Diagram](https://github.com/rueun/hhplus-concert-reservation/blob/step5/docs/SEQUENCE_DIAGRAM.md)
### 3. [API Specification](https://github.com/rueun/hhplus-concert-reservation/blob/step6/docs/API_SPECIFICATION.md)
### 4. [ERD](https://github.com/rueun/hhplus-concert-reservation/blob/step6/docs/ERD.md)
### 5. [Project Structure](https://github.com/rueun/hhplus-concert-reservation/blob/step6/docs/PROJECT_STRUCTURE.md)
### 6. [(3주차-5주차) Chapter2 서버구축 회고](https://devlog-rueun.tistory.com/entry/%ED%95%AD%ED%95%B4-%ED%94%8C%EB%9F%AC%EC%8A%A4-%EB%B0%B1%EC%97%94%EB%93%9C-6%EA%B8%B0-%EC%B1%95%ED%84%B023%EC%A3%BC%EC%B0%A8-5%EC%A3%BC%EC%B0%A8-%EC%84%9C%EB%B2%84%EA%B5%AC%EC%B6%95-%ED%9A%8C%EA%B3%A0)
### 7. [Concurrency Analysis Report](https://github.com/rueun/hhplus-concert-reservation/blob/step11/docs/CONCURRENCY_REPORT.md)
### 8. [Cache-캐싱을 통한 성능 개선 전략](https://github.com/rueun/hhplus-concert-reservation/blob/main/docs/CACHE.md)
### 9. [Waiting Queue System](https://github.com/rueun/hhplus-concert-reservation/blob/main/docs/WAITING_QUEUE_SYSTEM.md)
### 10. [DB-Index](https://github.com/rueun/hhplus-concert-reservation/blob/main/docs/DB_INDEX.md)
### 11. [MSA 환경에서의 트랜잭션 처리 방안](https://github.com/rueun/hhplus-concert-reservation/blob/main/docs/DB_INDEX.md)
<br>

## swagger-ui

---
![img.png](img.png)
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## 요구사항

---
## Description

- `콘서트 예약 서비스`를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## Requirements
- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.

## API Specs
1️⃣ **`주요` 유저 대기열 토큰 기능**
- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 대기열에 의해 **보호받는** 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.
- **내 대기번호를 조회하는 폴링용 API를 작성합니다.**
> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.


**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**
- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.
> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.


3️⃣ **`주요` 좌석 예약 요청 API**
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 임시배정 상태의 좌석에 대해 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**
- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**
- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.


> 💡**KEY POINT**  
> - 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.   
> - 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.

<br>

## 4주차 요구사항

---
### **`DEFAULT`**

- 하기 **비즈니스 로직** 개발 및 **단위 테스트** 작성
  - `concert` : 대기열 발급, 대기순번 조회, 좌석 예약 기능

> **단위 테스트** 는 반드시 대상 객체/기능 에 대한 의존성만 존재해야 함


### **`STEP07`**

- API Swagger 기능 구현 및 캡쳐본 첨부 ( Readme )
- 주요 비즈니스 로직 개발 및 단위 테스트 작성

### **`STEP08`**

- 비즈니스 Usecase 개발 및 통합 테스트 작성

> API 의 완성이 목표가 아닌, 기본 및 주요 기능의 비즈니스 로직 및 유즈케이스는 구현이 완료 되어야 함. ( `Business Layer` )

> DB Index , 대용량 처리를 위한 개선 포인트 등은 추후 챕터에서 진행하므로 목표는 `기능 개발의 완료` 로 합니다. 최적화 작업 등을 고려하는 것 보다 모든 기능을 정상적으로 제공할 수 있도록 해주세요. 특정 기능을 왜 이렇게 개발하였는지 합당한 이유와 함께 기능 개발을 진행해주시면 됩니다.


## 5주차 요구사항
### **`DEFAULT`**

- 비즈니스 별 발생할 수 있는 에러 코드 정의 및 관리 체계 구축
- 프레임워크별 글로벌 에러 핸들러를 통해 예외 로깅 및 응답 처리 핸들러 구현
  - `spring` - **RestControllerAdvice**
  - `nestjs` - **ExceptionFilter**

### **`STEP 09`**

- 시스템 성격에 적합하게 Filter, Interceptor 를 활용해 기능의 관점을 분리하여 개선
- 모든 API 가 정상적으로 기능을 제공하도록 완성

> 각 시나리오별 요구사항 내에 정의된 기능은 정상적으로 동작할 수 있어야 합니다. 개선 및 최적화에 초점을 두는 것이 아닌, 추후 개선해나갈 수 있도록 동작하는 기능을 완성하는 것이 목적입니다.


### **`STEP 10`**

- 시나리오별 동시성 **통합 테스트** 작성
- **Chapter 2** 회고록 작성

> DB Index , 대용량 처리를 위한 개선 포인트 등은 추후 챕터에서 진행하므로 목표는 `기능 개발의 완료` 로 합니다. 최적화 작업 등을 고려하는 것 보다 모든 기능을 정상적으로 제공할 수 있도록 해주세요. 특정 기능을 왜 이렇게 개발하였는지 합당한 이유와 함께 기능 개발을 진행해주시면 됩니다.



## 6주차 요구사항

---
### **`STEP 11_기본`**

- 나의 시나리오에서 발생할 수 있는 동시성 이슈에 대해 파악하고 가능한 동시성 제어 방식들을 도입해보고 각각의 장단점을 파악한 내용을 정리 제출
  - 구현의 복잡도, 성능, 효율성 등
  - README 작성 혹은 외부 링크, 프로젝트 내의 다른 문서에 작성하였다면 README 에 링크 게재

### **`STEP 12_심화`**

- **DB Lock 을 활용한 동시성 제어 방식** 에서 해당 비즈니스 로직에서 적합하다고 판단하여 차용한 동시성 제어 방식을 구현하여 비즈니스 로직에 적용하고, 통합테스트 등으로 이를 검증하는 코드 작성 및 제출


## 7주차 요구사항

---
### **`STEP 13_기본`**
- 조회가 오래 걸리는 쿼리에 대한 캐싱, 혹은 Redis 를 이용한 로직 이관을 통해 성능 개선할 수 있는 로직을 분석하고 이를 합리적인 이유와 함께 정리한 문서 제출

### **`STEP 14_심화`**
**콘서트 예약 시나리오(대기열 인원수 제한 없음)** 
- 대기열 구현에 대한 설계를 진행하고, 설계한 내용과 부합하도록 적절하게 동작하는 대기열을 구현하여 제출
  - Redis, Queue, MQ 등 DB가 아닌 다른 수단을 활용해 대기열 개선 설계 및 구현 (”Nice to have”)

## 8주차 요구사항

---
### **`STEP 15_기본`**
- 나의 시나리오에서 수행하는 쿼리들을 수집해보고, 필요하다고 판단되는 인덱스를 추가하고 쿼리의 성능개선 정도를 작성하여 제출
  - 자주 조회하는 쿼리, 복잡한 쿼리 파악
  - Index 추가 전후 Explain, 실행시간 등 비교

### **`STEP 16_심화`**
- 내가 개발한 기능의 트랜잭션 범위에 대해 이해하고, 서비스의 규모가 확장된다면 서비스들을 어떻게 분리하고 그 분리에 따른 트랜잭션 처리의 한계와 해결방안에 대한 서비스설계 문서 작성
- 실시간 주문, 좌석예약 정보를 데이터 플랫폼에 전달하거나 이력 데이터를 저장 ( 외부 API 호출, 메세지 발행 등 ) 하는 요구사항 등을 기존 로직에 추가해 보고 기존 로직에 영향 없이 부가 기능을 제공


## 9주차 요구사항

---

### **`STEP 17`**
- docker 를 이용해 kafka 를 설치 및 실행하고 애플리케이션과 연결
- 각 프레임워크 (nest.js, spring) 에 적합하게 카프카 consumer, producer 를 연동 및 테스트

### **`STEP 18`**
- 기존에 애플리케이션 이벤트를 카프카 메세지 발행으로 변경
- 카프카의 발행이 실패하는 것을 방지하기 위해 Transactional Outbox Pattern를 적용
- 카프카의 발행이 실패한 케이스에 대한 재처리를 구현 ( Scheduler or BatchProcess )