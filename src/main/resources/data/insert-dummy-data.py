import pymysql
from datetime import datetime, timedelta
import random
import json
from tqdm import tqdm

# MySQL 연결 설정
connection = pymysql.connect(
    host='localhost',
    user='root',
    password='root',
    database='hhplus_concert_reservation',
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

places_with_seats = {
    "LG 아트센터": 110,
    "우리금융아트홀": 118,
    "쉐라톤그랜드워커힐 시어터": 120,
    "블루스퀘어 삼성카드홀": 138,
    "블루스퀘어 삼성전자홀": 176,
    "예스24라이브홀": 200,
    "세종문화회관 대극장": 382,
    "올림픽홀": 400,
    "경희대 평화의전당": 450,
    "코엑스 컨벤션홀 Hall D": 700,
    "SK핸드볼경기장": 700,
    "고려대 화정체육관": 605,
    "잠실 실내체육관": 110,
    "KSPO DOME": 150,
    "고척스카이돔": 220,
    "상암 월드컵 경기장": 668,
    "잠실올림픽주경기장": 543
}

# 더미 데이터 생성 함수
def generate_dummy_data():
    try:
        with connection.cursor() as cursor:
            # 테이블 데이터 초기화
            cursor.execute("TRUNCATE concert_reservation")
            cursor.execute("TRUNCATE payment")
            cursor.execute("TRUNCATE user_point")
            cursor.execute("TRUNCATE concert_seat")
            cursor.execute("TRUNCATE concert_session")
            cursor.execute("TRUNCATE concert")
            cursor.execute("TRUNCATE users")
    
            # 더미 데이터 삽입
            # 1. users 테이블에 데이터 삽입
            user_ids = []
            for i in tqdm(range(100000), desc="Users 삽입 중"):
                cursor.execute(
                    "INSERT INTO users (created_at, updated_at, email, name) VALUES (%s, %s, %s, %s)",
                    (datetime.now(), datetime.now(), f'user{i+1}@example.com', f'User {i+1}')
                )
                user_ids.append(cursor.lastrowid)

            # 2. user_point 테이블에 데이터 삽입
            for user_id in tqdm(user_ids, desc="User Points 삽입 중"):
                cursor.execute(
                    "INSERT INTO user_point (created_at, updated_at, amount, user_id) VALUES (%s, %s, %s, %s)",
                    (datetime.now(), datetime.now(), random.randint(100000, 200000), user_id)
                )

            # 3. concert 테이블에 데이터 삽입
            concert_ids = []
            concert_info = []
            for i in tqdm(range(10000), desc="Concerts 삽입 중"):
                days_offset = random.randint(-365, 30)
                reservation_open_at = datetime.now() + timedelta(days=days_offset)
                reservation_close_at = reservation_open_at + timedelta(days=random.randint(1, 30))
                place = random.choice(list(places_with_seats.keys()))

                cursor.execute(
                    "INSERT INTO concert (created_at, updated_at, name, place, reservation_open_at, reservation_close_at) VALUES (%s, %s, %s, %s, %s, %s)",
                    (
                        datetime.now(), datetime.now(),
                        f'Concert {i+1}',
                        place,
                        reservation_open_at,
                        reservation_close_at
                    )
                )
                concert_ids.append(cursor.lastrowid)
                concert_info.append({
                    'concert_id': cursor.lastrowid,
                    'reservation_close_at': reservation_close_at
                })
            
            connection.commit()
            
            # 4. concert_session 테이블에 데이터 삽입
            session_ids = []
            session_info = []

            # 각 concert의 정보 조회 (concert_id, reservation_close_at, place)
            cursor.execute("SELECT id, reservation_close_at, place FROM concert")
            concert_data = cursor.fetchall()

            for concert in tqdm(concert_data, desc="Concert Sessions 삽입 중"):
                concert_id = concert['id']
                reservation_close_at = concert['reservation_close_at']
                place = concert['place']
                total_seat_count = places_with_seats.get(place, 100)

                # 각 콘서트가 1~3개의 회차를 가지도록 설정
                num_sessions = random.randint(1, 3)
                for _ in range(num_sessions):
                    # concert_at은 reservation_close_at 이후 1주일 이내로 설정
                    concert_at = reservation_close_at + timedelta(days=random.randint(1, 7))
                    
                    cursor.execute(
                        "INSERT INTO concert_session (created_at, updated_at, concert_at, concert_id, total_seat_count) VALUES (%s, %s, %s, %s, %s)",
                        (
                            datetime.now(), datetime.now(),
                            concert_at, concert_id,
                            total_seat_count
                        )
                    )
                    session_ids.append(cursor.lastrowid)
                    session_info.append({
                        'session_id': cursor.lastrowid,
                        'total_seat_count': total_seat_count
                    })

                connection.commit()
            
            #5. concert_seat 테이블에 데이터 삽입
            # concert_session 테이블에서 세션 정보 가져오기
            seat_ids = []
            # 각 세션의 좌석 생성
            for session_info in tqdm(session_info, desc="Concert Seats 삽입 중"):
                session_id = session_info['session_id']
                total_seats = session_info['total_seat_count']

                for seat_number in range(1, total_seats + 1):
                    price = random.randint(100000, 150000)
                    status = random.choice(['TEMPORARY_RESERVED', 'CONFIRMED', 'AVAILABLE'])
                    
                    cursor.execute(
                        """
                        INSERT INTO concert_seat (created_at, updated_at, concert_session_id, price, seat_number, status, version)
                        VALUES (%s, %s, %s, %s, %s, %s, %s)
                        """,
                        (datetime.now(), datetime.now(), session_id, price, seat_number, status, 1)
                    )
                    seat_ids.append(cursor.lastrowid)

            connection.commit()
            
            # 6. concert_reservation 테이블에 데이터 삽입
            concert_reservation_ids = []
            concert_reservation_status = ['TEMPORARY_RESERVED', 'CONFIRMED', 'CANCELED']
            # 1부터 10000까지의 숫자 리스트 생성
            numbers = list(range(1, 10001))
            for _ in tqdm(range(100000), desc="Concert Reservations 삽입 중"):
                # 사용자가 예약한 콘서트 세션을 무작위로 선택
                session_id = random.choice(session_ids)
                concert_id = random.choice(concert_ids)
                user_id = random.choice(user_ids)
                reservation_close_at = datetime.now() - timedelta(days=random.randint(1, 365))
                # 1부터 10000까지 중에서 랜덤으로 4개 선택
                selected_seat_ids = random.sample(numbers, 4)
                cursor.execute(
                    "INSERT INTO concert_reservation (created_at, updated_at, concert_id, concert_session_id, reservation_at, seat_ids, status, total_price, user_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
                    (
                        datetime.now(), datetime.now(),
                        concert_id, session_id,
                        reservation_close_at,
                        json.dumps(selected_seat_ids),
                        random.choice(concert_reservation_status),
                        random.randint(100000, 150000),
                        user_id
                    )
                )
                concert_reservation_ids.append(cursor.lastrowid)
                connection.commit()

            # 7. payment 테이블에 데이터 삽입
            payment_status = ['PAID', 'CANCELED']
            for _ in tqdm(range(100000), desc="Payments 삽입 중"):
                reservation_id = random.choice(concert_reservation_ids)
                cursor.execute(
                    "INSERT INTO payment (created_at, updated_at, payment_at, total_price, reservation_id, user_id, status) VALUES (%s, %s, %s, %s, %s, %s, %s)",
                    (
                        datetime.now(), datetime.now(),
                        datetime.now() - timedelta(days=random.randint(1, 365)),
                        random.randint(100000, 150000),
                        reservation_id,
                        random.choice(user_ids),
                        random.choice(payment_status)
                    )
                )

            # 변경 사항 저장
            connection.commit()
            print("더미 데이터 삽입 완료")

    except Exception as e:
        print(f"에러 발생: {e}")
        connection.rollback()
    finally:
        connection.close()

# 함수 실행
generate_dummy_data()
