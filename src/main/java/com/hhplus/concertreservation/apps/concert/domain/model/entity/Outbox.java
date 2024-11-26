package com.hhplus.concertreservation.apps.concert.domain.model.entity;

import com.hhplus.concertreservation.apps.concert.domain.model.enums.OutboxStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Outbox {
    private Long id;
    private String eventType;
    private String eventKey;
    private String payload;
    private OutboxStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Outbox create(final String eventType, final String eventKey, final String payload) {
        return Outbox.builder()
                .eventType(eventType)
                .eventKey(eventKey)
                .payload(payload)
                .status(OutboxStatus.INIT)
                .build();
    }

    public void publish() {
        this.status = OutboxStatus.PUBLISHED;
    }
}
