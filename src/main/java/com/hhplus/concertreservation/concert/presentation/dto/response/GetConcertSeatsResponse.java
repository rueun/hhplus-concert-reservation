package com.hhplus.concertreservation.concert.presentation.dto.response;

import java.util.List;

public record GetConcertSeatsResponse(
        int totalSeatCount,
        List<ConcertSeatResponse> unavailableSeats,
        List<ConcertSeatResponse> availableSeats
) {
}
