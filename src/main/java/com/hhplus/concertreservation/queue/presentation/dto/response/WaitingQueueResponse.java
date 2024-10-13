package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;

import java.time.LocalDateTime;

public class WaitingQueueResponse {
    public record CreateWaitingQueueResponse(
            String id,
            String token,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime joinedAt
    ) {
    }

    public record GetWaitingQueueStatusResponse(
            Long id,
            Long userId,
            QueueStatus status,
            Long waitingCount
    ) {
    }
}
