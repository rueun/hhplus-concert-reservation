package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Repository
@RequiredArgsConstructor
public class OutboxJpaReader implements OutboxReader {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public List<Outbox> findAllAfterThreshold(final int timeLimit, final LocalDateTime now) {
        final LocalDateTime thresholdTime = now.minusMinutes(timeLimit);

        return outboxJpaRepository.findAllAfterThreshold(thresholdTime).stream()
                .map(OutboxEntity::toDomain)
                .toList();
    }

    @Override
    public Outbox getByEventKey(final String eventKey) {
        return outboxJpaRepository.findByEventKey(eventKey)
                .map(OutboxEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Outbox not found"));
    }
}
