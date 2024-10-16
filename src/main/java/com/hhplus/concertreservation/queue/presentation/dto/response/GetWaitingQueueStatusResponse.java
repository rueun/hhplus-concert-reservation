package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;

public record GetWaitingQueueStatusResponse(
            Long id,
            Long userId,
            QueueStatus status,
            Long waitingCount
    ) {

    public static GetWaitingQueueStatusResponse of(final WaitingQueueInfo waitingQueueInfo) {
        return new GetWaitingQueueStatusResponse(
                waitingQueueInfo.id(),
                waitingQueueInfo.userId(),
                waitingQueueInfo.status(),
                waitingQueueInfo.waitingNumber()
        );
    }
}