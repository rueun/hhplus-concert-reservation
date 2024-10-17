package com.hhplus.concertreservation.payment.infrastruture.entity;

import com.hhplus.concertreservation.common.auditing.BaseEntity;
import com.hhplus.concertreservation.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.payment.domain.model.vo.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "total_price")
    private long totalPrice;

    @Column(name = "payment_at")
    LocalDateTime paymentAt;


    public PaymentEntity(final Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUserId();
        this.reservationId = payment.getReservationId();
        this.status = payment.getStatus();
        this.totalPrice = payment.getTotalPrice();
        this.paymentAt = payment.getPaymentAt();
    }

    public Payment toDomain() {
        return Payment.builder()
                .id(id)
                .userId(userId)
                .reservationId(reservationId)
                .totalPrice(totalPrice)
                .status(status)
                .paymentAt(paymentAt)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}

