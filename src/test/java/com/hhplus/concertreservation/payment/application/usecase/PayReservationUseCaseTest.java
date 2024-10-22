package com.hhplus.concertreservation.payment.application.usecase;

import com.hhplus.concertreservation.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.enums.ConcertReservationStatus;
import com.hhplus.concertreservation.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.payment.domain.model.enums.PaymentStatus;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("결제 예약 유스케이스 통합 테스트")
@SpringBootTest
class PayReservationUseCaseTest {

    @Autowired
    private PayReservationUseCase payReservationUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private UserReader userReader;

    @Autowired
    private ConcertWriter concertWriter;

    @Autowired
    private ConcertReader concertReader;

    @Autowired
    private WaitingQueueWriter waitingQueueWriter;

    @Autowired
    private WaitingQueueReader waitingQueueReader;


    @Test
    void 임시_예약된_콘서트_예약건을_결제한다() {
        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        UserPoint userPoint = UserPoint.builder()
                .userId(user.getId())
                .amount(100000)
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .seatIds(List.of(concertSeat1.getId(), concertSeat2.getId()))
                .totalPrice(20000)
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .build();

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .token("token")
                .status(QueueStatus.ACTIVATED)
                .build();

        userWriter.save(user);
        userWriter.saveUserPoint(userPoint);

        concertWriter.saveAll(List.of(concertSeat1, concertSeat2));
        concertWriter.save(concertReservation);

        waitingQueueWriter.save(waitingQueue);

        // when
        final var payment = payReservationUseCase.payReservation(1L, 1L, "token");

        // then
        assertAll(
                () -> assertThat(payment.getId()).isNotNull(),
                () -> assertThat(payment.getUserId()).isEqualTo(1L),
                () -> assertThat(payment.getReservationId()).isEqualTo(1L),
                () -> assertThat(payment.getTotalPrice()).isEqualTo(20000),
                () -> assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID),
                () -> assertThat(payment.getPaymentAt()).isNotNull(),
                () -> assertThat(payment.getCreatedAt()).isNotNull(),
                () -> assertThat(payment.getUpdatedAt()).isNotNull()
        );

        // 포인트 차감
        final UserPoint updatedUserPoint = userReader.getUserPointByUserId(1L);
        assertThat(updatedUserPoint.getAmount()).isEqualTo(80000);

        // 결제된 예약건 상태 변경
        final ConcertReservation updatedConcertReservation = concertReader.getConcertReservationById(1L);
        assertThat(updatedConcertReservation.getStatus()).isEqualTo(ConcertReservationStatus.CONFIRMED);

        // 결제된 좌석 상태 변경
        final List<ConcertSeat> updatedConcertSeats = concertReader.getConcertSeatsByIds(List.of(1L, 2L));
        assertAll(
                () -> assertThat(updatedConcertSeats.get(0).getStatus()).isEqualTo(ConcertSeatStatus.CONFIRMED),
                () -> assertThat(updatedConcertSeats.get(1).getStatus()).isEqualTo(ConcertSeatStatus.CONFIRMED)
        );

        // 대기열 만료
        final WaitingQueue updatedWaitingQueue = waitingQueueReader.getByToken("token");
        assertThat(updatedWaitingQueue.getStatus()).isEqualTo(QueueStatus.EXPIRED);
    }

    @Test
    void 예약이_임시예약_상태가_아니면_예외가_발생한다() {
        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        UserPoint userPoint = UserPoint.builder()
                .userId(user.getId())
                .amount(100000)
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.CONFIRMED)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .status(ConcertSeatStatus.CONFIRMED)
                .build();

        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .seatIds(List.of(concertSeat1.getId(), concertSeat2.getId()))
                .totalPrice(20000)
                .status(ConcertReservationStatus.CONFIRMED)
                .build();

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .token("token")
                .status(QueueStatus.ACTIVATED)
                .build();

        userWriter.save(user);
        userWriter.saveUserPoint(userPoint);

        concertWriter.saveAll(List.of(concertSeat1, concertSeat2));
        concertWriter.save(concertReservation);

        waitingQueueWriter.save(waitingQueue);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> payReservationUseCase.payReservation(1L, 1L, "token"))
                        .isInstanceOf(CoreException.class)
                        .hasMessage("예약이 임시 예약 상태가 아닙니다.")
                        .extracting(e -> ((CoreException) e).getErrorType())
                        .isEqualTo(ConcertErrorType.INVALID_CONCERT_RESERVATION_STATUS)
        );
    }

    @Test
    void 유저_포인트가_부족하면_예외가_발생한다() {
        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        UserPoint userPoint = UserPoint.builder()
                .userId(user.getId())
                .amount(10000)
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .seatIds(List.of(concertSeat1.getId(), concertSeat2.getId()))
                .totalPrice(20000)
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .build();

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .token("token")
                .status(QueueStatus.ACTIVATED)
                .build();

        userWriter.save(user);
        userWriter.saveUserPoint(userPoint);

        concertWriter.saveAll(List.of(concertSeat1, concertSeat2));
        concertWriter.save(concertReservation);

        waitingQueueWriter.save(waitingQueue);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> payReservationUseCase.payReservation(1L, 1L, "token"))
                        .isInstanceOf(CoreException.class)
                        .hasMessage("잔여 포인트가 부족합니다.")
                        .extracting(e -> ((CoreException) e).getErrorType())
                        .isEqualTo(UserErrorType.USER_POINT_NOT_ENOUGH)
        );
    }

}