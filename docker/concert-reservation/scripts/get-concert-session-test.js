import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 500, // 초당 동시 요청 수
    duration: '30s', // 테스트 지속 시간
};

export default function () {
    const url = 'http://localhost:8080/api/v1/concerts/4/sessions'; // 실제 API URL

    const headers = {
        'Content-Type': 'application/json',
        'QUEUE-TOKEN': 'token1'
    };

    const response = http.get(url, { headers: headers });

    check(response, {
        'is status 200': (r) => r.status === 200,
    });

    sleep(1); // 각 요청 사이에 1초 대기
}