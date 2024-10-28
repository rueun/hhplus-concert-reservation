package com.hhplus.concertreservation.apps.concert.presentation.controller;

import com.hhplus.concertreservation.apps.concert.application.usecase.GetAvailableConcertSessionsUseCase;
import com.hhplus.concertreservation.apps.concert.application.usecase.GetConcertSeatsUseCase;
import com.hhplus.concertreservation.apps.concert.application.usecase.ReserveConcertUseCase;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertSeatsInfo;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.presentation.dto.request.ReserveConcertRequest;
import com.hhplus.concertreservation.apps.concert.presentation.dto.response.GetConcertSeatsResponse;
import com.hhplus.concertreservation.apps.concert.presentation.dto.response.GetConcertSessionsResponse;
import com.hhplus.concertreservation.apps.concert.presentation.dto.response.ReserveConcertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
@Tag(name = "Concert", description = "콘서트 API")
public class ConcertController {

    private final GetAvailableConcertSessionsUseCase getAvailableConcertSessionsUseCase;
    private final GetConcertSeatsUseCase getConcertSeatsUseCase;
    private final ReserveConcertUseCase reserveConcertUseCase;

    @Operation(summary = "콘서트 세션 조회 - 예약 가능한 세션 조회")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetConcertSessionsResponse.class)))
    @GetMapping("/{concertId}/sessions")
    public ResponseEntity<GetConcertSessionsResponse> getAvailableSessions(
            @PathVariable Long concertId
    ) {
        final List<ConcertSession> availableConcertSessions = getAvailableConcertSessionsUseCase.getAvailableConcertSessions(concertId);
        return ResponseEntity.ok(GetConcertSessionsResponse.of(availableConcertSessions));
    }

    @Operation(summary = "콘서트 좌석 조회")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetConcertSeatsResponse.class)))
    @GetMapping("/{concertId}/sessions/{sessionId}/seats")
    public ResponseEntity<GetConcertSeatsResponse> getConcertSeats(
            @PathVariable Long concertId,
            @PathVariable Long sessionId
    )
    {
        final ConcertSeatsInfo concertSeats = getConcertSeatsUseCase.getConcertSeats(sessionId);
        return ResponseEntity.ok(GetConcertSeatsResponse.of(concertSeats));
    }

    @Operation(summary = "콘서트 예약")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReserveConcertResponse.class)))
    @PostMapping("/{concertId}/sessions/{sessionId}/reservations")
    public ResponseEntity<ReserveConcertResponse> reserveConcert(
            @PathVariable Long concertId,
            @PathVariable Long sessionId,
            @RequestBody ReserveConcertRequest request
    ) {
        final ConcertReservationInfo concertReservationInfo = reserveConcertUseCase.reserveConcert(request.toCommand(concertId, sessionId));
        return ResponseEntity.status(201)
                .body(ReserveConcertResponse.of(concertReservationInfo));
    }
}
