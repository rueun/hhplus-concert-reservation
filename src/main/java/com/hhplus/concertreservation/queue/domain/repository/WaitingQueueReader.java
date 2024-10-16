package com.hhplus.concertreservation.queue.domain.repository;

import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;

import java.util.Optional;

public interface WaitingQueueReader {
    WaitingQueue getById(Long id);

    WaitingQueue getByToken(String token);

    Optional<WaitingQueue> getLatestActivatedQueue();
}
