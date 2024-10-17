package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetConcertSessionsResponse(
        @Schema(name = "콘서트 세션 목록")
        List<ConcertSessionResponse> sessions
) {
    public static GetConcertSessionsResponse of(final List<ConcertSession> sessions) {
        return new GetConcertSessionsResponse(
                sessions.stream()
                        .map(ConcertSessionResponse::of)
                        .toList()
        );
    }
}
