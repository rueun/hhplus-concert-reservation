package com.hhplus.concertreservation.user.domain.exception;

public enum UserErrorCode {

    USER_NOT_FOUND(404, "USER_001", "해당 유저를 찾을 수 없습니다");

    private final int status;
    private final String code;
    private final String message;

    UserErrorCode(int status, String code, String message) {
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
