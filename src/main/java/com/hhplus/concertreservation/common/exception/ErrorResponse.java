package com.hhplus.concertreservation.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
