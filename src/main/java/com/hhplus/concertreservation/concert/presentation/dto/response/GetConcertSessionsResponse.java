package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;

import java.util.List;

public record GetConcertSessionsResponse(
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
