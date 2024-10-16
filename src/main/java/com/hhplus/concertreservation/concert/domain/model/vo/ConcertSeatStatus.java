package com.hhplus.concertreservation.concert.domain.model.vo;

import lombok.Getter;

@Getter
public enum ConcertSeatStatus {
    TEMPORARY_RESERVED("임시 예약"),
    PAYMENT_COMPLETED("결제 완료"),
    AVAILABLE("예약 가능");

    private final String value;

    ConcertSeatStatus(String value) {
        this.value = value;
    }
}
