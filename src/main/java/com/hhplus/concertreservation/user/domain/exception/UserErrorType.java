package com.hhplus.concertreservation.user.domain.exception;

import com.hhplus.concertreservation.support.domain.exception.ErrorCode;
import com.hhplus.concertreservation.support.domain.exception.ErrorType;
import org.springframework.boot.logging.LogLevel;

public enum UserErrorType implements ErrorType {

    USER_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다", LogLevel.WARN),
    USER_POINT_NOT_FOUND(ErrorCode.NOT_FOUND, "포인트를 찾을 수 없습니다", LogLevel.WARN),
    USER_POINT_NOT_ENOUGH(ErrorCode.CLIENT_ERROR, "포인트가 부족합니다", LogLevel.WARN),
    POINT_AMOUNT_INVALID(ErrorCode.BUSINESS_ERROR, "충전/사용할 포인트 금액이 유효하지 않습니다", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    UserErrorType(ErrorCode code, String message, LogLevel logLevel) {
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
