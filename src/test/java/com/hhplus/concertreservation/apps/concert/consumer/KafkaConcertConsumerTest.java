package com.hhplus.concertreservation.apps.concert.consumer;

import com.hhplus.concertreservation.apps.concert.domain.event.ConcertReservedEvent;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.OutboxStatus;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.OutboxEntity;
import com.hhplus.concertreservation.apps.concert.infrastruture.repository.OutboxJpaRepository;
import com.hhplus.concertreservation.apps.kafka.KafkaMessageProducer;
import com.hhplus.concertreservation.common.testcontainer.TestContainersTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class KafkaConcertConsumerTest extends TestContainersTest {

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private KafkaConcertConsumer kafkaConcertConsumer;

    @Autowired
    private OutboxJpaRepository outboxJpaRepository;

    private final String topic = "concert.concert-reserved";
    private final String eventKey = "eventKey";


    @Test
    void 카프카_이벤트_발행_시_아웃박스에_존재하는_이벤트의_상태가_PUBLISHED로_변경된다() throws InterruptedException {
        // given
        final Outbox outbox = Outbox.create("test", eventKey, "test");
        outboxJpaRepository.save(new OutboxEntity(outbox));
        final ConcertReservedEvent event = new ConcertReservedEvent(1L);

        // when
        kafkaMessageProducer.send(topic, eventKey, event);
        Thread.sleep(1000);
        // then
        final Optional<OutboxEntity> outboxEvent = outboxJpaRepository.findByEventKey(eventKey);

        assertAll(
                () -> assertTrue(outboxEvent.isPresent()),
                () -> assertEquals(eventKey, outboxEvent.get().getEventKey()),
                () -> assertEquals(OutboxStatus.PUBLISHED, outboxEvent.get().getStatus())
        );
    }
}