package com.hhplus.concertreservation.apps.payment.domain.repository;

import com.hhplus.concertreservation.apps.payment.domain.model.entity.Payment;

public interface PaymentWriter {
    Payment save(Payment payment);
}
