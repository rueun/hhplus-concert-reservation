package com.hhplus.concertreservation.concert.domain.model.dto.command;

import java.util.List;

public record ReserveConcertCommand(
        Long userId,
        Long concertId,
        Long concertSessionId,
        List<Long> seatIds
) {
}