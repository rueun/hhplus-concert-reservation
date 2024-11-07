package com.hhplus.concertreservation.apps.queue.application.usecase;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService2;
import com.hhplus.concertreservation.apps.user.domain.service.UserService;
import com.hhplus.concertreservation.common.UseCase;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
public class CreateWaitingQueueUseCase2 {

    private final UserService userService;
    private final WaitingQueueService2 waitingQueueService;

    public WaitingQueue createWaitingQueue(final Long userId) {
        userService.checkUserExist(userId);
        return waitingQueueService.createWaitingQueue(userId);
    }
}
