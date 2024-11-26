package com.hhplus.concertreservation.apps.concert.infrastruture.entity;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.OutboxStatus;
import com.hhplus.concertreservation.support.domain.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox")
public class OutboxEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType;
    private String eventKey;
    @Column(columnDefinition = "TEXT")
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;


    public OutboxEntity(final Outbox outbox) {
        this.id = outbox.getId();
        this.eventType = outbox.getEventType();
        this.eventKey = outbox.getEventKey();
        this.payload = outbox.getPayload();
        this.status = outbox.getStatus();
        this.createdAt = outbox.getCreatedAt();
        this.updatedAt = outbox.getUpdatedAt();
    }

    public Outbox toDomain() {
        return Outbox.builder()
                .id(id)
                .eventType(eventType)
                .eventKey(eventKey)
                .payload(payload)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

