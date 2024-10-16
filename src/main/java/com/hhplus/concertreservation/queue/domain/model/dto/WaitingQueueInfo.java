package com.hhplus.concertreservation.queue.domain.model.dto;

import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;

import java.time.LocalDateTime;

public record WaitingQueueInfo(
        Long id,
        Long userId,
        String token,
        QueueStatus status,
        LocalDateTime activatedAt,
        LocalDateTime expiredAt,
        LocalDateTime lastActionedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long waitingNumber
){

    public static WaitingQueueInfo of (final WaitingQueue waitingQueue, final Long waitingNumber) {
        return new WaitingQueueInfo(
                waitingQueue.getId(),
                waitingQueue.getUserId(),
                waitingQueue.getToken(),
                waitingQueue.getStatus(),
                waitingQueue.getActivatedAt(),
                waitingQueue.getExpiredAt(),
                waitingQueue.getLastActionedAt(),
                waitingQueue.getCreatedAt(),
                waitingQueue.getUpdatedAt(),
                waitingNumber
        );
    }
}
