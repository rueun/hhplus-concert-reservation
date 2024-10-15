package com.hhplus.concertreservation.concert.presentation.dto.request;

import java.util.List;

public record ReserveConcertRequest(
        Long userId,
        List<Long> seatIds
) {
}
