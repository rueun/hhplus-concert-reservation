package com.hhplus.concertreservation.apps.queue.domain.repository;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueReader {
    WaitingQueue getById(Long id);

    WaitingQueue getByToken(String token);

    Optional<WaitingQueue> getLatestActivatedQueue();

    List<WaitingQueue> getWaitingQueues(int activationCount);

    List<WaitingQueue> getWaitingQueueToBeExpired(int minutes);
}
