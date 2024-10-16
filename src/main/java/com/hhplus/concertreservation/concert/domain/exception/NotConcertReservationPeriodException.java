package com.hhplus.concertreservation.concert.domain.exception;

public class NotConcertReservationPeriodException extends ConcertException {

    public NotConcertReservationPeriodException() {
        super(ConcertErrorCode.RESERVATION_PERIOD_NOT_AVAILABLE);
    }

    public NotConcertReservationPeriodException(String message) {
        super(ConcertErrorCode.RESERVATION_PERIOD_NOT_AVAILABLE, message);
    }

    public NotConcertReservationPeriodException(Throwable cause) {
        super(ConcertErrorCode.RESERVATION_PERIOD_NOT_AVAILABLE, cause);
    }
}
