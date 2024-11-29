import http from 'k6/http';
import { check, sleep } from 'k6';

const host = 'http://localhost:8080'; // 테스트할 서버 URL

export const options = {
    scenarios: {
        load_test: {
            executor: 'ramping-vus', // 가상 사용자 수 점진적 증가 및 감소
            startVUs: 100, // 테스트 시작 시 100명의 사용자로 시작
            stages: [
                { duration: '1s', target: 1000 },
                { duration: '3s', target: 3000 },
                { duration: '3s', target: 3000 },
                { duration: '3s', target: 7000 },
                { duration: '3s', target: 10000 },
                { duration: '5s', target: 7000 },
                { duration: '5s', target: 3000 },
                { duration: '5s', target: 1000 },
                { duration: '1s', target: 0 },
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
