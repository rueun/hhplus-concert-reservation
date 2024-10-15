package com.hhplus.concertreservation.payment.presentation.dto.response;

import com.hhplus.concertreservation.payment.domain.model.vo.PaymentStatus;

public record PaymentResponse(
            Long paymentId,
            int paymentAmount,
            PaymentStatus status
    ) {
    }