package com.hhplus.concertreservation.apps.payment.presentation.controller;


import com.hhplus.concertreservation.apps.payment.presentation.dto.request.PaymentRequest;
import com.hhplus.concertreservation.apps.payment.presentation.dto.response.PaymentResponse;
import com.hhplus.concertreservation.apps.payment.application.usecase.PayReservationUseCase;
import com.hhplus.concertreservation.apps.payment.domain.model.entity.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PayReservationUseCase payReservationUseCase;

    @Operation(summary = "예약 결제")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    @PostMapping
    public ResponseEntity<PaymentResponse> payReservation(
            @RequestHeader("QUEUE-TOKEN") String token,
            @RequestBody PaymentRequest request
    ) {
        final Payment payment = payReservationUseCase.payReservation(request.userId(), request.reservationId(), token);
        return ResponseEntity.status(201)
                .body(PaymentResponse.of(payment));
    }
}
