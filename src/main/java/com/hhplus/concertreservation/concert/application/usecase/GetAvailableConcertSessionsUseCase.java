package com.hhplus.concertreservation.concert.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.concert.domain.service.ConcertService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetAvailableConcertSessionsUseCase {

    private final ConcertService concertService;

    public List<ConcertSession> getAvailableConcertSessions(final Long concertId) {
        return concertService.getAvailableConcertSessions(concertId);
    }
}
