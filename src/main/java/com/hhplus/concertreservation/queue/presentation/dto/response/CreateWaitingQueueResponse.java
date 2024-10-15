package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;

import java.time.LocalDateTime;

public record CreateWaitingQueueResponse(
            Long id,
            String token,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime joinedAt
    ) {

        public static CreateWaitingQueueResponse of(final WaitingQueue waitingQueue) {
            return new CreateWaitingQueueResponse(
                    waitingQueue.getId(),
                    waitingQueue.getToken(),
                    waitingQueue.getActivatedAt()
            );
        }
    }