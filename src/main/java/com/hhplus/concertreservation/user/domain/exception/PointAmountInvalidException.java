package com.hhplus.concertreservation.user.domain.exception;

public class PointAmountInvalidException extends UserException {

    public PointAmountInvalidException() {
        super(UserErrorCode.POINT_AMOUNT_INVALID);
    }

    public PointAmountInvalidException(String message) {
        super(UserErrorCode.POINT_AMOUNT_INVALID, message);
    }

    public PointAmountInvalidException(Throwable cause) {
        super(UserErrorCode.POINT_AMOUNT_INVALID, cause);
    }
}
