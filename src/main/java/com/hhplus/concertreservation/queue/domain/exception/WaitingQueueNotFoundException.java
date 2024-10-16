package com.hhplus.concertreservation.queue.domain.exception;

public class WaitingQueueNotFoundException extends WaitingQueueException {

    public WaitingQueueNotFoundException() {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_FOUND);
    }

    public WaitingQueueNotFoundException(String message) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_FOUND, message);
    }

    public WaitingQueueNotFoundException(Throwable cause) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_FOUND, cause);
    }
}
