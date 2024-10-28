package com.hhplus.concertreservation.apps.queue.schduler;

import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final WaitingQueueService waitingQueueService;

    @Scheduled(fixedDelayString = "60000")
    public void activateWaitingQueue() {
        log.info("대기열 활성화 스케줄러 실행");
        final List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueues(200);
        waitingQueues.forEach(waitingQueue -> {
            try {
                waitingQueueService.activateQueue(waitingQueue.getToken());
            } catch (Exception e) {
                log.warn("대기열 활성화 중 오류 발생 (Token: {}): {}", waitingQueue.getToken(), e.getMessage());
            }
        });
    }


    @Scheduled(fixedDelayString = "60000")
    public void expireWaitingQueue() {
        log.info("대기열 만료 스케줄러 실행");
        final List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueueToBeExpired(10);
        waitingQueues.forEach(waitingQueue -> {
            try {
                waitingQueueService.expireQueue(waitingQueue.getToken());
            } catch (Exception e) {
                log.warn("대기열 토큰 만료 중 오류 발생 (Token: {}): {}", waitingQueue.getToken(), e.getMessage());
            }
        });
    }

}
