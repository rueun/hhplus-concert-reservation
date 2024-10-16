package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.ConcertSeatUnavailableException;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class ConcertSeatTest {

    @Test
    void 좌석이_예약가능한_상태라면_임시_예약이_가능하다() {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.AVAILABLE) // 예약 가능한 상태
                .build();

        // when
        concertSeat.reserve(); // 예약 시도

        // then
        assertEquals(ConcertSeatStatus.TEMPORARY_RESERVED, concertSeat.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertSeatStatus.class, names = {"TEMPORARY_RESERVED", "PAYMENT_COMPLETED"})
    void 좌석이_예약가능한_상태가_아니라면_임시_예약이_불가능하다(ConcertSeatStatus status) {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(status) // 예약 불가능한 상태
                .build();

        // when & then
        thenThrownBy(concertSeat::reserve)
                .isInstanceOf(ConcertSeatUnavailableException.class)
                .hasMessage("예약 가능한 좌석이 아닙니다.");
    }

    @Test
    void 좌석의_예약이_가능하다면_참을_반환한다() {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.AVAILABLE)
                .build();

        // when & then
        assertTrue(concertSeat.isAvailableForReservation());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertSeatStatus.class, names = {"TEMPORARY_RESERVED", "PAYMENT_COMPLETED"})
    void 좌석의_예약이_불가능하다면_거짓을_반환한다(ConcertSeatStatus status) {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(status)
                .build();

        // when & then
        assertFalse(concertSeat.isAvailableForReservation());
    }
}