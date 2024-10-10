package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;

import java.time.LocalDateTime;

public class QueueResponse {
    public record CreateQueueResponse (
            String id,
            String token,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime joinedAt
    ) {
    }

    public record GetQueueStatusResponse(
            Long id,
            Long userId,
            QueueStatus status,
            Long waitingCount
    ) {
    }
}
