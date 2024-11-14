package com.hhplus.concertreservation.apps.concert.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConcertReservedEvent {
    private final Long reservationId;
}
