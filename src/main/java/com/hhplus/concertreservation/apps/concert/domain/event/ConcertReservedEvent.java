package com.hhplus.concertreservation.apps.concert.domain.event;

import com.hhplus.concertreservation.apps.kafka.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConcertReservedEvent extends DomainEvent {
    private final Long reservationId;
}
