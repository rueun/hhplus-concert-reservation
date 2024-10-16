package com.hhplus.concertreservation.user.domain.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(UserErrorCode.USER_NOT_FOUND, message);
    }

    public UserNotFoundException(Throwable cause) {
        super(UserErrorCode.USER_NOT_FOUND, cause);
    }
}
