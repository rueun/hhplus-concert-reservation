package com.hhplus.concertreservation.apps.queue.domain.model.entity;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueueToken {
    private Long userId;
    private String token;
    private QueueStatus status;
    private LocalDateTime activatedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    /**
     * 대기열 새로 생성 시 사용
     * @param userId 사용자 ID
     * @param token 대기열 토큰
     */
    public QueueToken(final Long userId, final String token) {
        this.userId = userId;
        this.token = token;
        this.status = QueueStatus.WAITING;
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

    public void activate(final LocalDateTime activatedAt) {
        if (this.isActivated()) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_ALREADY_ACTIVATED, "이미 활성화된 대기열입니다.");
        }

        if (this.isExpired()) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED, "만료된 대기열은 활성화할 수 없습니다.");
        }

        this.status = QueueStatus.ACTIVATED;
        this.activatedAt = activatedAt;
    }

    public void expire(final LocalDateTime expiredAt) {
        this.status = QueueStatus.EXPIRED;
        this.expiredAt = expiredAt;
    }
}
