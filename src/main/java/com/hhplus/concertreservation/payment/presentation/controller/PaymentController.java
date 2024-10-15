package com.hhplus.concertreservation.payment.presentation.controller;

import com.hhplus.concertreservation.payment.domain.model.vo.PaymentStatus;
import com.hhplus.concertreservation.payment.presentation.dto.request.PaymentRequest;
import com.hhplus.concertreservation.payment.presentation.dto.response.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentResponse> payReservation(
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse payReservationResponse = new PaymentResponse(1L, 100, PaymentStatus.COMPLETED);
        return ResponseEntity.ok(payReservationResponse);
    }
}
