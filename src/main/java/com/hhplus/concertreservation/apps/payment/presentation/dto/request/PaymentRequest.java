package com.hhplus.concertreservation.apps.payment.presentation.dto.request;

public record PaymentRequest(
        Long userId,
        Long reservationId
) {
}