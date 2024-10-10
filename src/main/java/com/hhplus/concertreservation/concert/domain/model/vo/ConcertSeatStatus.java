package com.hhplus.concertreservation.concert.domain.model.vo;

import lombok.Getter;

@Getter
public enum ConcertSeatStatus {
    RESERVED("예약 완료"),
    TEMPORARY_RESERVED("임시 예약"),
    AVAILABLE("예약 가능");

    private final String value;

    ConcertSeatStatus(String value) {
        this.value = value;
    }
}
