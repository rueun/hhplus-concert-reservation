package com.hhplus.concertreservation.queue.domain.model.entity;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    /**
     * 대기열 새로 생성 시 사용
     * @param userId 사용자 ID
     * @param token 대기열 토큰
     */
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
