package com.hhplus.concertreservation.concert.domain.service;

import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.concert.domain.exception.NotConcertReservationPeriodException;
import com.hhplus.concertreservation.concert.domain.model.dto.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
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

    @Transactional
    public ConcertReservation reserveConcert(final ReserveConcertCommand command) {
        final Concert concert = concertReader.getConcertById(command.concertId());
        if (!concert.isWithinReservationPeriod(timeProvider.now())) {
            throw new NotConcertReservationPeriodException("예약 가능한 기간이 아닙니다.");
        }

        // 콘서트 좌석 업데이트
        final List<ConcertSeat> concertSeats = command.seatIds().stream()
                .map(concertReader::getConcertSeatById)
                .toList();

        concertSeats.forEach(ConcertSeat::reserve);
        concertWriter.saveAll(concertSeats);

        // 콘서트 예약 내역 생성
        long totalPrice = concertSeats.stream()
                .mapToLong(ConcertSeat::getPrice)
                .sum();

        final ConcertReservation concertReservation = new ConcertReservation(command, totalPrice, timeProvider.now());
        return concertWriter.save(concertReservation);
    }

}
