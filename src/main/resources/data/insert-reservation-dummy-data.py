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
            cursor.execute("TRUNCATE concert")
            cursor.execute("TRUNCATE concert_session")
            cursor.execute("TRUNCATE concert_reservation")
    
            # 1부터 1500000까지의 숫자 리스트 생성
            seat_numbers = list(range(1, 1500000))
            
            for i in tqdm(range(100000), desc="Concert 데이터 삽입 중"):
                # 데이터 삽입
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

            # 500만 개의 최근 1년 이내 데이터 생성
            for i in tqdm(range(50000), desc="Concert Reservations (최근 1년 이내) 삽입 중"):
                session_id = random.randint(1, 100000)
                concert_id = random.randint(1, 100000)
                user_id = random.randint(1, 1000000)
                selected_seat_ids = random.sample(seat_numbers, 4)
                total_price = random.randint(100000, 150000)
                
                reservation_at = datetime.now() - timedelta(days=random.randint(1, 7))
                status = random.choice(['CONFIRMED', 'CANCELED'])
                
                # 데이터 삽입
                cursor.execute(
                    """
                    INSERT INTO concert_reservation 
                    (created_at, updated_at, concert_id, concert_session_id, reservation_at, seat_ids, status, total_price, user_id)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    """,
                    (
                        datetime.now(), datetime.now(),
                        concert_id, session_id,
                        reservation_at,
                        json.dumps(selected_seat_ids),
                        status,
                        total_price,
                        user_id
                    )
                )
                
            # 50만 개의 최근 10분 이내 데이터 생성
            for i in tqdm(range(50000), desc="Concert Reservations (최근 10분 이내) 삽입 중"):
                session_id = random.randint(1, 100000)
                concert_id = random.randint(1, 100000)
                user_id = random.randint(1, 1000000)
                selected_seat_ids = random.sample(seat_numbers, 4)
                total_price = random.randint(100000, 150000)
                
                reservation_at = datetime.now() - timedelta(seconds=random.randint(1, 600))
                status = 'TEMPORARY_RESERVED'
                
                # 데이터 삽입
                cursor.execute(
                    """
                    INSERT INTO concert_reservation 
                    (created_at, updated_at, concert_id, concert_session_id, reservation_at, seat_ids, status, total_price, user_id)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    """,
                    (
                        datetime.now(), datetime.now(),
                        concert_id, session_id,
                        reservation_at,
                        json.dumps(selected_seat_ids),
                        status,
                        total_price,
                        user_id
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
