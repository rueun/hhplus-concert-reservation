import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 500, // 동시 사용자 수
    duration: '10s', // 테스트 지속 시간
};

export default function () {
    const url = 'http://localhost:8080/api/v1/waiting-queues'; // 실제 API URL
    const payload = JSON.stringify({
        userId: Math.floor(Math.random() * 10) + 1 // 1부터 100까지의 임의의 userId 생성
    });

    const headers = {
        'Content-Type': 'application/json'
    };

    const response = http.post(url, payload, { headers: headers });

    check(response, {
        'is status 201': (r) => r.status === 201,
    });

    sleep(1); // 각 요청 사이에 1초 대기
}
