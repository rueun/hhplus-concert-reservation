package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;

import java.time.LocalDateTime;

public record ConcertSessionResponse(
        Long sessionId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime concertAt
) {

    public static ConcertSessionResponse of(final ConcertSession session) {
        return new ConcertSessionResponse(
                session.getId(),
                session.getConcertAt()
        );
    }
}
