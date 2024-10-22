package com.hhplus.concertreservation.payment.domain.model.entity;

import com.hhplus.concertreservation.payment.domain.model.dto.CreatePaymentCommand;
import com.hhplus.concertreservation.payment.domain.model.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {
    private Long id;
    private Long userId;
    private Long reservationId;
    private long totalPrice;
    private PaymentStatus status;
    private LocalDateTime paymentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Payment create(final CreatePaymentCommand command, final LocalDateTime paymentAt) {
        return Payment.builder()
                .userId(command.userId())
                .reservationId(command.reservationId())
                .totalPrice(command.totalPrice())
                .status(PaymentStatus.PAID)
                .paymentAt(paymentAt)
                .build();
    }
}
