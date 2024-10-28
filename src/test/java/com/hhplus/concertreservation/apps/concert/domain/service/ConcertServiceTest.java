package com.hhplus.concertreservation.apps.concert.domain.service;

import com.hhplus.concertreservation.apps.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.apps.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertReservationStatus;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("콘서트 서비스 단위 테스트")
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
    void 콘서트_임시_예약_성공() {
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

        ConcertReservation concertReservation = ConcertReservation.create(command, 30000, LocalDateTime.now());
        given(concertWriter.saveAll(anyList())).willReturn(List.of(concertSeat1, concertSeat2));
        given(concertWriter.save(any(ConcertReservation.class))).willReturn(concertReservation);

        // when
        final ConcertReservationInfo result = concertService.reserveConcert(command);

        // then
        assertAll(
                () -> assertEquals(30000, result.getTotalPrice()),
                () -> assertEquals(2, result.getSeats().size()),
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
                .isInstanceOf(CoreException.class)
                .hasMessage("예약 가능한 기간이 아닙니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.RESERVATION_PERIOD_NOT_AVAILABLE);
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
                .isInstanceOf(CoreException.class)
                .hasMessage("예약 가능한 좌석이 아닙니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.CONCERT_SEAT_UNAVAILABLE);
    }


    @Test
    void 예약_확정_성공() {
        // given
        Long reservationId = 1L;
        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(reservationId)
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .seatIds(List.of(1L, 2L))
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .price(10000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();
        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .price(20000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        given(concertReader.getConcertReservationById(reservationId)).willReturn(concertReservation);
        given(concertReader.getConcertSeatsByIds(anyList())).willReturn(List.of(concertSeat1, concertSeat2));

        // when
        concertService.completeReservation(reservationId);

        // then
        assertAll(
                () -> assertEquals(ConcertReservationStatus.CONFIRMED, concertReservation.getStatus()),
                () -> assertEquals(ConcertSeatStatus.CONFIRMED, concertSeat1.getStatus()),
                () -> assertEquals(ConcertSeatStatus.CONFIRMED, concertSeat2.getStatus()),
                () -> then(concertWriter).should(times(1)).save(concertReservation),
                () -> then(concertWriter).should(times(1)).saveAll(List.of(concertSeat1, concertSeat2))
        );
    }


    @Test
    void 임시_예약_상태가_아닌_경우_예약_확정_실패() {
        // given
        Long reservationId = 1L;
        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(reservationId)
                .status(ConcertReservationStatus.CONFIRMED)
                .seatIds(List.of(1L, 2L))
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .price(10000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();
        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .price(20000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        given(concertReader.getConcertReservationById(reservationId)).willReturn(concertReservation);
        given(concertReader.getConcertSeatsByIds(anyList())).willReturn(List.of(concertSeat1, concertSeat2));

        // when & then
        thenThrownBy(() -> concertService.completeReservation(reservationId))
                .isInstanceOf(CoreException.class)
                .hasMessage("임시 예약 상태인 경우만 결제 완료 처리할 수 있습니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.INVALID_CONCERT_RESERVATION_STATUS);
    }


    @Test
    void 콘서트_예약_가능_시간이_아니라면_예약가능한_세션은_없다() {
        // given
        final Concert concert = Concert.builder()
                .id(1L)
                .reservationOpenAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-09-30T23:59:59"))
                .build();

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T00:00:00"));
        given(concertReader.getConcertById(1L)).willReturn(concert);

        // when
        List<ConcertSession> result = concertService.getAvailableConcertSessions(1L);

        // then
        assertEquals(0, result.size());
    }

    @Test
    void 콘서트_세션_시간이_현재_시간보다_미래인_경우에만_예약가능한_세션을_반환한다() {
        // given
        final Concert concert = Concert.builder()
                .id(1L)
                .reservationOpenAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-09-30T23:59:59"))
                .build();

        // 현재 시간 이전
        final ConcertSession concertSession1 = ConcertSession.builder()
                .id(1L)
                .concertId(1L)
                .concertAt(LocalDateTime.parse("2024-09-04T00:00:00"))
                .build();

        // 현재 시간 이후
        final ConcertSession concertSession2 = ConcertSession.builder()
                .id(2L)
                .concertId(1L)
                .concertAt(LocalDateTime.parse("2024-09-20T00:00:00"))
                .build();

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-09-05T00:00:00"));
        given(concertReader.getConcertById(1L)).willReturn(concert);
        given(concertReader.getConcertSessionsByConcertId(1L)).willReturn(List.of(concertSession1, concertSession2));

        // when
        List<ConcertSession> result = concertService.getAvailableConcertSessions(1L);

        // then
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertThat(result).extracting("id").containsExactly(2L)
        );
    }

    @Test
    void 콘서트_좌석_목록을_조회할_때_예약가능한_좌석과_예약불가능한_좌석을_구분하여_반환한다() {
        // given
        final ConcertSession concertSession = ConcertSession.builder()
                .id(1L)
                .concertId(1L)
                .concertAt(LocalDateTime.parse("2024-09-20T00:00:00"))
                .totalSeatCount(3)
                .build();

        final ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .concertSessionId(1L)
                .price(10000)
                .status(ConcertSeatStatus.AVAILABLE)
                .build();

        final ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .concertSessionId(1L)
                .price(20000)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        final ConcertSeat concertSeat3 = ConcertSeat.builder()
                .id(3L)
                .concertSessionId(1L)
                .price(30000)
                .status(ConcertSeatStatus.CONFIRMED)
                .build();

        given(concertReader.getConcertSessionById(1L)).willReturn(concertSession);
        given(concertReader.getConcertSeatsBySessionId(1L)).willReturn(List.of(concertSeat1, concertSeat2, concertSeat3));

        // when
        final var result = concertService.getConcertSeatsInfo(1L);

        // then
        assertAll(
                () -> assertEquals(concertSession, result.getConcertSession()),
                () -> assertThat(result.getAvailableSeats()).containsExactly(concertSeat1),
                () -> assertThat(result.getUnavailableSeats()).containsExactly(concertSeat2, concertSeat3),
                () -> assertEquals(3, result.getConcertSession().getTotalSeatCount())
        );
    }

    @Test
    void 임시_예약된_예약을_취소한다() {
        // given
        final ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .seatIds(List.of(1L, 2L))
                .build();

        final ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        final ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        given(concertReader.getConcertReservationById(1L)).willReturn(concertReservation);
        given(concertReader.getConcertSeatsByIds(List.of(1L, 2L))).willReturn(List.of(concertSeat1, concertSeat2));

        // when
        concertService.cancelTemporaryReservation(1L);

        // then
        assertAll(
                () -> assertEquals(ConcertReservationStatus.CANCELED, concertReservation.getStatus()),
                () -> assertEquals(ConcertSeatStatus.AVAILABLE, concertSeat1.getStatus()),
                () -> assertEquals(ConcertSeatStatus.AVAILABLE, concertSeat2.getStatus()),
                () -> then(concertWriter).should(times(1)).save(concertReservation),
                () -> then(concertWriter).should(times(1)).saveAll(List.of(concertSeat1, concertSeat2))
        );
    }

    @Test
    void 임시_예약된_상태가_아니라면_예약을_취소할_수_없다() {
        // given
        final ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .status(ConcertReservationStatus.CONFIRMED)
                .seatIds(List.of(1L, 2L))
                .build();

        final ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.CONFIRMED)
                .build();

        final ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .status(ConcertSeatStatus.CONFIRMED)
                .build();

        given(concertReader.getConcertReservationById(1L)).willReturn(concertReservation);
        given(concertReader.getConcertSeatsByIds(List.of(1L, 2L))).willReturn(List.of(concertSeat1, concertSeat2));

        // when & then
        thenThrownBy(() -> concertService.cancelTemporaryReservation(1L))
                .isInstanceOf(CoreException.class)
                .hasMessage("임시 예약 상태인 경우만 취소할 수 있습니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(ConcertErrorType.INVALID_CONCERT_RESERVATION_STATUS);
    }

}