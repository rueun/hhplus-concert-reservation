package com.hhplus.concertreservation.user.domain.exception;

public class UserPointNotFoundException extends UserException {

    public UserPointNotFoundException() {
        super(UserErrorCode.USER_POINT_NOT_FOUND);
    }

    public UserPointNotFoundException(String message) {
        super(UserErrorCode.USER_POINT_NOT_FOUND, message);
    }

    public UserPointNotFoundException(Throwable cause) {
        super(UserErrorCode.USER_POINT_NOT_FOUND, cause);
    }
}
