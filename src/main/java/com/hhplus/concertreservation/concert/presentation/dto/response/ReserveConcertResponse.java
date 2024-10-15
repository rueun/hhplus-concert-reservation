package com.hhplus.concertreservation.concert.presentation.dto.response;

import java.util.List;

public record ReserveConcertResponse(
        Long reservationId,
        int totalPrice,
        List<ReserveSeatResponse> reservedSeats
) {

    public record ReserveSeatResponse(
            Long seatId,
            String seatNumber,
            int price
    ) {
    }
}
