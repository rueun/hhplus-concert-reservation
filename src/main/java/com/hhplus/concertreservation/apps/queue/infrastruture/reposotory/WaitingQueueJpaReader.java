package com.hhplus.concertreservation.apps.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.infrastruture.entity.WaitingQueueEntity;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingQueueJpaReader implements WaitingQueueReader {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final TimeProvider timeProvider;

    @Override
    public WaitingQueue getById(final Long id) {
        return waitingQueueJpaRepository.findById(id)
                .map(WaitingQueueEntity::toDomain)
                .orElseThrow(() -> new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_FOUND));
    }

    @Override
    public WaitingQueue getByToken(final String token) {
        return waitingQueueJpaRepository.findByToken(token)
                .map(WaitingQueueEntity::toDomain)
                .orElseThrow(() -> new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_FOUND));
    }

    @Override
    public Optional<WaitingQueue> getLatestActivatedQueue() {
        return waitingQueueJpaRepository.getLatestActivatedQueue()
                .map(WaitingQueueEntity::toDomain);
    }

    @Override
    public List<WaitingQueue> getWaitingQueues(final int activationCount) {
        return waitingQueueJpaRepository.findWaitingQueues(activationCount).stream()
                .map(WaitingQueueEntity::toDomain)
                .toList();
    }

    @Override
    public List<WaitingQueue> getWaitingQueueToBeExpired(final int minutes) {

        // 현재 시각에서 minutes 만큼 이전의 시간을 계산
        final LocalDateTime expirationTime = timeProvider.now().minusMinutes(minutes);

        return waitingQueueJpaRepository.findWaitingQueueToBeExpired(expirationTime).stream()
                .map(WaitingQueueEntity::toDomain)
                .toList();
    }
}
