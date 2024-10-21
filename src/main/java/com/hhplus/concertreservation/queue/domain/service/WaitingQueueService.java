package com.hhplus.concertreservation.queue.domain.service;

import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueReader waitingQueueReader;
    private final WaitingQueueWriter waitingQueueWriter;

    private final UUIDGenerator uuidGenerator;
    private final TimeProvider timeProvider;

    public WaitingQueue createWaitingQueue(final Long userId) {
        final String token = uuidGenerator.generate();
        return waitingQueueWriter.save(new WaitingQueue(userId, token));
    }


    /**
     * 대기열 정보 조회
     * 현재 대기열 정보를 조회하고 대기 순서를 계산하여 반환
     * @param token 대기열 토큰 정보
     * @return 대기열 정보(대기 순서 정보 포함)
     */
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

    /**
     * 현재 대기열이 활성화 상태인지 확인. 각 요청 전에 대기열 상태를 활성화 상태인지 확인할 때 사용
     * @param token 대기열 토큰 정보
     * @throws CoreException 대기열이 만료되었거나 활성화 상태가 아닌 경우
     */
    public void checkActivatedQueue(final String token) {
        final WaitingQueue currentWaitingQueue = waitingQueueReader.getByToken(token);

        if (currentWaitingQueue.isExpired()) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED, "대기열이 만료되었습니다.");
        }

        if (!currentWaitingQueue.isActivated()) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_ACTIVATED, "대기열이 활성상태가 아닙니다.");
        }
    }

    /**
     * 활성화된 대기열 만료 처리
     * @param token 대기열 토큰 정보
     */
    public void expireQueue(final String token) {
        final WaitingQueue currentWaitingQueue = waitingQueueReader.getByToken(token);
        currentWaitingQueue.expire(timeProvider.now());
        waitingQueueWriter.save(currentWaitingQueue);
    }
}
