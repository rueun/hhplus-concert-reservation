package com.hhplus.concertreservation.queue.domain.exception;

public class WaitingQueueAlreadyActivatedException extends WaitingQueueException {

    public WaitingQueueAlreadyActivatedException() {
        super(WaitingQueueErrorCode.ALREADY_ACTIVATED);
    }

    public WaitingQueueAlreadyActivatedException(String message) {
        super(WaitingQueueErrorCode.ALREADY_ACTIVATED, message);
    }

    public WaitingQueueAlreadyActivatedException(Throwable cause) {
        super(WaitingQueueErrorCode.ALREADY_ACTIVATED, cause);
    }
}
