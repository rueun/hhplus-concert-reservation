package com.hhplus.concertreservation.queue.infrastruture.entity;

import com.hhplus.concertreservation.common.auditing.BaseEntity;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "waiting_queue")
public class WaitingQueueEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String token;

    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "last_actioned_at")
    private LocalDateTime lastActionedAt;

    public WaitingQueueEntity (final WaitingQueue waitingQueue) {
        this.id = waitingQueue.getId();
        this.userId = waitingQueue.getUserId();
        this.token = waitingQueue.getToken();
        this.status = waitingQueue.getStatus();
        this.activatedAt = waitingQueue.getActivatedAt();
        this.expiredAt = waitingQueue.getExpiredAt();
        this.lastActionedAt = waitingQueue.getLastActionedAt();
        this.createdAt = waitingQueue.getCreatedAt();
        this.updatedAt = waitingQueue.getUpdatedAt();
    }

    public WaitingQueue toDomain() {
        return WaitingQueue.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .status(status)
                .activatedAt(activatedAt)
                .expiredAt(expiredAt)
                .lastActionedAt(lastActionedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
