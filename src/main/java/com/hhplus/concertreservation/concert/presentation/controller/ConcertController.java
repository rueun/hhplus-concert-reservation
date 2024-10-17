package com.hhplus.concertreservation.concert.presentation.controller;

import com.hhplus.concertreservation.concert.application.usecase.GetConcertSeatsUseCase;
import com.hhplus.concertreservation.concert.application.usecase.GetAvailableConcertSessionsUseCase;
import com.hhplus.concertreservation.concert.application.usecase.ReserveConcertUseCase;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertSeatsInfo;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.concert.presentation.dto.request.ReserveConcertRequest;
import com.hhplus.concertreservation.concert.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final GetAvailableConcertSessionsUseCase getAvailableConcertSessionsUseCase;
    private final GetConcertSeatsUseCase getConcertSeatsUseCase;
    private final ReserveConcertUseCase reserveConcertUseCase;

    @GetMapping("/{concertId}/sessions")
    public ResponseEntity<GetConcertSessionsResponse> getAvailableSessions(
            @PathVariable Long concertId
    ) {
        final List<ConcertSession> availableConcertSessions = getAvailableConcertSessionsUseCase.getAvailableConcertSessions(concertId);
        return ResponseEntity.ok(GetConcertSessionsResponse.of(availableConcertSessions));
    }

    @GetMapping("/{concertId}/sessions/{sessionId}/seats")
    public ResponseEntity<GetConcertSeatsResponse> getConcertSeats(
            @PathVariable Long concertId,
            @PathVariable Long sessionId
    )
    {
        final ConcertSeatsInfo concertSeats = getConcertSeatsUseCase.getConcertSeats(sessionId);
        return ResponseEntity.ok(GetConcertSeatsResponse.of(concertSeats));
    }

    @PostMapping("/{concertId}/sessions/{sessionId}/reservations")
    public ResponseEntity<ReserveConcertResponse> reserveConcert(
            @PathVariable Long concertId,
            @PathVariable Long sessionId,
            @RequestBody ReserveConcertRequest request
    ) {
        final ConcertReservationInfo concertReservationInfo = reserveConcertUseCase.reserveConcert(request.toCommand(concertId, sessionId));
        return ResponseEntity.ok(ReserveConcertResponse.of(concertReservationInfo));
    }
}
