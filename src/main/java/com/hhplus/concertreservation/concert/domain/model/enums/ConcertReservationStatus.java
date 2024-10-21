package com.hhplus.concertreservation.concert.domain.model.enums;

import lombok.Getter;

@Getter
public enum ConcertReservationStatus {
    TEMPORARY_RESERVED("임시 예약"),
    CONFIRMED("예약 확정"),
    CANCELED("예약 취소");

    private final String value;

    ConcertReservationStatus(String value) {
        this.value = value;
    }
}
