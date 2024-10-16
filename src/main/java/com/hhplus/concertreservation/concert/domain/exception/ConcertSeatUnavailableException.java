package com.hhplus.concertreservation.concert.domain.exception;

public class ConcertSeatUnavailableException extends ConcertException {

    public ConcertSeatUnavailableException() {
        super(ConcertErrorCode.CONCERT_SEAT_UNAVAILABLE_FOR_RESERVATION);
    }

    public ConcertSeatUnavailableException(String message) {
        super(ConcertErrorCode.CONCERT_SEAT_UNAVAILABLE_FOR_RESERVATION, message);
    }

    public ConcertSeatUnavailableException(Throwable cause) {
        super(ConcertErrorCode.CONCERT_SEAT_UNAVAILABLE_FOR_RESERVATION, cause);
    }
}
