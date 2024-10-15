package com.hhplus.concertreservation.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueJpaReader implements WaitingQueueReader {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public WaitingQueueEntity getById(final Long id) {
        return waitingQueueJpaRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public WaitingQueueEntity getByToken(final String token) {
        return waitingQueueJpaRepository.findByToken(token)
                .orElseThrow();
    }
}
