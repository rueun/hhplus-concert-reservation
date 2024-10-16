package com.hhplus.concertreservation.queue.domain.service;

import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueReader waitingQueueReader;
    private final WaitingQueueWriter waitingQueueWriter;

    private final UUIDGenerator uuidGenerator;

    public WaitingQueue createWaitingQueue(final Long userId) {
        final String token = uuidGenerator.generate();
        return waitingQueueWriter.save(new WaitingQueue(userId, token));
    }

    public WaitingQueueInfo getWaitingQueueInfo(final String token) {
        final WaitingQueue currentWaitingQueue = waitingQueueReader.getByToken(token);

        if (currentWaitingQueue.isWaiting()) {
            // 마지막 활성화된 대기열 정보를 조회하여 대기 순서를 계산
            Long waitingOrder = waitingQueueReader.getLatestActivatedQueue()
                    .map(latestActivatedQueue -> currentWaitingQueue.getId() - latestActivatedQueue.getId())
                    .orElse(currentWaitingQueue.getId());

            return WaitingQueueInfo.of(currentWaitingQueue, waitingOrder);

        }
        return WaitingQueueInfo.of(currentWaitingQueue, 0L);
    }
}
