package com.hhplus.concertreservation.apps.concert.domain.model.enums;

import lombok.Getter;

@Getter
public enum ConcertSeatStatus {
    TEMPORARY_RESERVED("임시 예약"),
    CONFIRMED("확정 예약"),
    AVAILABLE("예약 가능");

    private final String value;

    ConcertSeatStatus(String value) {
        this.value = value;
    }
}
