package com.hhplus.concertreservation.queue.domain.model.entity;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WaitingQueue {

    private Long id;
    private Long userId;
    private String token;
    private QueueStatus status;
    private LocalDateTime activatedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lastActionedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public WaitingQueue(Long id, Long userId, String token, QueueStatus status, LocalDateTime activatedAt, LocalDateTime expiredAt, LocalDateTime lastActionedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.activatedAt = activatedAt;
        this.expiredAt = expiredAt;
        this.lastActionedAt = lastActionedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public WaitingQueue (final Long userId, final String token) {
        this.userId = userId;
        this.token = token;
        this.status = QueueStatus.WAITING;
        this.lastActionedAt = LocalDateTime.now();
    }

    public boolean isWaiting() {
        return this.status == QueueStatus.WAITING;
    }

    public boolean isActivated() {
        return this.status == QueueStatus.ACTIVATED;
    }

    public boolean isExpired() {
        return this.status == QueueStatus.EXPIRED;
    }
}
