package com.hhplus.concertreservation.concert.domain.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("콘서트 세션 단위 테스트")
class ConcertSessionTest {

    @Test
    void 콘서트_세션_시간이_현재보다_미래이면_True() {
        // given
        final ConcertSession concertSession = ConcertSession.builder()
                .concertAt(LocalDateTime.parse("2024-10-01T10:00:00"))
                .build();

        // when
        final boolean result = concertSession.isAvailable(LocalDateTime.parse("2024-09-30T23:59:59"));

        // then
        assertTrue(result);
    }

    @Test
    void 콘서트_세션_시간이_현재보다_과거이면_False() {
        // given
        final ConcertSession concertSession = ConcertSession.builder()
                .concertAt(LocalDateTime.parse("2024-10-01T10:00:00"))
                .build();

        // when
        final boolean result = concertSession.isAvailable(LocalDateTime.parse("2024-10-01T10:00:01"));

        // then
        assertFalse(result);
    }
}