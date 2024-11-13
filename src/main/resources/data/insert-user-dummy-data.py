import pymysql
from datetime import datetime, timedelta
import random
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


# 더미 데이터 생성 함수
def generate_dummy_data():
    try:
        with connection.cursor() as cursor:
            # 테이블 데이터 초기화
            cursor.execute("TRUNCATE user_point")
            cursor.execute("TRUNCATE users")
    
            # 더미 데이터 삽입
            # 1. users 테이블에 데이터 삽입
            user_ids = []
            for i in tqdm(range(1000000), desc="Users 삽입 중"):
                cursor.execute(
                    "INSERT INTO users (created_at, updated_at, email, name) VALUES (%s, %s, %s, %s)",
                    (datetime.now(), datetime.now(), f'user{i+1}@example.com', f'User {i+1}')
                )
                user_ids.append(cursor.lastrowid)

            # 2. user_point 테이블에 데이터 삽입
            for user_id in tqdm(user_ids, desc="User Points 삽입 중"):
                cursor.execute(
                    "INSERT INTO user_point (created_at, updated_at, amount, user_id) VALUES (%s, %s, %s, %s)",
                    (datetime.now(), datetime.now(), random.randint(100000, 400000), user_id)
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
