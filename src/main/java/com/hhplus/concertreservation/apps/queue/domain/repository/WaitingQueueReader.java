package com.hhplus.concertreservation.apps.queue.domain.repository;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueReader {
    WaitingQueue getByToken(String token);
    List<WaitingQueue> getWaitingQueuesToBeActivated(int activationCount);
    WaitingQueue getActiveQueueByToken(String token);
}
