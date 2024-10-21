package com.hhplus.concertreservation.queue.domain.exception;

import com.hhplus.concertreservation.support.domain.exception.ErrorCode;
import com.hhplus.concertreservation.support.domain.exception.ErrorType;
import org.springframework.boot.logging.LogLevel;

public enum WaitingQueueErrorType implements ErrorType {

    WAITING_QUEUE_NOT_FOUND(ErrorCode.AUTHORIZATION_ERROR, "해당 대기열 정보를 찾을 수 없습니다", LogLevel.WARN),
    WAITING_QUEUE_EXPIRED(ErrorCode.AUTHORIZATION_ERROR, "대기열이 만료되었습니다", LogLevel.WARN),
    WAITING_QUEUE_NOT_ACTIVATED(ErrorCode.AUTHORIZATION_ERROR, "대기열이 활성상태가 아닙니다", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    WaitingQueueErrorType(ErrorCode code, String message, LogLevel logLevel) {
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
