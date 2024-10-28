package com.hhplus.concertreservation.apps.payment.infrastruture.repository;

import com.hhplus.concertreservation.apps.payment.infrastruture.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
