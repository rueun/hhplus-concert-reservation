package com.hhplus.concertreservation.support.domain.exception;

import org.springframework.boot.logging.LogLevel;

public interface ErrorType {
    ErrorCode getCode();
    String getMessage();
    LogLevel getLogLevel();
    String getSubErrorCode();
}
