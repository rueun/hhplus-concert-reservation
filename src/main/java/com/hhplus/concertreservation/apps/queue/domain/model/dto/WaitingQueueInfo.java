package com.hhplus.concertreservation.apps.queue.domain.model.dto;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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

    public static WaitingQueueInfo of(final WaitingQueue currentWaitingQueue) {
        return WaitingQueueInfo.builder()
                .userId(currentWaitingQueue.getUserId())
                .token(currentWaitingQueue.getToken())
                .status(currentWaitingQueue.getStatus())
                .waitingNumber(currentWaitingQueue.getWaitingOrder())
                .build();
    }
}
