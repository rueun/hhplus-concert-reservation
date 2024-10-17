package com.hhplus.concertreservation.payment.presentation.dto.response;

import com.hhplus.concertreservation.payment.domain.model.vo.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentResponse(
            @Schema(name = "결제 ID")
            Long paymentId,
            @Schema(name = "결제 금액")
            int paymentAmount,
            @Schema(name = "결제 상태")
            PaymentStatus status
    ) {
    }