package com.hhplus.concertreservation.queue.domain.repository;

import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;

public interface WaitingQueueWriter {
    WaitingQueue save(WaitingQueue waitingQueue);
}
