package com.hhplus.concertreservation.user.domain.exception;

public class UserPointNotEnoughException extends UserException {

    public UserPointNotEnoughException() {
        super(UserErrorCode.USER_POINT_NOT_ENOUGH);
    }

    public UserPointNotEnoughException(String message) {
        super(UserErrorCode.USER_POINT_NOT_ENOUGH, message);
    }

    public UserPointNotEnoughException(Throwable cause) {
        super(UserErrorCode.USER_POINT_NOT_ENOUGH, cause);
    }
}
