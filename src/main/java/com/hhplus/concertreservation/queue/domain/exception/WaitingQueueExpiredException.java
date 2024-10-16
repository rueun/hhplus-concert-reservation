package com.hhplus.concertreservation.queue.domain.exception;

public class WaitingQueueExpiredException extends WaitingQueueException {

    public WaitingQueueExpiredException() {
        super(WaitingQueueErrorCode.WAITING_QUEUE_EXPIRED);
    }

    public WaitingQueueExpiredException(String message) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_EXPIRED, message);
    }

    public WaitingQueueExpiredException(Throwable cause) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_EXPIRED, cause);
    }
}
