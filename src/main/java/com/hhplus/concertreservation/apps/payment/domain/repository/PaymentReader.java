package com.hhplus.concertreservation.apps.payment.domain.repository;

import com.hhplus.concertreservation.apps.payment.domain.model.entity.Payment;

public interface PaymentReader {
    Payment getById(Long paymentId);
}
