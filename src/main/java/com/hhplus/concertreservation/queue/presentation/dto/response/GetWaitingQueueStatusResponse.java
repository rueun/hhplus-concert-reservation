package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;

public record GetWaitingQueueStatusResponse(
            Long id,
            Long userId,
            QueueStatus status,
            Long waitingCount
    ) {
    }