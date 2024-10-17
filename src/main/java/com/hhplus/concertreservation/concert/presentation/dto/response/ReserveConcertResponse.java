package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;

import java.util.List;

public record ReserveConcertResponse(
        Long reservationId,
        long totalPrice,
        List<ReserveSeatResponse> reservedSeats
) {

    public static ReserveConcertResponse of(final ConcertReservationInfo concertReservationInfo) {
        return new ReserveConcertResponse(
                concertReservationInfo.getId(),
                concertReservationInfo.getTotalPrice(),
                ReserveSeatResponse.of(concertReservationInfo.getSeats())
        );
    }
    public record ReserveSeatResponse(
            Long seatId,
            int  seatNumber,
            long price
    ) {
        public static List<ReserveSeatResponse> of(final List<ConcertSeat> seats) {
            return seats.stream()
                    .map(seat -> new ReserveSeatResponse(seat.getId(), seat.getSeatNumber(), seat.getPrice()))
                    .toList();
        }
    }
}
