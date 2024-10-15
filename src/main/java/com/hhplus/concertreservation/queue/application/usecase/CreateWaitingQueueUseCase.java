package com.hhplus.concertreservation.queue.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.user.domain.service.UserService;
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
