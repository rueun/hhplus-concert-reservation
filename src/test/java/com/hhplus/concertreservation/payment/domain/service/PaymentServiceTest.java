package com.hhplus.concertreservation.payment.domain.service;

import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.payment.domain.model.dto.CreatePaymentCommand;
import com.hhplus.concertreservation.payment.domain.model.entity.Payment;
import com.hhplus.concertreservation.payment.domain.repository.PaymentReader;
import com.hhplus.concertreservation.payment.domain.repository.PaymentWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("결제 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentReader paymentReader;

    @Mock
    private PaymentWriter paymentWriter;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제_조회_성공() {
        // given
        Long paymentId = 1L;
        Payment payment = mock(Payment.class);

        given(paymentReader.getById(paymentId)).willReturn(payment);

        // when
        Payment result = paymentService.getPayment(paymentId);

        // then
        assertEquals(payment, result);
        then(paymentReader).should(times(1)).getById(paymentId);
    }


    @Test
    void 결제_내역_생성_성공() {
        // given
        CreatePaymentCommand command = new CreatePaymentCommand(1L, 1L, 10000L);
        Payment payment = mock(Payment.class);
        given(paymentWriter.save(any(Payment.class))).willReturn(payment);
        given(timeProvider.now()).willReturn(java.time.LocalDateTime.now());

        // when
        Payment result = paymentService.createPayment(command);

        // then
        assertAll(
                () -> assertEquals(payment, result),
                () -> then(paymentWriter).should(times(1)).save(any(Payment.class)),
                () -> then(timeProvider).should(times(1)).now()
        );
    }

}