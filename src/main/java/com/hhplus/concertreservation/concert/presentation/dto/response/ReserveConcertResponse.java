package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReserveConcertResponse(
        @Schema(name = "예약 ID")
        Long reservationId,
        @Schema(name = "총 금액")
        long totalPrice,
        @Schema(name = "예약된 좌석 목록")
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
            @Schema(name = "좌석 ID")
            Long seatId,
            @Schema(name = "좌석 번호")
            int  seatNumber,
            @Schema(name = "좌석 가격")
            long price
    ) {
        public static List<ReserveSeatResponse> of(final List<ConcertSeat> seats) {
            return seats.stream()
                    .map(seat -> new ReserveSeatResponse(seat.getId(), seat.getSeatNumber(), seat.getPrice()))
                    .toList();
        }
    }
}
