package com.hhplus.concertreservation.apps.queue.application.usecase;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.apps.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
public class CreateWaitingQueueUseCase {

    private final UserService userService;
    private final WaitingQueueService waitingQueueService;

    public WaitingQueue createWaitingQueue(final Long userId) {
        userService.checkUserExist(userId);
        return waitingQueueService.createWaitingQueue(userId);
    }
}
