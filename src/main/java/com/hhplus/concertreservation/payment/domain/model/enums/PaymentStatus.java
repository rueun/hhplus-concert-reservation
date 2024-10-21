package com.hhplus.concertreservation.payment.domain.model.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    COMPLETED("결제 완료"),
    CANCELED("결제 취소");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
