package com.hhplus.concertreservation.concert.presentation.controller;

import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import com.hhplus.concertreservation.concert.presentation.dto.response.ConcertSeatResponse;
import com.hhplus.concertreservation.concert.presentation.dto.response.ConcertSessionResponse;
import com.hhplus.concertreservation.concert.presentation.dto.response.GetConcertSeatsResponse;
import com.hhplus.concertreservation.concert.presentation.dto.response.GetConcertSessionsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ConcertSeatResponse unAvailableSeat = new ConcertSeatResponse(2L, "A2", ConcertSeatStatus.RESERVED, BigDecimal.valueOf(1000L));
        ConcertSeatResponse availableSeat = new ConcertSeatResponse(1L, "A1",ConcertSeatStatus.AVAILABLE, BigDecimal.valueOf(1000L));

        return ResponseEntity.ok(new GetConcertSeatsResponse(100, List.of(unAvailableSeat), List.of(availableSeat)));
    }

}
