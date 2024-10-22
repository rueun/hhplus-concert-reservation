package com.hhplus.concertreservation.payment.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.payment.domain.model.dto.CreatePaymentCommand;
import com.hhplus.concertreservation.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.payment.domain.service.PaymentService;
import com.hhplus.concertreservation.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class PayReservationUseCase {

    private final UserService userService;
    private final PaymentService paymentService;
    private final ConcertService concertService;
    private final WaitingQueueService waitingQueueService;

    @Transactional
    public Payment payReservation(final Long userId, final Long reservationId, final String token) {
        final ConcertReservation concertReservation = concertService.getConcertReservation(reservationId);
        if (!concertReservation.isTemporaryReserved()) {
            throw new CoreException(ConcertErrorType.INVALID_CONCERT_RESERVATION_STATUS, "예약이 임시 예약 상태가 아닙니다.");
        }
        // 유저 포인트 사용
        userService.usePoint(userId, concertReservation.getTotalPrice());
        // 결제 생성
        final Payment payment = paymentService.createPayment(
                new CreatePaymentCommand(userId, reservationId, concertReservation.getTotalPrice()));

        // 콘서트 예약 및 좌석 상태 변경
        concertService.completeReservation(reservationId);

        // 대기열 만료
        waitingQueueService.expireQueue(token);
        return payment;
    }
}
