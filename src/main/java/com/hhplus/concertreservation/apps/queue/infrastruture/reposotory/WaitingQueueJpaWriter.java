package com.hhplus.concertreservation.apps.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.infrastruture.entity.WaitingQueueEntity;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueJpaWriter implements WaitingQueueWriter {
    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public WaitingQueue save(final WaitingQueue waitingQueue) {
        final WaitingQueueEntity entity = waitingQueueJpaRepository.save(new WaitingQueueEntity(waitingQueue));
        return entity.toDomain();
    }
}
