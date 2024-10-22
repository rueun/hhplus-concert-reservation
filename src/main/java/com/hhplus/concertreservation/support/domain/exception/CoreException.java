package com.hhplus.concertreservation.support.domain.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {
    private final ErrorType errorType;
    private final String errorCode;
    private final String message;

    public CoreException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.errorCode = errorType.getSubErrorCode();
        this.message = errorType.getMessage();
    }

    public CoreException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.errorCode = errorType.getSubErrorCode();
        this.message = message;
    }
}