package com.hhplus.concertreservation.queue.domain.model.entity;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WaitingQueue {

    private Long id;
    private Long userId;
    private String token;
    private QueueStatus status;
    private LocalDateTime activatedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lastActionedAt;

    public WaitingQueue (final Long userId, final String token) {
        this.userId = userId;
        this.token = token;
        this.status = QueueStatus.WAITING;
        this.lastActionedAt = LocalDateTime.now();
    }
}
