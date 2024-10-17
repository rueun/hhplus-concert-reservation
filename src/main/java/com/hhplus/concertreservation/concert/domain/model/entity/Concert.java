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
    private LocalDateTime reservationOpenAt;
    private LocalDateTime reservationCloseAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isWithinReservationPeriod(final LocalDateTime now) {
        return now.isAfter(reservationOpenAt) && now.isBefore(reservationCloseAt);
    }
}
