package com.hhplus.concertreservation.queue.domain.service;

import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;
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

    public WaitingQueueEntity getWaitingQueue(final String token) {
        return waitingQueueReader.getByToken(token);
    }
}
