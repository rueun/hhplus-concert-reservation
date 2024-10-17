package com.hhplus.concertreservation.concert.domain.exception;

public class ConcertSesstionNotFoundException extends ConcertException {

    public ConcertSesstionNotFoundException() {
        super(ConcertErrorCode.CONCERT_SESSION_NOT_FOUND);
    }

    public ConcertSesstionNotFoundException(String message) {
        super(ConcertErrorCode.CONCERT_SESSION_NOT_FOUND, message);
    }

    public ConcertSesstionNotFoundException(Throwable cause) {
        super(ConcertErrorCode.CONCERT_SESSION_NOT_FOUND, cause);
    }
}
