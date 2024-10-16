package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;

public record ConcertSeatResponse(
        Long seatId,
        int number,
        ConcertSeatStatus status,
        long price
) {
}
