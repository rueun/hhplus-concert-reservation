package com.hhplus.concertreservation.apps.queue.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CreateWaitingQueueResponse(

            @Schema(name = "대기열 id")
            Long id,
            @Schema(name = "대기열 토큰")
            String token,
            @Schema(name = "대기열 생성 시각")
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