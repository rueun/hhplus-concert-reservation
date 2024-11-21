package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;

public interface OutboxWriter {
    Outbox save(Outbox outbox);
}
