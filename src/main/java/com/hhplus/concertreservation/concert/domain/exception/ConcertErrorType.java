package com.hhplus.concertreservation.concert.domain.exception;

import com.hhplus.concertreservation.support.domain.exception.ErrorCode;
import com.hhplus.concertreservation.support.domain.exception.ErrorType;
import org.springframework.boot.logging.LogLevel;

public enum ConcertErrorType implements ErrorType {

    CONCERT_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 콘서트를 찾을 수 없습니다", LogLevel.WARN),
    RESERVATION_PERIOD_NOT_AVAILABLE(ErrorCode.BUSINESS_ERROR, "현재 예약 기간이 아닙니다.", LogLevel.WARN),

    CONCERT_SESSION_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 콘서트 세션을 찾을 수 없습니다", LogLevel.WARN),

    CONCERT_SEAT_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 콘서트 좌석을 찾을 수 없습니다", LogLevel.WARN),
    CONCERT_SEAT_UNAVAILABLE(ErrorCode.BUSINESS_ERROR, "예약 가능한 좌석이 아닙니다", LogLevel.WARN),

    CONCERT_RESERVATION_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 콘서트 예약을 찾을 수 없습니다", LogLevel.WARN),
    INVALID_CONCERT_RESERVATION_STATUS(ErrorCode.BUSINESS_ERROR, "올바르지 않은 콘서트 예약 상태입니다", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ConcertErrorType(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    @Override
    public ErrorCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public LogLevel getLogLevel() {
        return logLevel;
    }

}
