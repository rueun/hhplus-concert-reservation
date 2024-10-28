package com.hhplus.concertreservation.apps.concert.scheduler;

import com.hhplus.concertreservation.apps.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertReservationScheduler {

    private final ConcertService concertService;

    @Scheduled(fixedDelayString = "60000")
    public void expireTemporaryReservations() {
        log.info("임시예약 만료 스케줄러 실행");
        final List<ConcertReservation> concertReservations = concertService.getTemporaryReservationToBeExpired(5);
        concertReservations.forEach(concertReservation -> {
            try {
                concertService.cancelTemporaryReservation(concertReservation.getId());
            } catch (Exception e) {
                log.warn("임시예약 만료 중 오류 발생 (ID: {}): {}", concertReservation.getId(), e.getMessage());
            }
        });
    }

}
