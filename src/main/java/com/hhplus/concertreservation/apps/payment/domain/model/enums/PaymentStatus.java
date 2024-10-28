package com.hhplus.concertreservation.apps.payment.domain.model.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PAID("결제 완료"),
    CANCELED("결제 취소");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
