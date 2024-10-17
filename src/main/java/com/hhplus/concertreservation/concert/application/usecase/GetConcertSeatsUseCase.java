package com.hhplus.concertreservation.concert.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertSeatsInfo;
import com.hhplus.concertreservation.concert.domain.service.ConcertService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetConcertSeatsUseCase {

    private final ConcertService concertService;

    public ConcertSeatsInfo getConcertSeats(final Long sessionId) {
        return concertService.getConcertSeatsInfo(sessionId);
    }
}
