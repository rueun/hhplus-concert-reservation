package com.hhplus.concertreservation.apps.queue.domain.service;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueReader waitingQueueReader;
    private final WaitingQueueWriter waitingQueueWriter;
    private final UUIDGenerator uuidGenerator;

    /**
     * 대기열 생성
     * @param userId 사용자 ID
     * @return 생성된 대기열 정보
     */
    public WaitingQueue createWaitingQueue(final Long userId) {
        final String token = uuidGenerator.generate();
        return waitingQueueWriter.createWaitingQueue(new WaitingQueue(userId, token));
    }

    /**
     * 대기열 정보 조회
     * 현재 대기열 정보를 조회하고 대기 순서를 계산하여 반환
     * @param token 대기열 토큰 정보
     * @return 대기열 정보(대기 순서 정보 포함)
     */
    public WaitingQueueInfo getWaitingQueueInfo(final String token) {
        final WaitingQueue currentWaitingQueue = waitingQueueReader.getByToken(token);
        return WaitingQueueInfo.of(currentWaitingQueue);
    }


    /**
     * 가장 오래된 대기열 목록 조회
     * @param activationCount 조회하려는 활성화 대기열 개수
     * @return 활성화하려는 대기열 목록
     */
    public List<WaitingQueue> getWaitingQueuesToBeActivated(final int activationCount) {
        return waitingQueueReader.getWaitingQueuesToBeActivated(activationCount);
    }

    /**
     * 현재 대기열이 활성화 상태인지 확인. 각 요청 전에 대기열 상태를 활성화 상태인지 확인할 때 사용
     * @param token 대기열 토큰 정보
     * @throws CoreException 대기열이 만료되었거나 활성화 상태가 아닌 경우
     */
    public void activateQueue(final String token) {
        waitingQueueWriter.moveToActiveQueue(token);
    }

    /**
     * 현재 대기열이 활성화 상태인지 확인. 각 요청 전에 대기열 상태를 활성화 상태인지 확인할 때 사용
     * @param token 대기열 토큰 정보
     * @throws CoreException 대기열 토큰이 활성화 상태가 아닌 경우
     */
    public void checkActivatedQueue(final String token) {
        final WaitingQueue currentWaitingQueue = waitingQueueReader.getActiveQueueByToken(token);

        if (!currentWaitingQueue.isActivated()) {
            waitingQueueWriter.removeActiveQueue(token);
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_ACTIVATED, "대기열 정보가 활성상태가 아닙니다.");
        }
    }

    /**
     * 활성 대기열 만료 처리
     * @param token 대기열 토큰 정보
     */
    public void expireActiveQueue(final String token) {
        waitingQueueWriter.removeActiveQueue(token);
    }
}
