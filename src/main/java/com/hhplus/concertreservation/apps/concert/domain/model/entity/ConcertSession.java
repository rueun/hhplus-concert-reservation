package com.hhplus.concertreservation.apps.concert.domain.model.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertSession {

    private Long id;
    private Long concertId;
    private LocalDateTime concertAt;
    private int totalSeatCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isAvailable(final LocalDateTime now) {
        return concertAt.isAfter(now);
    }
}
