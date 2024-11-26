package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxReader {
    List<Outbox> findAllAfterThreshold(int timeLimit, LocalDateTime now);
    Outbox getByEventKey(String eventKey);
}
