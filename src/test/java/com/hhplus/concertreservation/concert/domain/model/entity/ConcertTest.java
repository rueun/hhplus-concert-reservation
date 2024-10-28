package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("콘서트 도메인 단위 테스트")
class ConcertTest {

    @Test
    void 예약기간_내에_있는경우_참_리턴() {
        // given
        LocalDateTime now = LocalDateTime.parse("2024-10-15T10:00:00");
        LocalDateTime reservationOpenAt = LocalDateTime.parse("2024-10-14T00:00:00");
        LocalDateTime reservationCloseAt = LocalDateTime.parse("2024-10-16T00:00:00");

        Concert concert = Concert.builder()
                .id(1L)
                .reservationOpenAt(reservationOpenAt)
                .reservationCloseAt(reservationCloseAt)
                .build();

        // when
        boolean result = concert.isWithinReservationPeriod(now);

        // Then
        assertTrue(result);
    }

    @Test
    void 예약_오픈시간_이전인_경우_거짓_리턴() {
        // given
        LocalDateTime now = LocalDateTime.parse("2024-10-13T23:59:59");
        LocalDateTime reservationOpenAt = LocalDateTime.parse("2024-10-14T00:00:00");
        LocalDateTime reservationCloseAt = LocalDateTime.parse("2024-10-16T00:00:00");

        Concert concert = Concert.builder()
                .id(1L)
                .reservationOpenAt(reservationOpenAt)
                .reservationCloseAt(reservationCloseAt)
                .build();

        // when
        boolean result = concert.isWithinReservationPeriod(now);

        // Then
        assertFalse(result);
    }

    @Test
    void 예약_종료시간_이후인_경우_거짓_리턴() {
        // given
        LocalDateTime now = LocalDateTime.parse("2024-10-16T00:00:01");
        LocalDateTime reservationOpenAt = LocalDateTime.parse("2024-10-14T00:00:00");
        LocalDateTime reservationCloseAt = LocalDateTime.parse("2024-10-16T00:00:00");

        Concert concert = Concert.builder()
                .id(1L)
                .reservationOpenAt(reservationOpenAt)
                .reservationCloseAt(reservationCloseAt)
                .build();

        // when
        boolean result = concert.isWithinReservationPeriod(now);

        // Then
        assertFalse(result);
    }
}