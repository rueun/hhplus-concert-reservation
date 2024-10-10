package com.hhplus.concertreservation.reservation.presentation.controller;

import com.hhplus.concertreservation.reservation.domain.model.vo.PaymentStatus;
import com.hhplus.concertreservation.reservation.presentation.dto.request.PayReservationRequest;
import com.hhplus.concertreservation.reservation.presentation.dto.response.ReservationResponse.CreateReservationResponse;
import com.hhplus.concertreservation.reservation.presentation.dto.response.ReservationResponse.PayReservationResponse;
import com.hhplus.concertreservation.reservation.presentation.dto.response.ReservationResponse.ReservedSeatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {


    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(
            @RequestBody CreateReservationResponse request
    ) {
        ReservedSeatResponse reservedSeat = new ReservedSeatResponse(1L, "A1", BigDecimal.valueOf(100));
        return ResponseEntity.ok(new CreateReservationResponse(1L, BigDecimal.valueOf(100), List.of(reservedSeat)));
    }

    @PostMapping("/{reservationId}/payments")
    public ResponseEntity<PayReservationResponse> payReservation(
            @PathVariable Long reservationId,
            @RequestBody PayReservationRequest request
    ) {
        PayReservationResponse payReservationResponse = new PayReservationResponse(1L, BigDecimal.valueOf(100), PaymentStatus.COMPLETED);
        return ResponseEntity.ok(payReservationResponse);
    }
}
