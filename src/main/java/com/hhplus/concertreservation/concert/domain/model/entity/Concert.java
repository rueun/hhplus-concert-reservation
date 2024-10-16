package com.hhplus.concertreservation.concert.domain.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Concert {
    private Long id;
    private String name;
    private String place;
    private LocalDateTime reservationStartAt;
    private LocalDateTime reservationCloseAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isWithinReservationPeriod(final LocalDateTime now) {
        return now.isAfter(reservationStartAt) && now.isBefore(reservationCloseAt);
    }
}
