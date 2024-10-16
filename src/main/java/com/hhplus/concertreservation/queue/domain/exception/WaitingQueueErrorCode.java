package com.hhplus.concertreservation.queue.domain.exception;

public enum WaitingQueueErrorCode {

    WAITING_QUEUE_NOT_FOUND(404, "WAITING_QUEUE_001", "해당 대기열 정보를 찾을 수 없습니다"),
    WAITING_QUEUE_EXPIRED(403, "WAITING_QUEUE_002", "대기열이 만료되었습니다"),
    WAITING_QUEUE_NOT_ACTIVATED(403, "WAITING_QUEUE_003", "대기열이 활성상태가 아닙니다");

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
