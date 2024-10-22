package com.hhplus.concertreservation.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.queue.domain.exception.WaitingQueueNotFoundException;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingQueueJpaReader implements WaitingQueueReader {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public WaitingQueue getById(final Long id) {
        return waitingQueueJpaRepository.findById(id)
                .map(WaitingQueueEntity::toDomain)
                .orElseThrow(WaitingQueueNotFoundException::new);
    }

    @Override
    public WaitingQueue getByToken(final String token) {
        return waitingQueueJpaRepository.findByToken(token)
                .map(WaitingQueueEntity::toDomain)
                .orElseThrow(WaitingQueueNotFoundException::new);
    }

    @Override
    public Optional<WaitingQueue> getLatestActivatedQueue() {
        return waitingQueueJpaRepository.getLatestActivatedQueue()
                .map(WaitingQueueEntity::toDomain);
    }
}
