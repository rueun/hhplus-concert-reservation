package com.hhplus.concertreservation.concert.domain.service;

import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertSeatsInfo;
import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertWriter concertWriter;
    private final ConcertReader concertReader;
    private final TimeProvider timeProvider;

    /**
     * 콘서트 임시 예약
     * @param command
     * @return 예약 정보
     */
    @Transactional
    public ConcertReservationInfo reserveConcert(final ReserveConcertCommand command) {
        final Concert concert = concertReader.getConcertById(command.concertId());
        if (!concert.isWithinReservationPeriod(timeProvider.now())) {
            throw new CoreException(ConcertErrorType.RESERVATION_PERIOD_NOT_AVAILABLE, "예약 가능한 기간이 아닙니다.");
        }

        // 콘서트 좌석 업데이트
        final List<ConcertSeat> concertSeats = command.seatIds().stream()
                .map(concertReader::getConcertSeatById)
                .toList();

        concertSeats.forEach(ConcertSeat::reserveTemporary);
        final List<ConcertSeat> savedConcertSeats = concertWriter.saveAll(concertSeats);

        // 콘서트 예약 내역 생성
        long totalPrice = concertSeats.stream()
                .mapToLong(ConcertSeat::getPrice)
                .sum();

        final ConcertReservation concertReservation = ConcertReservation.create(command, totalPrice, timeProvider.now());
        final ConcertReservation savedConcertReservation = concertWriter.save(concertReservation);

        return new ConcertReservationInfo(savedConcertReservation, savedConcertSeats);
    }

    public ConcertReservation  getConcertReservation(final Long concertReservationId) {
        return concertReader.getConcertReservationById(concertReservationId);
    }


    /**
     * 콘서트 예약 확정 처리
     * @param reservationId 예약 ID
     */
    @Transactional
    public void completeReservation(final Long reservationId) {
        final ConcertReservation concertReservation = concertReader.getConcertReservationById(reservationId);
        final List<ConcertSeat> concertSeats = concertReader.getConcertSeatsByIds(concertReservation.getSeatIds());

        concertReservation.complete();
        concertSeats.forEach(ConcertSeat::confirm);

        concertWriter.save(concertReservation);
        concertWriter.saveAll(concertSeats);
    }


    /**
     * 콘서트 예약 가능한 세션 목록 조회
     * 현재 시간 기준으로 콘서트 예약 가능한 세션 목록을 조회한다.
     * @param concertId 콘서트 ID
     * @return 예약 가능한 세션 목록
     */
    public List<ConcertSession> getAvailableConcertSessions(final Long concertId) {
        final Concert concert = concertReader.getConcertById(concertId);
        if (!concert.isWithinReservationPeriod(timeProvider.now())) {
            return List.of();
        }

        return concertReader.getConcertSessionsByConcertId(concertId).stream()
                .filter(session -> session.isAvailable(timeProvider.now()))
                .toList();
    }


    /**
     * 콘서트 세션의 좌석 목록 조회
     * @param concertSessionId 콘서트 세션 ID
     */
    public ConcertSeatsInfo getConcertSeatsInfo(final Long concertSessionId) {

        final ConcertSession concertSession = concertReader.getConcertSessionById(concertSessionId);
        final List<ConcertSeat> concertSeats = concertReader.getConcertSeatsBySessionId(concertSessionId);

        final List<ConcertSeat> availableSeats = concertSeats.stream()
                .filter(ConcertSeat::isAvailable)
                .toList();

        final List<ConcertSeat> unavailableSeats = concertSeats.stream()
                .filter(ConcertSeat::isUnavailable)
                .toList();

        return ConcertSeatsInfo.of(concertSession, availableSeats, unavailableSeats);
    }

    /**
     * 콘서트 임시 예약 취소
     * 스케줄러를 통해 호출되는 메서드
     * @param reservationId 예약 ID
     */
    @Transactional
    public void cancelTemporaryReservation(final Long reservationId) {
        final ConcertReservation concertReservation = concertReader.getConcertReservationById(reservationId);
        final List<ConcertSeat> concertSeats = concertReader.getConcertSeatsByIds(concertReservation.getSeatIds());

        concertReservation.cancelTemporaryReservation();
        concertSeats.forEach(ConcertSeat::cancel);

        concertWriter.save(concertReservation);
        concertWriter.saveAll(concertSeats);
    }
}
