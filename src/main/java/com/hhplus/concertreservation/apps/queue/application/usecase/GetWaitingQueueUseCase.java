package com.hhplus.concertreservation.apps.queue.application.usecase;

import com.hhplus.concertreservation.apps.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.common.UseCase;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
public class GetWaitingQueueUseCase {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueInfo getWaitingQueueInfo(final String token) {
        return waitingQueueService.getWaitingQueueInfo(token);
    }
}
