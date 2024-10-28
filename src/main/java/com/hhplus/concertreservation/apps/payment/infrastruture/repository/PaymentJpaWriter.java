package com.hhplus.concertreservation.apps.payment.infrastruture.repository;

import com.hhplus.concertreservation.apps.payment.infrastruture.entity.PaymentEntity;
import com.hhplus.concertreservation.apps.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.apps.payment.domain.repository.PaymentWriter;
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
