package com.hhplus.concertreservation.queue.domain.exception;

public class WaitingQueueNotActivatedException extends WaitingQueueException {

    public WaitingQueueNotActivatedException() {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_ACTIVATED);
    }

    public WaitingQueueNotActivatedException(String message) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_ACTIVATED, message);
    }

    public WaitingQueueNotActivatedException(Throwable cause) {
        super(WaitingQueueErrorCode.WAITING_QUEUE_NOT_ACTIVATED, cause);
    }
}
