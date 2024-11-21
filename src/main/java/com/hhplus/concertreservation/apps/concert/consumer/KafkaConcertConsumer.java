package com.hhplus.concertreservation.apps.concert.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.domain.service.OutboxService;
import com.hhplus.concertreservation.apps.concert.infrastruture.external.DataPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaConcertConsumer {

    private final OutboxService outboxService;
    private final DataPlatformService dataPlatformService;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "outbox-group", topics = "concert.concert-reserved")
    public void consumeOutbox(ConsumerRecord<String, String> data, Acknowledgment acknowledgment, Consumer<String, String> consumer){
        final String eventKey = data.key();
        outboxService.publish(eventKey);
        acknowledgment.acknowledge();
    }

    @KafkaListener(groupId = "external-platform-group", topics = "concert.concert-reserved")
    public void consumeExternalPlatform(ConsumerRecord<String, String> data, Acknowledgment acknowledgment, Consumer<String, String> consumer) throws JsonProcessingException {
        // 외부 플랫폼 연동
        Outbox outbox = objectMapper.readValue(data.value(), Outbox.class);
        final String payload = outbox.getPayload();
        dataPlatformService.sendReservationData(payload);
        acknowledgment.acknowledge();
    }
}

