package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;

public record ConcertSeatResponse(
        Long seatId,
        int number,
        ConcertSeatStatus status,
        long price
) {

    public static ConcertSeatResponse of(final ConcertSeat seat) {
        return new ConcertSeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getStatus(),
                seat.getPrice()
        );
    }
}
