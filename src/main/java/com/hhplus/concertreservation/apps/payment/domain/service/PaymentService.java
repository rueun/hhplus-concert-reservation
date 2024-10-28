package com.hhplus.concertreservation.apps.payment.domain.service;

import com.hhplus.concertreservation.apps.payment.domain.model.dto.CreatePaymentCommand;
import com.hhplus.concertreservation.apps.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.apps.payment.domain.repository.PaymentReader;
import com.hhplus.concertreservation.apps.payment.domain.repository.PaymentWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentReader paymentReader;
    private final PaymentWriter paymentWriter;

    private final TimeProvider timeProvider;

    public Payment getPayment(final Long paymentId) {
        return paymentReader.getById(paymentId);
    }

    public Payment createPayment(final CreatePaymentCommand command) {
        final Payment payment = Payment.create(command, timeProvider.now());
        return paymentWriter.save(payment);
    }
}
