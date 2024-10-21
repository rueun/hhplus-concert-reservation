package com.hhplus.concertreservation.payment.presentation.controller;

import com.hhplus.concertreservation.payment.domain.model.enums.PaymentStatus;
import com.hhplus.concertreservation.payment.presentation.dto.request.PaymentRequest;
import com.hhplus.concertreservation.payment.presentation.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    @Operation(summary = "예약 결제")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    @PostMapping
    public ResponseEntity<PaymentResponse> payReservation(
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse payReservationResponse = new PaymentResponse(1L, 100, PaymentStatus.COMPLETED);
        return ResponseEntity.status(201)
                .body(payReservationResponse);
    }
}
