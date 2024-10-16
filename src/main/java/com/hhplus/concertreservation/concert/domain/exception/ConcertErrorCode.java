package com.hhplus.concertreservation.concert.domain.exception;

public enum ConcertErrorCode {

    CONCERT_NOT_FOUND(404, "CONCERT_001", "해당 콘서트를 찾을 수 없습니다"),
    RESERVATION_PERIOD_NOT_AVAILABLE(400, "CONCERT_002", "현재 예약 기간이 아닙니다."),

    CONCERT_SESSION_NOT_FOUND(404, "CONCERT_SESSION_001", "해당 콘서트 세션을 찾을 수 없습니다"),

    CONCERT_SEAT_NOT_FOUND(404, "CONCERT_SEAT_001", "해당 콘서트 좌석을 찾을 수 없습니다"),
    CONCERT_SEAT_UNAVAILABLE_FOR_RESERVATION(400, "CONCERT_SEAT_002", "예약 가능한 좌석이 아닙니다"),

    CONCERT_RESERVATION_NOT_FOUND(404, "CONCERT_RESERVATION_001", "해당 콘서트 예약을 찾을 수 없습니다");



    private final int status;
    private final String code;
    private final String message;

    ConcertErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
