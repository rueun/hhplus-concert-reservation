package com.hhplus.concertreservation.concert.domain.exception;

public class ConcertSeatNotFoundException extends ConcertException {

    public ConcertSeatNotFoundException() {
        super(ConcertErrorCode.CONCERT_SEAT_NOT_FOUND);
    }

    public ConcertSeatNotFoundException(String message) {
        super(ConcertErrorCode.CONCERT_SEAT_NOT_FOUND, message);
    }

    public ConcertSeatNotFoundException(Throwable cause) {
        super(ConcertErrorCode.CONCERT_SEAT_NOT_FOUND, cause);
    }
}
