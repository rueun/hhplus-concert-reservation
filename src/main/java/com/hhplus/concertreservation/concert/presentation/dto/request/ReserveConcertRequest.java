package com.hhplus.concertreservation.concert.presentation.dto.request;

import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;

import java.util.List;

public record ReserveConcertRequest(
        Long userId,
        List<Long> seatIds
) {
    public ReserveConcertCommand toCommand(final Long concertId, final Long sessionId) {
        return new ReserveConcertCommand(userId, concertId, sessionId, seatIds);
    }
}
