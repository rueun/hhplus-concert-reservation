package com.hhplus.concertreservation.payment.infrastruture.repository;

import com.hhplus.concertreservation.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.payment.domain.repository.PaymentWriter;
import com.hhplus.concertreservation.payment.infrastruture.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentJpaWriter implements PaymentWriter {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(final Payment payment) {
        return paymentJpaRepository.save(new PaymentEntity(payment))
                .toDomain();
    }
}
