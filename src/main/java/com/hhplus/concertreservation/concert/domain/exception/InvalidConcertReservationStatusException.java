package com.hhplus.concertreservation.concert.domain.exception;

public class InvalidConcertReservationStatusException extends ConcertException {

    public InvalidConcertReservationStatusException() {
        super(ConcertErrorCode.INVALID_CONCERT_RESERVATION_STATUS);
    }

    public InvalidConcertReservationStatusException(String message) {
        super(ConcertErrorCode.INVALID_CONCERT_RESERVATION_STATUS, message);
    }

    public InvalidConcertReservationStatusException(Throwable cause) {
        super(ConcertErrorCode.INVALID_CONCERT_RESERVATION_STATUS, cause);
    }
}
