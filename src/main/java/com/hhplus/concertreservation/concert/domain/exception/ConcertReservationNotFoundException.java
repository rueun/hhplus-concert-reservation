package com.hhplus.concertreservation.concert.domain.exception;

public class ConcertReservationNotFoundException extends ConcertException {

    public ConcertReservationNotFoundException() {
        super(ConcertErrorCode.CONCERT_RESERVATION_NOT_FOUND);
    }

    public ConcertReservationNotFoundException(String message) {
        super(ConcertErrorCode.CONCERT_RESERVATION_NOT_FOUND, message);
    }

    public ConcertReservationNotFoundException(Throwable cause) {
        super(ConcertErrorCode.CONCERT_RESERVATION_NOT_FOUND, cause);
    }
}
