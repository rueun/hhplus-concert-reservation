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

    @Scheduled(fixedDelayString = "10000")
    public void activateWaitingQueue() {
        log.info("대기열 활성화 스케줄러 실행");
        final List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueuesToBeActivated(3000);
        waitingQueues.forEach(waitingQueue -> {
            try {
                waitingQueueService.activateQueue(waitingQueue.getToken());
            } catch (Exception e) {
                log.warn("대기열 활성화 중 오류 발생 (Token: {}): {}", waitingQueue.getToken(), e.getMessage());
            }
        });
    }
}
