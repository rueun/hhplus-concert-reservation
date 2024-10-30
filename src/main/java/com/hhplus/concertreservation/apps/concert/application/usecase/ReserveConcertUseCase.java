package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.apps.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.apps.user.domain.service.UserService;
import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.common.aop.annotation.MultiDistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ReserveConcertUseCase {

    private final UserService userService;
    private final ConcertService concertService;

    @MultiDistributedLock(prefix = "consertSeatId", key = "#command.seatIds")
    public ConcertReservationInfo reserveConcert(final ReserveConcertCommand command) {
        userService.checkUserExist(command.userId());
        // 콘서트 예약 처리
        final ConcertReservationInfo concertReservationInfo = concertService.reserveConcert(command);
        log.info("임시 예약 완료: 사용자 ID = {}, 콘서트 ID = {}, 예약 좌석 수 = {}, 총 가격 = {}",
                concertReservationInfo.getUserId(), concertReservationInfo.getConcertId(),
                concertReservationInfo.getSeats().size(), concertReservationInfo.getTotalPrice());
        return concertReservationInfo;
    }
}
