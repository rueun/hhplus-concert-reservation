# DB Index 를 적용하여 Query 성능 개선하기

---

## 목차
* [인덱스 조사 내용](#인덱스-조사-내용)
  + [인덱스란?](#인덱스란)
  + [인덱스의 장/단점](#인덱스의-장단점)
  + [인덱스 사용이 적합한 경우](#인덱스-사용이-적합한-경우)
  + [카디널리티란?](#카디널리티란)
  + [단일 컬럼 인덱스와 복합 컬럼 인덱스](#단일-컬럼-인덱스와-복합-컬럼-인덱스)
  + [인덱스 자료구조](#인덱스-자료구조)
  + [인덱스를 이용한 조회 유의 사항](#인덱스를-이용한-조회-유의-사항)
  + [커버링 인덱스란?](#커버링-인덱스란)
  + [클러스터드 인덱스와 넌클러스터드 인덱스](#클러스터드-인덱스와-넌클러스터드-인덱스)
* [서비스 주요 조회 쿼리 - 쿼리 성능 개선을 위한 인덱스 적용 및 분석](#서비스-주요-조회-쿼리---쿼리-성능-개선을-위한-인덱스-적용-및-분석)
* [예상 시나리오 - 쿼리 성능 개선을 위한 인덱스 적용 및 분석](#예상-시나리오---쿼리-성능-개선을-위한-인덱스-적용-및-분석)
* [참고](#참고)

---
## 인덱스 조사 내용

### 인덱스란?
- 인덱스(index)는 추가적인 쓰기 작업과 저장 공간을 활용하여 데이터베이스 테이블에 저장된 데이터의 검색 속도를 향상시키기 위한 자료구조이다.
- 데이터베이스 내의 특정 컬럼(열)이나 컬럼들의 조합에 대한 값과 해당 값이 저장된 레코드(행)의 위치를 매핑하여 데이터베이스 쿼리의 성능을 최적화하는 데 중요한 역할을 한다.
- 데이터를 검색할 때 전체 테이블을 스캔하는 것이 아니라, 인덱스를 사용하여 검색 대상 레코드의 범위를 줄일 수 있다. 이는 대량의 데이터를 다루는 경우 데이터 검색 속도를 크게 향상시킨다.

### 인덱스의 장/단점
- **장점**
  - 검색 속도와 그에 따른 성능을 향상시킬 수 있다.
  - 테이블 Full-Scan 을 방지할 수 있다.
- **단점**
  - 생성/수정/삭제가 빈번한 테이블에 인덱스를 걸면 인덱스 크기가 비대해져 오히려 성능이 저하될 수 있다.
  - 인덱스를 재정렬하는 작업이 필요하다.
  - 인덱스를 위한 추가적인 저장공간이 필요하다. (DB의 약 10%)


### 인덱스 사용이 적합한 경우
- 규모가 작지 않은 테이블
- INSERT / UPDATE / DELETE 가 자주 발생하지 않는 컬럼
- JOIN / WHERE / ORDER BY 에 자주 사용되는 컬럼
- 데이터의 중복도가 낮은 컬럼 (카디널리티가 높은 컬럼)


### 카디널리티란?
- 카디널리티(Cardinality)는 인덱스에 저장된 유일한 값의 수를 의미한다.
- 카디널리티가 높을수록 인덱스의 효율이 높아진다.
- 카디널리티가 높은 컬럼 예시
  - 주민등록번호, 학번, 주문번호, 계좌번호 등
- 카디널리티가 낮은 컬럼 예시
  - 성별, 결제수단, 학년 등


### 단일 컬럼 인덱스와 복합 컬럼 인덱스
- **단일 컬럼 인덱스**
  - 하나의 컬럼에 대한 인덱스
  - 단일 컬럼 인덱스는 해당 컬럼에 대한 검색을 빠르게 할 수 있지만, 여러 컬럼을 조합한 검색에는 적합하지 않다.
  - 해당 컬럼의 카디널리티가 높은 것을 선택해야 한다.
- **복합 컬럼 인덱스**
  - 두 개 이상의 컬럼에 대한 인덱스
  - 복합 컬럼 인덱스는 여러 컬럼을 조합한 검색에 적합하다.
  - 카디널리티가 높은 순서에서 낮은 순서로 인덱스를 생성하는 것이 좋다.
  - 반드시 첫 번째 컬럼은 조회 조건에 포함되어야 인덱스를 탈 수 있다.


### 인덱스 자료구조
- **Hash Table**
  - 해시 테이블을 사용하여 데이터를 저장하는 인덱스 구조
  - 키 값에 대한 데이터의 위치를 직접 찾아내기 때문에 검색 속도가 매우 빠르다. (O(1))
  - 등호(=) 연산에만 특화되어 있어, 키 값에 대한 정렬이나 범위 검색을 지원하지 않는다.
- **B-Tree**
  - 가장 일반적으로 사용되는 인덱스 구조
  - 데이터베이스의 대부분에서 사용되는 인덱스 구조
  - 테이블의 모든 레코드를 정렬된 순서로 유지하며, 이진 트리의 노드에는 키 값과 해당 키 값이 위치한 레코드의 주소를 저장한다.
- **B+Tree**
  - B-Tree 인덱스의 변형으로, B-Tree 인덱스의 단점을 보완한 구조
  - B-Tree 인덱스와 달리 리프 노드만이 데이터를 가지고 있으며, 리프 노드는 연결 리스트로 연결되어 있다.
  - B+Tree 인덱스는 범위 검색에 특화되어 있으며, 범위 검색을 위한 블록 접근이 빈번한 경우에 사용된다.


### 인덱스를 이용한 조회 유의 사항
1. 인덱스 컬럼의 값과 타입을 그대로 사용해야 한다.
2. `LIKE`, `BETWEEN`, `<`, `>` 등의 범위 조건을 사용하면 해당 컬럼까지만 인덱스를 사용하고 이후의 컬럼은 인덱스를 사용하지 않는다.
3. AND 조건은 ROW 를 줄이지만 OR 는 비교를 위해 ROW 를 늘리므로 Full-Scan 발생확률이 높아진다.
4. `=`, `IN` 은 다음 컬럼에도 인덱스를 사용할 수 있다.


### 커버링 인덱스란?
- 일반적으로 인덱스를 설계한다면 WHERE 절에 대한 인덱스를 생각하지만, 실제로는 쿼리 전체에 대한 인덱스 설계가 필요하다. 
- 인덱스는 데이터를 효율적으로 찾는 방법이지만, 이를 잘 활용한다면 실제 데이터까지 접근하지 않고도 데이터를 찾아올 수 있다.
- 쿼리를 충족시키는데 필요한 모든 데이터를 가지고 있는 인덱스를 커버링 인덱스(Covering Index)라 한다.


### 클러스터드 인덱스와 넌클러스터드 인덱스
- **클러스터드 인덱스(Clustered Index)**
  - 테이블의 데이터를 정렬된 상태로 저장하는 인덱스
  - 테이블당 하나의 클러스터드 인덱스만 생성할 수 있다. 보통 기본키에 대해 생성된다.
  - 클러스터드 인덱스는 테이블의 물리적인 순서를 변경하므로, 테이블의 데이터를 정렬된 상태로 저장하고, 이를 이용하여 데이터를 검색할 때 테이블의 데이터를 읽지 않고 인덱스만으로도 데이터를 찾아올 수 있다.
- **넌클러스터드 인덱스(Non-Clustered Index)**
  - 테이블의 데이터를 정렬된 상태로 저장하지 않는 인덱스
  - 보통 개발자가 생성하는 인덱스는 넌클러스터드 인덱스이다.
  - 테이블당 여러 개의 넌클러스터드 인덱스를 생성할 수 있다.
  - 넌클러스터드 인덱스는 테이블의 데이터를 정렬된 상태로 저장하지 않으므로, 인덱스를 이용하여 데이터를 검색할 때는 인덱스를 이용하여 데이터를 찾은 후, 해당 데이터의 위치를 찾아서 데이터를 읽어야한다.

### 실행 계획(EXPLAIN)
- 쿼리의 맨 앞에 EXPLAIN 을 붙여 실행하면, 상세한 실행 계획을 확인할 수 있다.
```sql
EXPLAIN SELECT * FROM concert WHERE date = '2022-12-25'
```
- 실행 계획 컬럼
  - id : 쿼리의 순서
  - select_type : 쿼리의 유형
  - table : 쿼리에서 사용된 테이블
  - type : 테이블 접근 방법
    - system : 시스템 테이블
    - const : 상수 테이블
    - ref : 인덱스를 사용하여 레코드를 읽는 경우
    - range : 인덱스를 사용하여 범위를 읽는 경우
    - index : 인덱스를 사용하여 테이블을 읽는 경우
    - all : 테이블 전체를 읽는 경우
  - possible_keys : 사용 가능한 인덱스
  - key : 실제 사용된 인덱스
  - key_len : 사용된 인덱스의 길이
  - ref : 인덱스를 참조하는 테이블의 컬럼
  - rows : 쿼리 결과로 반환되는 레코드 수
  - Extra : 추가 정보
    - Using index : 커버링 인덱스 사용
    - Using where : WHERE 절로 필터링한 경우
    - Using temporary : 임시 테이블 사용
    - Using filesort : 데이터를 정렬하는 경우
  - filtered : 필터링된 레코드의 비율

---

## 서비스 주요 조회 쿼리 - 쿼리 성능 개선을 위한 인덱스 적용 및 분석
- `User getById(Long userId);`
  - 설명: 특정 유저의 정보를 조회한다.
  - 쿼리: `SELECT * FROM user WHERE id = ?`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- `UserPoint getByUserId(Long userId);`
  - 설명: 특정 유저의 포인트 정보를 조회한다.
  - 테스트 데이터 건수: 100만건
  - 쿼리: `SELECT * FROM user_point WHERE user_id = ?`
  - 인덱스 추가 전
    ```
       실행 시간: [2024-11-14 02:07:43] 1 row retrieved starting from 1 in 295 ms (execution: 281 ms, fetching: 14 ms)
    ```
    | id | select\_type | table       | partitions | type | possible\_keys | key | key\_len | ref | rows  | filtered | Extra |
        | :--- |:------------| :--- | :--- | :--- | :--- | :--- | :--- |:------|:---------| :--- | :--- |
    | 1 | SIMPLE | user\_point | null | ALL | null | null | null | null | 996442 | 10       | Using where |  

  - 인덱스 추가 후
    - 인덱스 생성: `CREATE INDEX idx_user_point_user_id ON user_point(user_id);`
    ```
      실행 시간: [2024-11-14 02:09:29] 1 row retrieved starting from 1 in 21 ms (execution: 3 ms, fetching: 18 ms)
    ```
    | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
    | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
    | 1 | SIMPLE | user\_point | null | ref | idx\_user\_point\_user\_id | idx\_user\_point\_user\_id | 9 | const | 1 | 100 | null |
  
  - 결론: 인덱스를 적용하여 성능이 향상되었다. (295ms -> 21ms, 14배 이상 성능 향상)
    - user_id 컬럼에 인덱스를 적용하여 조회 성능을 향상시켰다.
    - user_id 컬럼은 카디널리티가 높은 컬럼이므로 인덱스를 적용하여 성능을 향상시킬 수 있다.
    - 큰 성능 향상이 보이지 않는 이유는 user_id 컬럼이 보통 선형적으로 증가하는 값으로 저장될 가능성이 높기 때문이다.

<br>

- Concert getConcertById(Long concertId);
  - 설명: 특정 콘서트의 정보를 조회한다.
  - 쿼리: `SELECT * FROM concert WHERE id = ?`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- ConcertSession getConcertSessionById(Long concertSessionId);
  - 설명: 특정 콘서트 회차의 정보를 조회한다.
  - 쿼리: `SELECT * FROM concert_session WHERE id = ?`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- ConcertSeat getConcertSeatById(Long concertSeatId);
  - 설명: 특정 콘서트 좌석의 정보를 조회한다.
  - 쿼리: `SELECT * FROM concert_seat WHERE id = ?`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- ConcertReservation getConcertReservationById(Long concertReservationId);
  - 설명: 특정 콘서트 예약 정보를 조회한다.
  - 쿼리: `SELECT * FROM concert_reservation WHERE id = ?`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- List<ConcertSeat> getConcertSeatsByIds(List<Long> seatIds);
  - 설명 : 특정 콘서트 좌석 목록을 조회한다.
  - 쿼리: `SELECT * FROM concert_seat WHERE id IN (?)`
  - 기타: 이미 PK 인덱스가 적용되어 있으므로 추가 인덱스 적용 불필요

<br>

- List<ConcertReservation> getTemporaryReservationsToBeExpired(int minutes);
  - 설명: 일정 시간 이내에 만료될 예약 정보를 조회한다.
  - 테스트 데이터 건수: 5백만건
  - 쿼리: `SELECT * FROM concert_reservation WHERE status = 'TEMPORARY_RESERVED' AND reservation_at < ?`
  - 인덱스 적용 전
     ```
        실행 시간: [2024-11-14 02:27:26] 500 rows retrieved starting from 1 in 1 s 443 ms (execution: 1 s 422 ms, fetching: 21 ms)
     ```
      | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
      | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
      | 1 | SIMPLE | concert\_reservation | null | ALL | null | null | null | null | 5383398 | 3.33 | Using where |

  - 인덱스 적용 후
    - 인덱스 생성: `CREATE INDEX idx_concert_reservation_status_reservation_at ON concert_reservation(status, reservation_at);`
    ```
      실행 시간: [2024-11-14 02:28:17] 500 rows retrieved starting from 1 in 32 ms (execution: 4 ms, fetching: 28 ms)
    ```
    | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
    | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
    | 1 | SIMPLE | concert\_reservation | null | range | idx\_concert\_reservation\_status\_reservation\_at | idx\_concert\_reservation\_status\_reservation\_at | 1032 | null | 1024 | 100 | Using index condition |

  - 결론: 인덱스를 적용하여 성능이 향상되었다. (1s 443ms -> 32ms, 44배 이상 성능 향상)
    - status 컬럼과 reservation_at 컬럼을 조합한 복합 인덱스를 생성하여 조회 성능을 향상시켰다.
    - status 컬럼은 카디널리티가 낮은 컬럼이지만, 처음 조회 시 WHERE 절로 필터링되는 컬럼이므로 인덱스를 적용하여 성능을 향상시킬 수 있다.
    - status 단일 인덱스로도 성능 향상이 가능할 것이라 판단된다.


<br>

  
## 예상 시나리오 - 쿼리 성능 개선을 위한 인덱스 적용 및 분석

---
- 인기 콘서트 TOP 10 조회
  - 설명: 인기 콘서트 TOP 10을 조회한다.
    - 최근 일주일 간 예약 및 결제 건수가 많은 콘서트를 기준으로 인기 콘서트를 선정한다.
  - 테스트 데이터 건수: 500만건
    - 쿼리
      ```
        SELECT c.id AS concert_id,
           c.name AS concert_name,
           c.place AS concert_place, COUNT(cr.id) AS reservation_count
        FROM concert_reservation cr JOIN concert c on c.id = cr.concert_id
        WHERE status in ('TEMPORARY_RESERVED', 'CONFIRMED') AND reservation_at >= NOW() - INTERVAL 7 DAY
        GROUP BY concert_id
        ORDER BY reservation_count DESC
        LIMIT 10;
      ```
  - 인덱스 적용 전
    ```
       실행 시간 : [2024-11-14 02:39:30] 10 rows retrieved starting from 1 in 7 s 711 ms (execution: 7 s 691 ms, fetching: 20 ms)
    ```
    | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
    | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
    | 1 | SIMPLE | cr | null | ALL | null | null | null | null | 5383398 | 6.67 | Using where; Using temporary; Using filesort |
    | 1 | SIMPLE | c | null | eq\_ref | PRIMARY | PRIMARY | 8 | hhplus\_concert\_reservation.cr.concert\_id | 1 | 100 | null |

  - 인덱스 적용 후
    - 인덱스 생성: `CREATE INDEX idx_concert_reservation_status_concert_id_reservation_at ON concert_reservation(status, concert_id, reservation_at);`
    ```
      실행 시간: [2024-11-14 02:42:29] 10 rows retrieved starting from 1 in 3 s 611 ms (execution: 3 s 588 ms, fetching: 23 ms)
    ```
    | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
    | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
    | 1 | SIMPLE | cr | null | index | idx\_concert\_reservation\_status\_concert\_id\_reservation\_at | idx\_concert\_reservation\_status\_concert\_id\_reservation\_at | 1041 | null | 5383398 | 23.09 | Using where; Using index; Using temporary; Using filesort |
    | 1 | SIMPLE | c | null | eq\_ref | PRIMARY | PRIMARY | 8 | hhplus\_concert\_reservation.cr.concert\_id | 1 | 100 | null |

  - 한 번 더 개선 가능한 부분
    - concert 테이블과 조인하지 않고, 인기 콘서트 TOP 10을 조회할 수 있는 인덱스를 생성할 수 있다.
    - 각 콘서트의 정보는 concert 테이블에서 따로 조회하는 것이 더 효율적일 수 있다. (Top 10 개의 콘서트 정보만 조회하면 되므로)
    - 개선된 쿼리
        ```
            SELECT concert_id, COUNT(cr.id) AS reservation_count
            FROM concert_reservation
            WHERE status in ('TEMPORARY_RESERVED', 'CONFIRMED') AND reservation_at >= NOW() - INTERVAL 7 DAY
            GROUP BY concert_id
            ORDER BY reservation_count DESC
            LIMIT 10;
        ```
      
        ```
            실행 시간: [2024-11-14 02:47:44] 10 rows retrieved starting from 1 in 1 s 582 ms (execution: 1 s 571 ms, fetching: 11 ms)
        ```

        | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
        | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
        | 1 | SIMPLE | concert\_reservation | null | index | idx\_concert\_reservation\_status\_concert\_id\_reservation\_at,idx\_concert\_reservation\_status | idx\_concert\_reservation\_status\_concert\_id\_reservation\_at | 1041 | null | 5383398 | 23.09 | Using where; Using index; Using temporary; Using filesort |
    
    - 결론: 인덱스를 적용하여 성능이 향상되었다. (7s 711ms -> 3s 611ms -> 1s 582ms, 4배 이상 성능 향상)
      - status, concert_id, reservation_at 컬럼을 조합한 복합 인덱스를 생성하여 조회 성능을 향상시켰다.
      - status 컬럼은 카디널리티가 낮은 컬럼이지만, 처음 조회 시 WHERE 절로 필터링되는 컬럼이므로 인덱스를 적용하여 성능을 향상시킬 수 있다.
      - concert_id 컬럼은 카디널리티가 높은 컬럼이므로 인덱스를 적용하여 성능을 향상시킬 수 있다.
      - reservation_at 컬럼은 범위 검색이 필요한 컬럼이므로 인덱스를 적용하여 성능을 향상시킬 수 있다.
      - 인덱스를 적용하여 성능이 향상되었지만, 더 개선할 수 있는 부분이 있음을 확인하였다.
      - concert 테이블과 조인하지 않고, 인기 콘서트 TOP 10을 조회할 수 있는 인덱스를 생성하여 성능을 더 향상시킬 수 있다.
        - 그럼에도 불구하고 1초 미만으로 나오지 않는 이유는, 대량의 데이터를 처리하기 때문이다.
          - 인덱스를 적용하여 성능을 향상시켰지만, 여전히 대량의 데이터를 처리하는데 시간이 소요된다.
          - 대량의 데이터를 처리하는 경우, 쿼리 최적화 및 인덱스 최적화 외에도 쿼리 성능을 향상시키기 위한 다양한 방법을 고려해야 한다.


<br>

## 참고

---
- [[Database] 인덱스(index)란?](https://mangkyu.tistory.com/96)
- [DB 인덱스와 커버링 인덱스](https://velog.io/@789456jang/DB-%EC%9D%B8%EB%8D%B1%EC%8A%A4%EC%99%80-%EC%BB%A4%EB%B2%84%EB%A7%81-%EC%9D%B8%EB%8D%B1%EC%8A%A4)













