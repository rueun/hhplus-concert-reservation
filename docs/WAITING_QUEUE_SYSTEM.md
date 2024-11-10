# 대기열 시스템 설계

--- 

## 목차
> * [개요](#개요)
> * [시스템 설계](#시스템-설계)
>   + [기존 방식: RDB 를 이용한 대기열 시스템](#기존-방식--rdb-를-이용한-대기열-시스템)
>     - [문제점](#문제점)
>   + [새로운 방식: Redis 를 이용한 대기열 시스템](#새로운-방식--redis-를-이용한-대기열-시스템)
>     - [선정 이유](#선정-이유)
>     - [사용한 자료구조](#사용한-Redis-자료구조)
>     - [구현 방식](#구현-방식)
>     - [대기열에서 참가열로 전환하는 개수 계산](#대기열에서-참가열로-전환하는-개수-계산)
> * [부하 테스트](#부하-테스트)
>   + [테스트 결과](#테스트-결과)
> * [참고](#참고)

---

## 개요
- 콘서트 예약 시, 과도한 트래픽으로 인해 서버가 다운되는 문제를 해결하기 위해 대기열 시스템을 도입
- DB 테이블로 관리하는 방식과 Redis를 이용한 방식을 비교하여 Redis를 이용한 대기열 시스템을 도입

---

## 시스템 설계

### 기존 방식: RDB 를 이용한 대기열 시스템
- 대기열 정보를 RDB에 저장하고 관리하는 방식

#### 문제점
  - `성능 저하`: RDB 에서 대기열을 관리할 때 대기열의 상태를 자주 업데이트해야 하므로, 데이터베이스 트랜잭션이 빈번히 발생하여 성능이 저하될 수 있다. 특히, 대기열에 많은 트래픽이 발생할 경우 데이터베이스의 과부하로 이어진다.
  - `확장성 부족`: 실시간으로 많은 요청을 처리하는 대기열에는 적합하지 않다. 수평적 확장에 제한이 있어, 데이터베이스 클러스터나 샤딩 등 추가 구성이 필요할 수 있다.
  - `지연 문제`: 대기열의 상태를 읽고 쓰는 속도가 제한적이기 때문에, 대기열에서의 지연이 발생할 수 있다. 대기 시간은 증가하고, 실시간성을 요구하는 서비스에서 문제가 발생할 수 있다.
  - `과도한 트래픽 처리 불가`: 일시적으로 많은 트래픽이 발생하면 DB 처리 성능이 저하된다.


### 새로운 방식: Redis 를 이용한 대기열 시스템
- 대기열 및 참가열 정보를 Redis에 저장하고 관리하는 방식

#### 선정 이유
- `빠른 속도`: 인메모리 저장소로 읽기와 쓰기 속도가 매우 빠르다. 또한 대기열의 상태를 실시간으로 관리할 수 있어, 요청 지연이 크게 감소하며 실시간 처리가 요구되는 서비스에 적합하다.
- `확장성`: 클러스터링을 통해 수평적 확장이 가능하며, 많은 트래픽이 발생해도 대기열 시스템이 안정적으로 유지될 수 있다. 이를 통해 유연한 확장이 가능해지고, 대기열의 효율적인 관리가 가능해진다.
- `데이터 구조의 유연성`: List, Set, Hash 등의 다양한 데이터 구조를 제공해 대기열을 보다 간단하게 구현할 수 있다.
- `분산 환경 지원`: 여러 노드에 걸쳐 데이터가 분산될 수 있어 고가용성을 보장하므로 대기열 시스템의 안정성이 높아진다.
- `안정성`: 지속적인 데이터 백업과 복제를 통해 데이터의 안정성을 보장한다.


#### 사용한 Redis 자료구조
- 대기열: **sorted set**
    - Score 를 기준으로 정렬된 중복을 허용하지 않는 고유한 값들을 관리하는 컬렉션
    - 대기열에 입장한 순서대로 참가열에 입장해야 하므로 사용
    - `key: waiting-queue` `member: {token}` `score: {timestamp}`

- 참가열: **sets**
    - 중복을 허용하지 않는 고유한 값들의 컬렉션
    - 대기열에서 참가열로 진입한 토큰을 관리
    - `key: active-queue` `member: {token}`
  

- 토큰 메타 정보: **hash**
    - 토큰의 메타 정보를 관리하는 해시맵
    - `key: token-meta:{token}` `field: {userId}`
    - 대기열 생성 시, 해당 정보 저장
    - 활성열 진입 시, ttl 설정(5분)

    

#### 구현 방식
1. **대기열 생성**
   - 대기열 생성 시, 대기열에 토큰을 추가하고, 토큰의 메타 정보를 해시에 저장한다.
   > - ZADD waiting-queue {timestamp} {token}
   > - HSET token-meta:{token} userId {userId}

2. **참가열 진입**
   - 대기열에서 참가열로 진입 시, 대기열에서 토큰을 제거하고, 참가열에 토큰을 추가한다.
   - 이때, 토큰 메타 정보에 TTL 을 설정한다(5분).
   > - ZPOPMIN waiting-queue {n} // n개의 토큰을 가져온다
   > - SADD active-queue {token} // n개의 토큰을 참가열로 이동
   > - EXPIRE token-meta:{token} 300 // 5분 후 만료되도록 설정

3. **참가열 만료**
   - 예약을 완료하는 경우 참가열에서 토큰을 제거한다. 이때, 토큰 메타 정보도 함께 삭제한다.
   > - SREM active-queue {token}
   > - DEL token-meta:{token}

4. **참가열 조회 시 만료된 토큰 제거**
   - 참가열에 있는 토큰을 조회한다. 만약 참가열에 토큰이 있고, 메타 정보의 ttl이 만료되었다면, 참가열에서 해당 토큰을 제거한다.
    > - SISMEMBER active-queue {token}
    > - DEL token-meta:{token} (참가열에 토큰이 있으면서, ttl 만료된 경우)

<br>

#### 대기열에서 참가열로 전환하는 대기열 개수 계산
- 대기열에 유입된 순서대로 스케줄러를 통해 지정한 개수만큼 참가열로 전환시킨다.
  > 특정 주기마다 참가열로 전환되는 토큰의 개수는 대략적으로 정의한 값이고, 추후 운영 환경으로 세팅 후 부하 테스트 등을 통해 더 정확한 수치를 도출해 지정해야 한다.
    - 스케줄러 주기당 진입시키는 유저수 계산
        1. **한 유저가 콘서트 조회를 시작한 이후에 하나의 예약을 완료할 때까지 걸리는 시간**: `평균 1분`
        2. **DB에 동시에 접근할 수 있는 트래픽의 최대치를 계산**: 약 1,000 TPS (초당 트랜잭션 수) ⇒ 1분당 60,000
            - 이 수치는 테스트 수치이므로, 실제 운영환경에서 부하테스트를 통해 의미있는 수치를 도출할 필요가 있다.
        3. **유저가 예약완료 결제까지 호출하는 API 수**: 약 3회 (ex: 콘서트 좌석조회, 예약, 예외로 인한 재시도(예측치) 등등)

        - 계산 과정
            1. **분당 처리 가능한 트랜잭션 수**:
                - 1,000 TPS * 60초 = **60,000 TPS**
            2. **분당 처리 가능한 유저 수**:
                - 분당 처리하는 트랜잭션 수 / 유저가 호출하는 API 수
                - 60,000 트랜잭션 / 3회 = **20,000 명**
            3. **10초마다 처리 가능한 유저 수**:
                - 20,000 유저 / 60초 * 10초 = **3333 명**
        - **결론**: `10초마다 3,000명씩 유효한 토큰으로 전환시킨다.`

---

### 부하 테스트

#### 1. 대기열 생성
- 시나리오
  - 초당 1000명의 유저가 동시에 대기열에 입장
  - 20초간 총 20000명의 유저가 대기열에 입장

#### 부하 테스트 결과
| 지표                          | RDB 사용                                                              | Redis                                                                      | 개선 사항                              |
|------------------------------|-------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|----------------------------------------|
| **검사 통과율**               | 95.62% (19124/20000)                                                                      | 96.23% (19246/20000)                                                       | +0.61%                                 |
| **데이터 수신량**             | 3.5 MB (168 kB/s)                                                                         | 3.5 MB (169 kB/s)                                                          | -                                      |
| **데이터 전송량**             | 3.3 MB (162 kB/s)                                                                         | 3.3 MB (162 kB/s)                                                          | -                                      |
| **HTTP 요청 차단 시간**       | avg=1.2ms, min=0s, med=2µs, max=100.83ms, p(90)=13µs, p(95)=1.37ms                        | avg=854.05µs, min=0s, med=1µs, max=103.84ms, p(90)=6µs, p(95)=292.04µs     | avg 약 28.83% 단축                     |
| **HTTP 요청 연결 시간**       | avg=1.19ms, min=0s, med=0s, max=100.79ms, p(90)=0s, p(95)=1.35ms                         | avg=841.41µs, min=0s, med=0s, max=100.49ms, p(90)=0s, p(95)=254µs          | avg 약 29.27% 단축                     |
| **HTTP 요청 처리 시간**       | avg=11.25ms, min=601µs, med=2.56ms, max=247.36ms, p(90)=17.84ms, p(95)=65.44ms           | avg=7.6ms, min=0s, med=2.33ms, max=213.82ms, p(90)=16.12ms, p(95)=28.01ms  | avg 약 32.44% 단축                     |
| **HTTP 요청 실패율**          | 4.38% (876/20000)                                                                         | 3.76% (754/20000)                                                          | -0.62%                                 |
| **HTTP 요청 수신 시간**       | avg=15.1µs, min=0s, med=9µs, max=751µs, p(90)=34µs, p(95)=46µs                            | avg=12.64µs, min=0s, med=7µs, max=302µs, p(90)=31µs, p(95)=42µs            | 수신 속도 소폭 개선                    |
| **HTTP 요청 송신 시간**       | avg=47.72µs, min=1µs, med=4µs, max=3.91ms, p(90)=18µs, p(95)=39µs                         | avg=38.04µs, min=0s, med=3µs, max=8.7ms, p(90)=17µs, p(95)=36µs            | 송신 속도 소폭 개선                    |
| **HTTP 요청 대기 시간**       | avg=11.19ms, min=592µs, med=2.53ms, max=247.34ms, p(90)=17.82ms, p(95)=65.4ms            | avg=7.55ms, min=0s, med=2.29ms, max=213.17ms, p(90)=16.03ms, p(95)=27.99ms | avg 약 32.5% 단축                      |
| **가상 사용자 수**            | 1000, min=1000, max=1000                                                                  | 1000, min=1000, max=1000                                                   | 동일                                   |

### 요약

1. **HTTP 요청 차단 및 연결 시간 단축**: 레디스 적용으로 차단 시간과 연결 시간이 각각 평균 약 28.83%와 29.27% 단축
2. **HTTP 요청 처리 및 대기 시간 단축**: 처리 시간과 대기 시간이 각각 평균 약 32.44%와 32.5% 단축됨
3. **전반적인 성능 개선**: 레디스 적용 후 HTTP 요청 성능 지표들이 전반적으로 개선되었으며, 시스템의 효율성과 안정성이 향상


### 참고
- [Redis 공식 문서](https://redis.io/documentation)
- [Redis Serializer](https://github.com/binghe819/TIL/blob/master/Spring/Redis/redis%20serializer/serializer.md)
- [(spring boot) Redis Key Event Notification 처리 방법](https://wildeveloperetrain.tistory.com/299)