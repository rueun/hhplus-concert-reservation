package com.hhplus.concertreservation.queue.domain.repository;

import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;

public interface WaitingQueueReader {
    WaitingQueueEntity getById(Long id);

    WaitingQueueEntity getByToken(String token);
}
