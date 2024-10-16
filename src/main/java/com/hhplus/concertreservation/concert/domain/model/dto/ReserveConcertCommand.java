package com.hhplus.concertreservation.concert.domain.model.dto;

import java.util.List;

public record ReserveConcertCommand(
        Long userId,
        Long concertId,
        Long concertSessionId,
        List<Long> seatIds
) {
}
