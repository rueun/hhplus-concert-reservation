package com.hhplus.concertreservation.concert.presentation.dto.response;

import java.util.List;

public record ReserveConcertResponse(
        Long reservationId,
        long totalPrice,
        List<ReserveSeatResponse> reservedSeats
) {

    public record ReserveSeatResponse(
            Long seatId,
            int  seatNumber,
            long price
    ) {
    }
}
