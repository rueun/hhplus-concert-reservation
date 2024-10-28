package com.hhplus.concertreservation.apps.queue.domain.repository;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;

public interface WaitingQueueWriter {
    WaitingQueue save(WaitingQueue waitingQueue);
}
