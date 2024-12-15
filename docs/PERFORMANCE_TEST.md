# 콘서트 대기열 서비스 성능 테스트 및 장애 대응 보고서

---

## 목차

## 성능 테스트 지표

---
### 1. TPS(Transaction Per Second)
- 시스템이 얼마나 많은 트랜잭션을 처리할 수 있는지를 나타내는 지표
- 시스템의 처리량을 나타내는 지표
- TPS = (시스템이 처리한 트랜잭션 수) / (시스템이 처리한 시간)
- TPS가 높을수록 시스템의 처리량이 높다고 볼 수 있다.

### 2. Latency(지연 시간)
- 시스템이 요청을 받고 응답을 보내기까지 걸리는 시간
- 시스템의 응답 속도를 나타내는 지표
- Latency가 낮을수록 시스템의 응답 속도가 빠르다고 볼 수 있다.

### 3. Error Rate(에러율)
- 시스템이 처리한 요청 중 에러가 발생한 비율
- 시스템의 안정성을 나타내는 지표
- Error Rate가 낮을수록 시스템의 안정성이 높다고 볼 수 있다.
- Error Rate = (에러가 발생한 요청 수) / (전체 요청 수)


## 성능 테스트 종류

---
### 1. 부하 테스트(Load Test)
- 시스템이 정상적으로 동작하는지 확인하기 위한 테스트
- 시스템의 최대 처리량을 확인하기 위한 테스트
- 목표 값을 설정하고, 목표값만큼의 부하를 견딜 수 있는지를 테스트한다.

### 2. 스트레스 테스트(Stress Test)
- 시스템이 과부하 상태에서도 정상적으로 동작하는지 확인하기 위한 테스트
- 시스템의 최대 처리량을 넘어서는 부하를 가해 시스템의 성능을 확인한다.

### 3. 중단점 테스트(Breakpoint Test)
- 시스템이 어느 정도의 부하까지 견딜 수 있는지 확인하기 위한 테스트
- 부하를 점진적으로 증가시키며, 시스템이 어느 지점에서 더 이상 동작하지 않는지 확인한다.
- 시스템의 한계를 확인하기 위한 테스트

### 4. 스파이크 테스트(Spike Test)
- 갑작스런 부하가 발생했을 때 시스템이 어떻게 동작하는지 확인하기 위한 테스트
- 이벤트성 부하를 가해 시스템의 성능을 확인한다.


## 성능 테스트를 위한 도구 및 환경 설정

---
### k6
- 성능 테스트를 위한 오픈소스 도구
- Javascript로 테스트 스크립트를 작성하여 성능 테스트를 진행할 수 있다.
- 다양한 프로토콜(HTTP, Websocket, gRPC)을 지원한다.
- CLI(Command Line Interface)를 통해 테스트를 실행할 수 있다.
- 테스트 결과를 다양한 형태로 출력할 수 있다.


### docker-compose.yml 작성
- kafka, zookeeper, kafka-ui, redis, grafana, influxdb, prometheus, concert-service 를 docker-compose.yml 로 구성
- concert-service 는 미리 생성한 docker image를 사용
- concert-service 애플리케이션의 cpu 및 memory 설정을 통해 운영 환경에 가깝게 설정하여 유의미한 테스트를 진행
```yaml
version: '3.8'
services:
  # kafka
  zookeeper:
    container_name: zookeeper
    image: arm64v8/zookeeper:latest
    networks:
      - concert_system_network
    ports:
      - '2181:2181'
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
      - ZOO_TLS_CLIENT_AUTH=none
      - ZOO_TLS_QUORUM_CLIENT_AUTH=none

  kafka:
    container_name: kafka
    image: bitnami/kafka:latest
    networks:
      - concert_system_network
    ports:
      - '9092:9092'
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_CFG_LISTENERS=LC://kafka:29092,LX://kafka:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=LC://kafka:29092,LX://${DOCKER_HOST_IP:-localhost}:9092
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=LC:PLAINTEXT,LX:PLAINTEXT
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=LC
    depends_on:
      - zookeeper

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-ui
    networks:
      - concert_system_network
    ports:
      - "8989:8080"
    restart: always
    depends_on:
      - kafka
    environment:
      - KAFKA_CLUSTERS_0_NAME=local-cluster
      - KAFKA_CLUSTERS_0_BOOTSTRAP_SERVERS=kafka:29092
      - KAFKA_CLUSTERS_0_ZOOKEEPER=zookeeper:2181

  # redis
  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"
    networks:
      - concert_system_network
    volumes:
      - ./redis:/data

  # grafana
  grafana:
    container_name: grafana
    image: grafana/grafana:latest
    networks:
      - concert_system_network
    ports:
      - "3000:3000"
    environment:
      - GF_AUTH_ANONYMOUS_ORG_ROLE=Admin
      - GF_AUTH_ANONYMOUS_ENABLED=true
      - GF_AUTH_BASIC_ENABLED=false
    volumes:
      - ./grafana:/etc/grafana/provisioning/

  # influxdb
  influxdb:
    container_name: influxdb
    image: influxdb:1.8
    networks:
      - concert_system_network
    ports:
      - "8086:8086"
    environment:
      - INFLUXDB_DB=k6

  # prometheus
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    networks:
      - concert_system_network
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.enable-lifecycle'
    restart: always
    extra_hosts:
      - "host.docker.internal:host-gateway"


  # application
  concert-service:
    image: hhplus-concert-reservation:0.0.1
    container_name: concert-app
    networks:
      - concert_system_network
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/hhplus_concert_reservation?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=root
      - SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
      - SPRING_KAFKA_BOOTSTRAP-SERVERS=kafka:29092
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
    depends_on:
      - kafka
      - redis
    deploy:
      resources:
        limits:
          memory: 2G
          cpus: '3.0'
        reservations:
          memory: 2G
          cpus: '3.0'

networks:
  concert_system_network:
    driver: bridge
```

## 성능 테스트 결과

---

### 시나리오1 : 10,000 명의 사용자가 동시에 대기열을 생성하고 조회한다.
- 대기열 생성 및 조회 시스템의 처리량 및 응답 속도를 확인하기 위한 시나리오
- 대기열 생성 및 조회는 콘서트 예약 서비스의 핵심 기능이다.

#### k6 스크립트1 - 사용자 수를 고정하고 대기열 생성 및 조회
```javascript
import http from 'k6/http';
import {check, sleep} from 'k6';

const host = 'http://localhost:8080'; // 테스트할 서버 URL


// 부하 테스트 옵션 설정
export const options = {
    scenarios: {
        peek_test: {
            executor: 'constant-vus',
            vus: 10000,
            duration: '2m'
        },
    },
};

// 대기열 생성 함수
function createQueue(userId) {
    const res = http.post(`${host}/api/v1/waiting-queues`, // 요청 URL
        JSON.stringify({ userId: userId }), // 요청 바디에 랜덤 userId 포함
        {
            headers: { 'Content-Type': 'application/json' }, // JSON 요청 헤더
        }
    );
    check(res, { 'Queue created successfully': (r) => r.status === 201 });
    return JSON.parse(res.body).token;
}

// 대기열 조회 함수
function checkQueue(queueToken) {
    const res = http.get(`${host}/api/v1/waiting-queues`, {
        headers: {
            'accept': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Queue status retrieved': (r) => r.status === 200 });
    return JSON.parse(res.body).status; // 대기열 순번 반환
}

// VU 별로 실행되는 메인 시나리오
export default function () {
    const userId = Math.floor(Math.random() * 1000) + 1; // 1~1000 사이의 랜덤 숫자
    console.log(`User ${userId} is trying to book a concert ticket.`)
    const queueToken = createQueue(userId); // 1. 대기열 생성
    let queueStatus;
    do {
        sleep(3); // 대기열 조회 간격
        queueStatus = checkQueue(queueToken); // 2. 대기열 조회
    } while (queueStatus !== "ACTIVATED"); // 대기열이 활성화 상태가 될 때까지 반복
}
```
- `constant-vus` : 가상 사용자 수를 일정하게 유지하는 방식
- 대기열 생성 및 조회 시스템의 안정성을 확인하기 위한 시나리오
- 한 번에 여러 사용자가 대기열을 생성하고 조회하는 상황을 모방하기 위함


#### 조건 1 - CPU: 1, Memory: 2G
![docker-1.png](images/docker-1.png)
![img1.png](images/k6-1.png)
![k6-1.png](images/grafana-k6-1.png)

- CPU가 1로 설정되어 있어, 대기열 생성 및 조회 시스템의 처리량이 낮고, 실패하는 경우가 많이 발생했다.
- 주요 지표 
  - **서버 연결 시간 (http_req_connecting)**
    - 서버에 연결하는 데 걸린 평균 시간은 52.51µs 이고, 연결이 거의 즉시 이루어졌다. 
  - **HTTP 요청 응답 시간 (http_req_duration)**
    - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 658.73ms였다. 
    - P90이 192.67ms, P95은 10s 이며 최대 응답 시간은 1m 이다.
  - 대부분의 요청이 1초 이내에 완료되었다.
  - **서버 응답 대기 시간 (http_req_waiting)**
    - 서버에서 응답을 기다리는 데 걸린 평균 시간은 658.67ms 이다.


#### 조건 2 - CPU: 3, Memory: 2G
![docker-2.png](images/docker-2.png)
![k6-2.png](images/k6-2.png)
![grafana-k6-2.png](images/grafana-k6-2.png)
- CPU가 3으로 설정되어 있어, 1로 설정했을 때보다 대기열 생성 및 조회 시스템의 처리량이 높아졌다.
- 주요 지표
    - **서버 연결 시간 (http_req_connecting)**
        - 서버에 연결하는 데 걸린 평균 시간은 53.94µs 이고, 연결이 거의 즉시 이루어졌다.
    - **HTTP 요청 응답 시간 (http_req_duration)**
        - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 624.44ms였다.
        - P90이 75.8ms, P95은 8.17s 이며 최대 응답 시간은 1m 이다.
    - 대기열이 활성화 될 때까지 조회하기 때문에 시간이 많이 소요된다.
    - 대부분의 요청이 1초 이내에 완료되었다.
    - **서버 응답 대기 시간 (http_req_waiting)**
        - 서버에서 응답을 기다리는 데 걸린 평균 시간은 624.41ms 이다.

- 위의 테스트를 통해 cpu 수가 성능에 미치는 영향을 확인할 수 있었다.


### 시나리오2 : 200,000 명의 사용자가 동시에 대기열을 요청한다.
- 대기열 생성의 응답 속도를 확인하기 위한 시나리오
- 대기열 생성은 콘서트 예약 서비스의 핵심 기능이다.
![waiting-queue-create-k6.png](images%2Fwaiting-queue-create-k6.png)
- 주요 지표
    - **http_reqs** : 약 200,000개의 요청을 처리하였다.
    - **서버 연결 시간 (http_req_connecting)**
        - 평균 시간: 1.63µs
        - 연결이 거의 즉시 이루어졌다.
    - **HTTP 요청 응답 시간 (http_req_duration)**
      - avg=256.67ms  min=3.54ms med=256.21ms max=786.73ms p(90)=426.3ms p(95)=455.42ms
      - 대부분의 요청이 1초 이내에 완료되었다.
    - **서버 응답 대기 시간 (http_req_waiting)**
      - avg=256.64ms  min=3.5ms med=256.19ms max=786.71ms p(90)=426.26ms p(95)=455.39ms
      - 서버에서 응답을 기다리는 데 걸린 평균 시간은 256.64ms 이다.
      - 대부분의 요청이 1초 이내에 완료되었다.
- 요약
    - 대부분의 요청이 1초 이내에 처리되었으며, 응답 시간이 빠르다.
    - 서버 응답 대기 시간이 256.64ms로 빠르게 처리되었다.
    - 대기열 생성 시스템의 처리량 및 응답 속도가 안정적이다.


### 시나리오3 : 10,000 명 이상의 사용자가 동시에 대기열을 생성하고 조회한다.
- 대기열 생성 및 조회 시스템의 처리량 및 응답 속도를 확인하기 위한 시나리오
![waiting-queue-create-read-k6.png](images%2Fwaiting-queue-create-read-k6.png)
- 주요 지표
    - **http_reqs** : 약 20,000 개의 요청 처리
    - **서버 연결 시간 (http_req_connecting)**
        - 평균 시간: 21.19µs
        - 연결이 거의 즉시 이루어졌다.
    - **HTTP 요청 응답 시간 (http_req_duration)**
        - avg=3.19ms  min=416µs med=2.53ms max=63.66ms p(90)=5.77ms p(95)=8ms
        - 대부분의 요청이 짧은 시간 내에 완료되었다.
    - **서버 응답 대기 시간 (http_req_waiting)**
      - avg=3.11ms  min=364µs med=2.45ms max=63.55ms p(90)=5.68ms p(95)=7.88ms
      - 서버 평균 응답 대기 시간이 3.11ms로 빠르게 처리되었다.
- 요약
    - 대부분의 요청이 1초 이내에 처리되었으며, 응답 시간이 빠르다.
    - 서버 응답 대기 시간이 3.11ms로 빠르게 처리되었다.
    - 대기열 생성 및 조회 시스템의 처리량 및 응답 속도가 안정적이다.



### 시나리오4 : 300명의 사용자가 동시에 세션 조회, 좌석 조회, 예약, 결제 프로세스를 진행한다.
- 세션 조회, 좌석 조회, 예약, 결제 프로세스를 진행하는 시나리오
- 대기열로 앞에서 동시 접속자를 제한하기 때문에 VUser를 300명으로 설정하여 진행
- python script 로 미리 활성 토큰을 생성하여 사용자별로 토큰을 부여한 후 테스트 진행
- 실제 유저의 행동과 비슷하게 각 API 요청마다 sleep을 주어 진행

#### k6 스크립트 - 사용자 수를 고정하고 세션 조회, 좌석 조회, 예약, 결제 프로세스 진행
```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

const host = 'http://localhost:8080'; // 테스트할 서버 URL

export const options = {
    scenarios: {
        concert_booking: {
            executor: 'per-vu-iterations',  // 각 가상 사용자별로 반복 횟수 설정
            vus: 300,  // 300명의 가상 사용자
            iterations: 1  // 각 가상 사용자가 1번만 실행
        },
    },
};

// 콘서트 회차 조회 함수
function getConcertSession(queueToken) {
    const res = http.get(`${host}/api/v1/concerts/1/sessions`, {
        headers: {
            'accept': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Concert schedule retrieved': (r) => r.status === 200 });
    const sessions = JSON.parse(res.body).sessions;
    // sessions 배열에서 랜덤으로 하나의 세션을 선택하여 리턴
    const randomSession = sessions[Math.floor(Math.random() * sessions.length)];
    return randomSession.sessionId;
}

// 콘서트 좌석 조회 함수
function getAvailableSeat(sessionId, queueToken) {
    const res = http.get(`${host}/api/v1/concerts/1/sessions/${sessionId}/seats`, {
        headers: {
            'accept': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Seat availability retrieved': (r) => r.status === 200 });
    const availableSeats = JSON.parse(res.body).availableSeats;
    const randomCount = Math.floor(Math.random() * 2) + 1; // 1~2 사이의 랜덤 숫자
    const shuffledSeats = availableSeats.sort(() => 0.5 - Math.random()); // 좌석 배열 섞기
    const selectedSeats = shuffledSeats.slice(0, Math.min(randomCount, shuffledSeats.length)); // 가능한 개수만큼 선택
    return selectedSeats.map(seat => seat.seatId);
}

// 콘서트 예약 함수
function reserveSeat(userId, sessionId, seatIds, queueToken) {
    const res = http.post(`${host}/api/v1/concerts/1/sessions/${sessionId}/reservations`, JSON.stringify({
        userId: userId,
        seatIds: seatIds
    }), {
        headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Seat reserved successfully': (r) => r.status === 201 });
    return JSON.parse(res.body).reservationId;
}

// 예약 결제 함수
function makePayment(userId, reservationId, queueToken) {
    const res = http.post(`${host}/api/v1/payments`, JSON.stringify({
        userId: userId,
        reservationId: reservationId
    }), {
        headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Payment successful': (r) => r.status === 201 });
}

// VU 별로 실행되는 메인 시나리오
export default function () {
    const userId = `${__VU}`;
    const queueToken = `queue_token_${__VU}`; // VU별로 토큰을 순차적으로 생성
    
    const sessionId = getConcertSession(queueToken); // 3. 콘서트 회차 조회

    sleep(3);

    const seatIds = getAvailableSeat(sessionId, queueToken); // 4. 콘서트 좌석 조회

    sleep(2);

    const reservationId = reserveSeat(userId, sessionId, seatIds, queueToken); // 5. 콘서트 예약

    sleep(2);

    makePayment(userId, reservationId, queueToken); // 6. 결제*!/*/
}
```
- `per-vu-iterations` : 각 가상 사용자별로 반복 횟수를 설정하는 방식
- 세션 조회, 좌석 조회, 예약, 결제 프로세스를 진행하는 시나리오
- 한 번에 여러 사용자가 세션 조회, 좌석 조회, 예약, 결제 프로세스를 진행하는 상황을 나타낸다.

![img.png](images/img.png)
![img_1.png](images/img_1.png)
- 주요 지표
    - **서버 연결 시간 (http_req_connecting)**
        - 서버에 연결하는 데 걸린 평균 시간은 94.02µs 이고, 연결이 거의 즉시 이루어졌다.
    - **HTTP 요청 응답 시간 (http_req_duration)**
        - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 225.5ms였다.
        - P90이 794.22ms, P95은 837.54ms 이며 최대 응답 시간은 1s 이다.
    - 대부분의 요청이 1초 이내에 완료되었다.
    - **서버 응답 대기 시간 (http_req_waiting)**
        - 서버에서 응답을 기다리는 데 걸린 평균 시간은 225.13ms 이다.
- 요약
    - 대부분 요청이 1초 이내에 처리되었으며, 응답 시간이 빠르다.
    - 서버 응답 대기 시간이 225.13ms로 빠르게 처리되었다.

## 성능 테스트 결과 분석
- 성능 테스트를 통해 시스템의 처리량, 응답 속도, 안정성을 확인할 수 있었다.
- 시나리오1에서는 CPU 수가 성능에 미치는 영향을 확인할 수 있었다.
- 시나리오2에서는 세션 조회, 좌석 조회, 예약, 결제 프로세스를 진행하는 시나리오를 통해 시스템의 안정성을 확인할 수 있었다.
- 현재 카프카를 통한 비, 캐싱, 인덱스 등 


## 장애 대응 및 개선 방안

---
### 대기열 생성 및 조회 시나리오
- 부하 테스트를 통해 대기열 생성 및 조회까지의 성능을 평가한 결과, HTTP 요청 응답 시간은 전반적으로 안정적이었다.
- 그러나, CPU 수가 1로 설정되어 있을 때, 대기열 생성 및 조회 시스템의 처리량이 낮고, 실패하는 경우가 많이 발생했다.
- CPU 수가 3으로 설정되어 있을 때, 대기열 생성 및 조회 시스템의 처리량이 높아졌다.
- 개선 방안
  - CPU 수를 높여 시스템의 처리량을 높인다.
  - 시스템 자원 모니터링 강화
  - 요청이 많이 들어오면 트리거 되는 알람을 설정하여 서버의 자원을 늘릴 수 있도록 한다.
    - 트리거 되면 자동으로 서버의 자원을 늘리는 스케일 아웃 기능을 사용할 수 있다.

### 콘서트 조회 ~ 결제 시나리오
- 콘서트 조회, 일정 조회, 좌석 조회 및 예약의 각 단계를 포함한 시나리오에 대해 테스트를 수행한 결과, 좌석 예약 단계에서 성능 저하가 눈에 뛰게 나타났다.
- 시스템 전체의 평균 응답 시간은 양호했으나, 일부 요청에서 비정상적으로 긴 지연 시간이 발생한 경우도 확인되었다.
- 개선 방안 
  - 지연을 줄이기 위해 DB 쿼리 최적화를 진행한다.(인덱스, 쿼리 리팩토링 등..)
  - 자주 조회되는 콘서트 목록, 일정, 좌석 정보에 대한 캐싱을 적용하여 DB 조회 빈도를 줄여 성능을 향상시킬 수 있다.
    - 미리 캐시 웜업 작업을 수행하여 캐시 히트율을 높인다.
  - Grafana 같은 자원 모니터링 툴을 이용하여 시스템 자원을 모니터링한다.


### 데이터베이스 과부하 시
#### 시나리오
- 콘서트 예매 오픈 후, 동시에 많은 사용자가 몰려 DB 부하가 급증하는 상황이 발생할 수 있다.
- 이러한 상황에서 DB 쿼리가 느려지거나 응답이 지연되는 문제가 발생할 수 있다.

#### 대응 방안
- DB 쿼리 최적화
  - 인덱스를 적절히 사용하여 쿼리 성능을 향상시킨다.
  - 쿼리 리팩토링을 통해 비효율적인 쿼리를 개선한다.
- DB 리소스 증설
  - DB 인스턴스의 CPU, 메모리, 디스크 용량 등을 증설하여 부하를 분산시킨다.
- 캐싱 전략 강화
  - 자주 조회되는 데이터에 대한 캐싱을 강화하여 DB 부하를 줄인다.
- 읽기/쓰기 분리
  - 읽기와 쓰기를 분리하여 읽기 전용 DB를 별도로 운영한다.
  - 읽기 전용 DB는 캐싱을 적용하여 응답 속도를 향상시킨다.

### 캐시 서버(Redis) 장애 시
#### 시나리오
- 캐시 서버에 장애가 발생하면, 캐시 서버에 저장된 데이터에 접근할 수 없어 성능이 저하되는 문제가 발생할 수 있다.

#### 대응 방안
- 캐시 서버의 장애 대응을 위한 백업 전략
  - 캐시 서버의 데이터를 주기적으로 백업하여 장애 시 데이터를 복구한다.
- 캐시 서버 재시작 및 상태 점검
  - 캐시 서버의 장애 발생 시, 캐시 서버를 재시작하고 상태를 점검한다.
- 캐시 서버 클러스터링
  - 캐시 서버를 클러스터링하여 장애 발생 시 다른 서버로 자동으로 전환되도록 한다.
- 캐시 서버 모니터링 (예방)
  - 캐시 서버의 상태를 모니터링하여 장애 발생 시 빠르게 대응한다.
- 데이터 분산 저장
  - 캐시 서버에 데이터를 분산하여 저장하여 장애 시 데이터 손실을 최소화한다.

### 결론
- 테스트 결과, 토큰 생성 및 콘서트 예약 과정에서 일부 성능 저하와 지연이 발생하는 문제가 발견되었다.
- 이러한 문제를 해결하기 위해 **DB 쿼리 최적화**, **캐싱 전략 강화**, **시스템 자원 모니터링 강화**, **로그 수집** 등의 개선 작업을 수행해 볼 수 있으며, 이로 인해 시스템의 전반적인 성능 향상을 기대할 수 있다.
- 부하 테스트를 통해 미리 시스템의 성능과 안정성을 강화해 볼 수 있고 향후 발생할 수 있는 문제에 대한 빠른 대응이 가능하다.



## 참고

---
- [성능테스트 - 성능 테스트의 목적 / 종류 / 지표 / 용어 정리](https://dewble.tistory.com/entry/concept-of-performance-test)
