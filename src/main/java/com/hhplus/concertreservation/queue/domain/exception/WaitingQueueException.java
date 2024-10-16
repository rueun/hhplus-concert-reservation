package com.hhplus.concertreservation.queue.domain.exception;

import com.hhplus.concertreservation.common.exception.BusinessException;

public class WaitingQueueException extends BusinessException {
    public WaitingQueueException(WaitingQueueErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }

    public WaitingQueueException(WaitingQueueErrorCode errorCode, String message) {
        super(errorCode.getStatus(), errorCode.getCode(), message);
    }

    public WaitingQueueException(WaitingQueueErrorCode errorCode, Throwable cause) {
        super(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
        initCause(cause);
    }
}
