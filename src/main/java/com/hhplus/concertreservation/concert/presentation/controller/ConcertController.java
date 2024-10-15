package com.hhplus.concertreservation.concert.presentation.controller;

import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import com.hhplus.concertreservation.concert.presentation.dto.request.ReserveConcertRequest;
import com.hhplus.concertreservation.concert.presentation.dto.response.*;
import com.hhplus.concertreservation.concert.presentation.dto.response.ReserveConcertResponse.ReserveSeatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    @GetMapping("/{concertId}/sessions")
    public ResponseEntity<GetConcertSessionsResponse> getAvailableSessions(
            @PathVariable Long concertId
    ) {
        ConcertSessionResponse session = new ConcertSessionResponse(1L, LocalDateTime.parse("2021-01-01T00:00:00"));
        return ResponseEntity.ok(new GetConcertSessionsResponse(List.of(session)));
    }

    @GetMapping("/{concertId}/sessions/{sessionId}/seats")
    public ResponseEntity<GetConcertSeatsResponse> getConcertSeats(
            @PathVariable Long concertId,
            @PathVariable Long sessionId
    )
    {
        ConcertSeatResponse unAvailableSeat = new ConcertSeatResponse(2L, "A2", ConcertSeatStatus.RESERVED, 1000);
        ConcertSeatResponse availableSeat = new ConcertSeatResponse(1L, "A1",ConcertSeatStatus.AVAILABLE, 1000);

        return ResponseEntity.ok(new GetConcertSeatsResponse(100, List.of(unAvailableSeat), List.of(availableSeat)));
    }

    @PostMapping("/{concertId}/sessions/{sessionId}/reservations")
    public ResponseEntity<ReserveConcertResponse> reserveConcert(
            @PathVariable Long concertId,
            @PathVariable Long sessionId,
            @RequestBody ReserveConcertRequest request
    ) {
        ReserveSeatResponse reservedSeat = new ReserveSeatResponse(1L, "A1", 100);
        return ResponseEntity.ok(new ReserveConcertResponse(1L, 100, List.of(reservedSeat)));
    }
}
