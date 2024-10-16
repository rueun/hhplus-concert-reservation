package com.hhplus.concertreservation.queue.domain.exception;

public enum WaitingQueueErrorCode {

    WAITING_QUEUE_NOT_FOUND(404, "WAITING_QUEUE_001", "해당 대기열 정보를 찾을 수 없습니다");

    private final int status;
    private final String code;
    private final String message;

    WaitingQueueErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
