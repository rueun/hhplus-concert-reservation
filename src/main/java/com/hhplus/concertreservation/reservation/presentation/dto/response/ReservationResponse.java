package com.hhplus.concertreservation.reservation.presentation.dto.response;

import com.hhplus.concertreservation.reservation.domain.model.vo.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public class ReservationResponse {

    public record CreateReservationResponse (
            Long reservationId,
            BigDecimal totalPrice,
            List<ReservedSeatResponse> reservedSeats
    ) {
    }

    public record ReservedSeatResponse(
            Long seatId,
            String seatNumber,
            BigDecimal price
    ) {
    }

    public record PayReservationResponse(
            Long paymentId,
            BigDecimal paymentAmount,
            PaymentStatus status
    ) {
    }
}
