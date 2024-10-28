package com.hhplus.concertreservation.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.application.usecase.GetAvailableConcertSessionsUseCase;
import com.hhplus.concertreservation.common.time.FakeTimeProvider;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("예약 가능한 콘서트 세션 조회 유스케이스 통합 테스트")
@SpringBootTest
class GetAvailableConcertSessionsUseCaseTest {

    @Autowired
    private GetAvailableConcertSessionsUseCase getAvailableConcertSessionsUseCase;

    @Autowired
    private ConcertWriter concertWriter;


    @Test
    void 예약_가능한_콘서트_세션_조회() {
        // given
        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-10-30T00:00:00"))
                .build();

        // 예약 불가 (콘서트 세션 날짜가 현재 시간보다 이전)
        ConcertSession concertSession1 = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .concertAt(LocalDateTime.parse("2024-10-17T10:00:00"))
                .build();

        // 예약 가능
        ConcertSession concertSession2 = ConcertSession.builder()
                .id(2L)
                .concertId(concert.getId())
                .concertAt(LocalDateTime.parse("2024-10-21T10:00:00"))
                .build();

        concertWriter.save(concert);
        concertWriter.save(concertSession1);
        concertWriter.save(concertSession2);

        // when
        final List<ConcertSession> availableConcertSessions = getAvailableConcertSessionsUseCase.getAvailableConcertSessions(concert.getId());

        // then
        assertAll(
                () -> assertEquals(1, availableConcertSessions.size()),
                () -> assertEquals(2L, availableConcertSessions.get(0).getId()),
                () -> assertEquals(LocalDateTime.parse("2024-10-21T10:00:00"), availableConcertSessions.get(0).getConcertAt())
        );
    }

    @Test
    void 콘서트_예약기간이_아닌경우_세션_조회_시_빈리스트() {
        // given
        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-19T00:00:00")) // 현재 시간보다 미래
                .reservationCloseAt(LocalDateTime.parse("2024-10-30T00:00:00"))
                .build();

        ConcertSession concertSession1 = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .concertAt(LocalDateTime.parse("2024-10-20T10:00:00"))
                .build();

        ConcertSession concertSession2 = ConcertSession.builder()
                .id(2L)
                .concertId(concert.getId())
                .concertAt(LocalDateTime.parse("2024-10-21T10:00:00"))
                .build();

        concertWriter.save(concert);
        concertWriter.save(concertSession1);
        concertWriter.save(concertSession2);

        // when
        final List<ConcertSession> availableConcertSessions = getAvailableConcertSessionsUseCase.getAvailableConcertSessions(concert.getId());

        // then
        assertAll(
                () -> assertEquals(0, availableConcertSessions.size())
        );
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public TimeProvider timeProvider() {
            return new FakeTimeProvider(LocalDateTime.parse("2024-10-18T10:00"));
        }
    }
}