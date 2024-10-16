package com.hhplus.concertreservation.concert.domain.service;

import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.concert.domain.exception.ConcertSeatUnavailableException;
import com.hhplus.concertreservation.concert.domain.exception.NotConcertReservationPeriodException;
import com.hhplus.concertreservation.concert.domain.model.dto.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertReservationStatus;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertWriter concertWriter;

    @Mock
    private ConcertReader concertReader;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private ConcertService concertService;

    @Test
    void 콘서트_예약_성공() {
        // given
        ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));

        Concert concert = mock(Concert.class);
        given(concertReader.getConcertById(1L)).willReturn(concert);
        given(concert.isWithinReservationPeriod(any())).willReturn(true);

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .price(10000)
                .status(ConcertSeatStatus.AVAILABLE)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .price(20000)
                .status(ConcertSeatStatus.AVAILABLE)
                .build();

        given(concertReader.getConcertSeatById(1L)).willReturn(concertSeat1);
        given(concertReader.getConcertSeatById(2L)).willReturn(concertSeat2);

        ConcertReservation concertReservation = new ConcertReservation(command, 30000, LocalDateTime.now());
        given(concertWriter.save(any())).willReturn(concertReservation);

        // when
        final ConcertReservation result = concertService.reserveConcert(command);

        // then
        assertAll(
                () -> assertEquals(30000, result.getTotalPrice()),
                () -> assertEquals(2, result.getSeatIds().size()),
                () -> assertEquals(ConcertReservationStatus.TEMPORARY_RESERVED, result.getStatus())
        );
    }

    @Test
    void 콘서트_예약이_불가능한_기간이라면_예외가_발생한다() {
        // given
        ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));

        Concert concert = mock(Concert.class);
        given(concertReader.getConcertById(1L)).willReturn(concert);
        given(concert.isWithinReservationPeriod(any())).willReturn(false);

        // when & then
        thenThrownBy(() -> concertService.reserveConcert(command))
                .isInstanceOf(NotConcertReservationPeriodException.class)
                .hasMessage("예약 가능한 기간이 아닙니다.");
    }

    @Test
    void 콘서트_좌석이_예약_불가능하다면_예외가_발생한다() {
        // given
        ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));

        Concert concert = mock(Concert.class);
        given(concertReader.getConcertById(1L)).willReturn(concert);
        given(concert.isWithinReservationPeriod(any())).willReturn(true);

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .price(10000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .price(20000)
                .status(ConcertSeatStatus.AVAILABLE)
                .build();

        given(concertReader.getConcertSeatById(1L)).willReturn(concertSeat1);
        given(concertReader.getConcertSeatById(2L)).willReturn(concertSeat2);

        // when & then
        thenThrownBy(() -> concertService.reserveConcert(command))
                .isInstanceOf(ConcertSeatUnavailableException.class)
                .hasMessage("예약 가능한 좌석이 아닙니다.");
    }
}