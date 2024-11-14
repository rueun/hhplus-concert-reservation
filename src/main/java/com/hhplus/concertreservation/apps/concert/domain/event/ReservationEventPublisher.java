package com.hhplus.concertreservation.apps.concert.domain.event;

public interface ReservationEventPublisher {
    void publish(ConcertReservedEvent event);
}