package com.hhplus.concertreservation.concert.domain.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertSession {

    private Long id;
    private Long concertId;
    private LocalDateTime concertAt;
    private int totalSeatCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
