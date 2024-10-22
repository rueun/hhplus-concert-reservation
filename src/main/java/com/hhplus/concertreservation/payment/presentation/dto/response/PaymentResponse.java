package com.hhplus.concertreservation.payment.presentation.dto.response;

import com.hhplus.concertreservation.payment.domain.model.enums.PaymentStatus;
import com.hhplus.concertreservation.payment.domain.model.entity.Payment;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentResponse(
            @Schema(name = "결제 ID")
            Long paymentId,
            @Schema(name = "결제 금액")
            long paymentAmount,
            @Schema(name = "결제 상태")
            PaymentStatus status
    ) {
    public static PaymentResponse of(final Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getTotalPrice(), payment.getStatus());
    }
}