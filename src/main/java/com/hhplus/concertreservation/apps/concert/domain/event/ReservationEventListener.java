package com.hhplus.concertreservation.apps.concert.domain.event;

import com.hhplus.concertreservation.apps.concert.infrastruture.external.DataPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final DataPlatformService dataPlatformService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final ConcertReservedEvent event) {
        try {
            log.info("예약 데이터 전송: {}", event.getReservationId());
            dataPlatformService.sendReservationData(event.getReservationId());
        } catch (Exception e) {
            log.error("데이터 플랫폼 전송 실패: {}", e.getMessage());
            // 재시도 로직 추가 가능 (예: 메시지 큐, 스케줄러 등)
        }
    }
}
