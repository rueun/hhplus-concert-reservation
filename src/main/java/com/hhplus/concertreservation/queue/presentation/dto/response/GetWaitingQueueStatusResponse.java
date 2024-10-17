package com.hhplus.concertreservation.queue.presentation.dto.response;

import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetWaitingQueueStatusResponse(
            @Schema(name = "대기열 id")
            Long id,
            @Schema(name = "사용자 id")
            Long userId,
            @Schema(name = "대기열 상태")
            QueueStatus status,
            @Schema(name = "대기 번호")
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