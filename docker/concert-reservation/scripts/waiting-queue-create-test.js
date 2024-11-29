import http from 'k6/http';
import { check, sleep } from 'k6';

const host = 'http://localhost:8080'; // 테스트할 서버 URL

export const options = {
    scenarios: {
        test: {
            executor: 'ramping-vus',
            startVUs: 10,  // 시작 VU 수
            stages: [
                { duration: '20s', target: 500 },
                { duration: '10s', target: 700 },
                { duration: '30s', target: 1000 },
            ],
        },
    },
};


// 대기열 생성 함수
function createQueue(userId) {
    const res = http.post(`${host}/api/v1/waiting-queues`, // 요청 URLd
        JSON.stringify({ userId: userId }), // 요청 바디에 랜덤 userId 포함
        {
            headers: { 'Content-Type': 'application/json' }, // JSON 요청 헤더
        }
    );
    check(res, { 'Queue created successfully': (r) => r.status === 201 });
    console.log(`Queue token: ${JSON.parse(res.body)}`);
    console.log(`User ${userId} is in the waiting queue.`);
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
    const queueToken = createQueue(userId); // 1. 대기열 생성
    let queueStatus;
    do {
        sleep(3); // 대기열 조회 간격
        queueStatus = checkQueue(queueToken); // 2. 대기열 조회
    } while (queueStatus !== "ACTIVATED"); // 대기열이 활성화 상태가 될 때까지 반복
}
