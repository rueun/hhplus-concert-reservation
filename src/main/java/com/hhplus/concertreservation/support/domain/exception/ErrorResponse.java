package com.hhplus.concertreservation.support.domain.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
