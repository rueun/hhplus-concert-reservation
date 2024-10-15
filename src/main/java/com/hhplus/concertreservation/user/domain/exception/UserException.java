package com.hhplus.concertreservation.user.domain.exception;

import com.hhplus.concertreservation.common.exception.BusinessException;

public class UserException extends BusinessException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }

    public UserException(UserErrorCode errorCode, String message) {
        super(errorCode.getStatus(), errorCode.getCode(), message);
    }

    public UserException(UserErrorCode errorCode, Throwable cause) {
        super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
        initCause(cause);
    }
}
