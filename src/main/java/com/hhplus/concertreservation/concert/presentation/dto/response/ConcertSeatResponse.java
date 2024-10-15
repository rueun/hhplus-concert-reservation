package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;

import java.math.BigDecimal;

public record ConcertSeatResponse(
        Long seatId,
        String number,
        ConcertSeatStatus status,
        int price
) {
}
