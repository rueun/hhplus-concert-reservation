package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.apps.concert.domain.model.dto.ConcertSeatsInfo;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetConcertSeatsUseCase {

    private final ConcertService concertService;

    public ConcertSeatsInfo getConcertSeats(final Long sessionId) {
        return concertService.getConcertSeatsInfo(sessionId);
    }
}
