package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.ConcertSeatUnavailableException;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("콘서트 좌석 도메인 단위 테스트")
class ConcertSeatTest {

    @Test
    void 좌석이_예약가능한_상태라면_임시_예약이_가능하다() {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.AVAILABLE) // 예약 가능한 상태
                .build();

        // when
        concertSeat.reserveTemporary(); // 예약 시도

        // then
        assertEquals(ConcertSeatStatus.TEMPORARY_RESERVED, concertSeat.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertSeatStatus.class, names = {"TEMPORARY_RESERVED", "CONFIRMED"})
    void 좌석이_예약가능한_상태가_아니라면_임시_예약이_불가능하다(ConcertSeatStatus status) {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(status) // 예약 불가능한 상태
                .build();

        // when & then
        thenThrownBy(concertSeat::reserveTemporary)
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
        assertTrue(concertSeat.isAvailable());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertSeatStatus.class, names = {"TEMPORARY_RESERVED", "CONFIRMED"})
    void 좌석의_예약이_불가능하다면_거짓을_반환한다(ConcertSeatStatus status) {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(status)
                .build();

        // when & then
        assertFalse(concertSeat.isAvailable());
    }

    @Test
    void 임시_예약좌석_예약확정_성공() {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        // when
        concertSeat.confirm();

        // then
        assertEquals(ConcertSeatStatus.CONFIRMED, concertSeat.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = ConcertSeatStatus.class, names = {"AVAILABLE", "CONFIRMED"})
    void 임시_예약좌석_예약확정_실패(ConcertSeatStatus status) {
        // given
        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(status)
                .build();

        // when & then
        thenThrownBy(concertSeat::confirm)
                .isInstanceOf(ConcertSeatUnavailableException.class)
                .hasMessage("임시 예약된 좌석만 확정할 수 있습니다.");
    }

}