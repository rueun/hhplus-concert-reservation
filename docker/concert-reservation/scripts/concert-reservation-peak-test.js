import http from 'k6/http';
import { check, sleep } from 'k6';


const host = 'http://localhost:8080'; // 테스트할 서버 URL

// 부하 테스트 옵션 설정
export const options = {
    scenarios: {
        concert_booking: {
            executor: 'per-vu-iterations',  // 각 가상 사용자별로 반복 횟수 설정
            vus: 300,  // 300명의 가상 사용자
            iterations: 1  // 각 가상 사용자가 1번만 실행
        },
    },
};

// 콘서트 회차 조회 함수
function getConcertSession(queueToken) {
    const res = http.get(`${host}/api/v1/concerts/1/sessions`, {
        headers: {
            'accept': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Concert schedule retrieved': (r) => r.status === 200 });
    const sessions = JSON.parse(res.body).sessions;
    // sessions 배열에서 랜덤으로 하나의 세션을 선택하여 리턴
    const randomSession = sessions[Math.floor(Math.random() * sessions.length)];
    return randomSession.sessionId;
}

// 콘서트 좌석 조회 함수
function getAvailableSeat(sessionId, queueToken) {
    const res = http.get(`${host}/api/v1/concerts/1/sessions/${sessionId}/seats`, {
        headers: {
            'accept': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Seat availability retrieved': (r) => r.status === 200 });
    const availableSeats = JSON.parse(res.body).availableSeats;
    const randomCount = Math.floor(Math.random() * 2) + 1; // 1~2 사이의 랜덤 숫자
    const shuffledSeats = availableSeats.sort(() => 0.5 - Math.random()); // 좌석 배열 섞기
    const selectedSeats = shuffledSeats.slice(0, Math.min(randomCount, shuffledSeats.length)); // 가능한 개수만큼 선택
    return selectedSeats.map(seat => seat.seatId);
}

// 콘서트 예약 함수
function reserveSeat(userId, sessionId, seatIds, queueToken) {
    const res = http.post(`${host}/api/v1/concerts/1/sessions/${sessionId}/reservations`, JSON.stringify({
        userId: userId,
        seatIds: seatIds
    }), {
        headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Seat reserved successfully': (r) => r.status === 201 });
    return JSON.parse(res.body).reservationId;
}

// 예약 결제 함수
function makePayment(userId, reservationId, queueToken) {
    const res = http.post(`${host}/api/v1/payments`, JSON.stringify({
        userId: userId,
        reservationId: reservationId
    }), {
        headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json',
            'QUEUE-TOKEN': queueToken
        }
    });
    check(res, { 'Payment successful': (r) => r.status === 201 });
}

// VU 별로 실행되는 메인 시나리오
export default function () {
    const userId = `${__VU}`;
    const queueToken = `queue_token_${__VU}`; // VU별로 토큰을 순차적으로 생성

    console.log(`User ${userId} is trying to book a concert ticket.`);

    // 랜덤으로 쉬기 (1~3초)

    const sessionId = getConcertSession(queueToken); // 3. 콘서트 회차 조회

    sleep(3);

    const seatIds = getAvailableSeat(sessionId, queueToken); // 4. 콘서트 좌석 조회

    sleep(2);

    const reservationId = reserveSeat(userId, sessionId, seatIds, queueToken); // 5. 콘서트 예약

    sleep(2);

    makePayment(userId, reservationId, queueToken); // 6. 결제*!/*/
}
