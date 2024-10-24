package com.hhplus.concertreservation.concert.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ReserveConcertUseCase {

    private final UserService userService;
    private final ConcertService concertService;

    public ConcertReservationInfo reserveConcert(final ReserveConcertCommand command) {
        userService.checkUserExist(command.userId());
        try {
            // 콘서트 예약 처리
            final ConcertReservationInfo concertReservationInfo = concertService.reserveConcert(command);
            log.info("임시 예약 완료: 사용자 ID = {}, 콘서트 ID = {}, 예약 좌석 수 = {}, 총 가격 = {}",
                    concertReservationInfo.getUserId(), concertReservationInfo.getConcertId(),
                    concertReservationInfo.getSeats().size(), concertReservationInfo.getTotalPrice());
            return concertReservationInfo;
        } catch (ObjectOptimisticLockingFailureException e) {
            // 동시성 문제 발생 시 사용자 정의 예외로 변환
            throw new CoreException(ConcertErrorType.CONCERT_SEAT_UNAVAILABLE, "예약 가능한 좌석이 아닙니다.");
        }
    }
}
