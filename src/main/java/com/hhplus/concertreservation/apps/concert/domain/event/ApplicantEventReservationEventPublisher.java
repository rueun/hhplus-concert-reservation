package com.hhplus.concertreservation.apps.concert.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicantEventReservationEventPublisher implements ReservationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(final ConcertReservedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
