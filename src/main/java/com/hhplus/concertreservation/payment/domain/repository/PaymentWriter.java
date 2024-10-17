package com.hhplus.concertreservation.payment.domain.repository;

import com.hhplus.concertreservation.payment.domain.model.entity.Payment;

public interface PaymentWriter {
    Payment save(Payment payment);
}
