package com.hhplus.concertreservation.apps.concert.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hhplus.concertreservation.apps.concert.domain.event.ConcertReservedEvent;
import com.hhplus.concertreservation.apps.concert.domain.service.OutboxService;
import com.hhplus.concertreservation.apps.kafka.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final OutboxService outboxService;
    private final KafkaMessageProducer kafkaMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(final ConcertReservedEvent event) throws JsonProcessingException {
        outboxService.createOutbox(event.getClass().getSimpleName(), event.getEventKey(), event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final ConcertReservedEvent event) {
        kafkaMessageProducer.send("concert.concert-reserved", event.getEventKey(), event);
    }
}
