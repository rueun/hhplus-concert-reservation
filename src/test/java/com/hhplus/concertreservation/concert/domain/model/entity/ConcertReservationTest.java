package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.InvalidConcertReservationStatusException;
import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertReservationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("콘서트 예약 도메인 단위 테스트")
class ConcertReservationTest {

    @Test
    void 예약이_임시예약_상태로_생성된다() {
        // given
        ReserveConcertCommand reserveConcertCommand = new ReserveConcertCommand(
                1L,
                2L,
                3L,
                List.of(1L, 2L)
        );

        LocalDateTime now = LocalDateTime.parse("2022-01-01T00:00:00");

        // when
        ConcertReservation concertReservation = ConcertReservation.create(reserveConcertCommand, 50000L, now);

        // then
        assertAll(
                () -> assertEquals(1L, concertReservation.getUserId()),
                () -> assertEquals(2L, concertReservation.getConcertId()),
                () -> assertEquals(3L, concertReservation.getConcertSessionId()),
                () -> assertEquals(List.of(1L, 2L), concertReservation.getSeatIds()),
                () -> assertEquals(50000L, concertReservation.getTotalPrice()),
                () -> assertEquals(ConcertReservationStatus.TEMPORARY_RESERVED, concertReservation.getStatus()),
                () -> assertEquals(now, concertReservation.getReservationAt())
        );
    }

    @Test
    void 임시예약_상태에서_결제완료_처리_성공() {
        // given
        ConcertReservation reservation = ConcertReservation.builder()
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .build();

        // when
        reservation.complete();

        // then
        assertEquals(ConcertReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertReservationStatus.class, names = {"CONFIRMED", "CANCELED"})
    void 임시예약_아닌경우_결제완료_처리_실패(ConcertReservationStatus status) {
        // given
        ConcertReservation reservation = ConcertReservation.builder()
                .status(status)
                .build();

        // when & then
        thenThrownBy(reservation::complete)
                .isInstanceOf(InvalidConcertReservationStatusException.class)
                .hasMessage("임시 예약 상태인 경우만 결제 완료 처리할 수 있습니다.");
    }

    @Test
    void 임시예약_상태에서_취소_처리_성공() {
        // given
        ConcertReservation reservation = ConcertReservation.builder()
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .build();

        // when
        reservation.cancelTemporaryReservation();

        // then
        assertEquals(ConcertReservationStatus.CANCELED, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertReservationStatus.class, names = {"CONFIRMED", "CANCELED"})
    void 임시예약_아닌경우_취소_처리_실패(ConcertReservationStatus status) {
        // given
        ConcertReservation reservation = ConcertReservation.builder()
                .status(status)
                .build();

        // when & then
        thenThrownBy(reservation::cancelTemporaryReservation)
                .isInstanceOf(InvalidConcertReservationStatusException.class)
                .hasMessage("임시 예약 상태인 경우만 취소할 수 있습니다.");
    }
}