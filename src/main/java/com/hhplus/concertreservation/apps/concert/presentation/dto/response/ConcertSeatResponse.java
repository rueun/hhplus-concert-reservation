package com.hhplus.concertreservation.apps.concert.presentation.dto.response;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record ConcertSeatResponse(
        @Schema(name = "좌석 ID")
        Long seatId,
        @Schema(name = "좌석 번호")
        int number,
        @Schema(name = "좌석 상태")
        ConcertSeatStatus status,
        @Schema(name = "좌석 가격")
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
