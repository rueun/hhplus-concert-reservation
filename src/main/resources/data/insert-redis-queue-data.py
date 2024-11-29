import redis
import random
import time

# Redis 서버와 연결
r = redis.StrictRedis(host='localhost', port=6379, db=0, decode_responses=True)

# 대기열 생성 및 저장
def create_queues():
    r.flushdb()

    for i in range(1, 501):  # 1부터 500까지
        # 토큰 값 생성
        token = f"queue_token_{i}"

        # active-queues에 토큰 추가
        r.sadd("active-queues", token)

        # token-meta:{token} 해시 생성
        r.hset(f"token-meta:{token}", "userId", i)  # {i} 대신 i를 직접 사용

        # TTL 10분 (600초) 설정
        r.expire(f"token-meta:{token}", 600)

        print(f"Queue {token} created and added to active-queues with")

# 대기열 생성 호출
create_queues()

# 출력 확인: active-queues에 저장된 토큰 확인
print("All tokens in active-queues:")
tokens = r.smembers("active-queues")
for token in tokens:
    print(token)
