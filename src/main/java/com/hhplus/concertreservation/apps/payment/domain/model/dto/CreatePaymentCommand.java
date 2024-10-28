package com.hhplus.concertreservation.apps.payment.domain.model.dto;

public record CreatePaymentCommand(
        Long userId,
        Long reservationId,
        Long totalPrice
) {
}
