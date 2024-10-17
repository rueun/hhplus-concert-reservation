package com.hhplus.concertreservation.payment.domain.model.dto;

public record CreatePaymentCommand(
        Long userId,
        Long reservationId,
        Long totalPrice
) {
}
