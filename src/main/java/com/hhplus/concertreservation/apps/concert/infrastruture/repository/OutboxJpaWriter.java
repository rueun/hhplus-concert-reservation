package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxJpaWriter implements OutboxWriter {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox save(final Outbox outbox) {
        final OutboxEntity outboxEntity = new OutboxEntity(outbox);
        return outboxJpaRepository.save(outboxEntity).toDomain();
    }
}
