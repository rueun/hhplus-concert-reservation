package com.hhplus.concertreservation.queue.presentation.dto.response;

public record GetQueueStatusResponse(
        Long id,
        Long userId,
        // TODO: 추후 enum으로 변경 (WAITING, ACTIVE, EXPIRED)
        String status,
        Long waitingCount
) {
}
