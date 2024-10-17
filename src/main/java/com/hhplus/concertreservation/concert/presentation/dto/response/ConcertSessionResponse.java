package com.hhplus.concertreservation.concert.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ConcertSessionResponse(
        @Schema(name = "세션 ID")
        Long sessionId,
        @Schema(name = "콘서트 일시")
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
