package com.hhplus.concertreservation.payment.infrastruture.repository;

import com.hhplus.concertreservation.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.payment.domain.repository.PaymentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentJpaReader implements PaymentReader {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment getById(final Long paymentId) {
        paymentJpaRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다. paymentId=" + paymentId));
        return null;
    }
}
