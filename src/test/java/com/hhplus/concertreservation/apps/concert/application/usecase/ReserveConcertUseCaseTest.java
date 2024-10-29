package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertReservationStatus;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import com.hhplus.concertreservation.common.time.FakeTimeProvider;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("콘서트 좌석 예약 유스케이스 통합 테스트")
@SpringBootTest
class ReserveConcertUseCaseTest {

    @Autowired
    private ReserveConcertUseCase reserveConcertUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private ConcertWriter concertWriter;

    @Autowired
    private ConcertReader concertReader;

    @Test
    void 콘서트_좌석_예약_성공_예약_좌석을_반환한다() {

        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.now())
                .reservationCloseAt(LocalDateTime.now().plusDays(30))
                .build();

        ConcertSession concertSession = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .version(0L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .price(10000)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .version(0L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .price(20000)
                .build();

        userWriter.save(user);
        concertWriter.save(concert);
        concertWriter.save(concertSession);
        concertWriter.saveAll(List.of(concertSeat1, concertSeat2));

        // when
        final ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));
        final ConcertReservationInfo concertReservationInfo = reserveConcertUseCase.reserveConcert(command);

        // then
        assertAll(
                () -> assertThat(concertReservationInfo.getId()).isNotNull(),
                () -> assertThat(concertReservationInfo.getUserId()).isEqualTo(1L),
                () -> assertThat(concertReservationInfo.getConcertId()).isEqualTo(1L),
                () -> assertThat(concertReservationInfo.getConcertSessionId()).isEqualTo(1L),
                () -> assertThat(concertReservationInfo.getSeats()).hasSize(2),
                () -> assertThat(concertReservationInfo.getStatus()).isEqualTo(ConcertReservationStatus.TEMPORARY_RESERVED),
                () -> assertThat(concertReservationInfo.getTotalPrice()).isEqualTo(30000)
        );

        final List<ConcertSeat> concertSeats = concertReader.getConcertSeatsByIds(List.of(1L, 2L));
        assertAll(
                () -> assertThat(concertSeats.get(0).getStatus()).isEqualTo(ConcertSeatStatus.TEMPORARY_RESERVED),
                () -> assertThat(concertSeats.get(1).getStatus()).isEqualTo(ConcertSeatStatus.TEMPORARY_RESERVED)
        );
    }

    @Test
    void 콘서트_예약가능한_시간이_아닌_경우_예외가_발생한다() {

        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-19T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-10-30T00:00:00"))
                .build();

        userWriter.save(user);
        concertWriter.save(concert);

        final ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));

        // when & then
        thenThrownBy(() -> {
            reserveConcertUseCase.reserveConcert(command);
        })
                .isInstanceOf(CoreException.class)
                .hasMessage("예약 가능한 기간이 아닙니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.RESERVATION_PERIOD_NOT_AVAILABLE);

    }

    @Test
    void 콘서트_좌석이_이미_예약된_경우_예외가_발생한다() {

        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-10-30T00:00:00"))
                .build();

        ConcertSession concertSession = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .version(0L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .price(10000)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .version(0L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .price(20000)
                .build();

        userWriter.save(user);
        concertWriter.save(concert);
        concertWriter.save(concertSession);
        concertWriter.saveAll(List.of(concertSeat1, concertSeat2));

        final ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));

        // when & then
        thenThrownBy(() -> {
            reserveConcertUseCase.reserveConcert(command);
        })
                .isInstanceOf(CoreException.class)
                .hasMessage("예약 가능한 좌석이 아닙니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.CONCERT_SEAT_UNAVAILABLE);
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