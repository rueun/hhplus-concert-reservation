package com.hhplus.concertreservation.concert.domain.exception;

public class ConcertNotFoundException extends ConcertException {

    public ConcertNotFoundException() {
        super(ConcertErrorCode.CONCERT_NOT_FOUND);
    }

    public ConcertNotFoundException(String message) {
        super(ConcertErrorCode.CONCERT_NOT_FOUND, message);
    }

    public ConcertNotFoundException(Throwable cause) {
        super(ConcertErrorCode.CONCERT_NOT_FOUND, cause);
    }
}
