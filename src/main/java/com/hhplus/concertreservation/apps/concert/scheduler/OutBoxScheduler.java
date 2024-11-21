package com.hhplus.concertreservation.apps.concert.scheduler;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.domain.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelayString = "10000")
    public void rePublishMessageInitState() {
        log.info("Outbox 대기 중인 이벤트 발송 스케줄러 실행");
        final List<Outbox> outboxes = outboxService.findAllAfterThreshold(5);
        outboxes.forEach(outbox -> {
            try {
                outboxService.publish(outbox.getEventKey());
            } catch (Exception e) {
                log.error("Outbox 이벤트 발송 중 오류 발생 (ID: {}): {}", outbox.getId(), e.getMessage());
            }
        });
    }
}
