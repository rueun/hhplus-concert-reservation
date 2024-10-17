package com.hhplus.concertreservation.payment.infrastruture.repository;

import com.hhplus.concertreservation.payment.infrastruture.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
