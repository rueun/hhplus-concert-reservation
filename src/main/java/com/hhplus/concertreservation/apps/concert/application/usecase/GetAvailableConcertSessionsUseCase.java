package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.common.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetAvailableConcertSessionsUseCase {

    private final ConcertService concertService;

    public List<ConcertSession> getAvailableConcertSessions(final Long concertId) {
        return concertService.getAvailableConcertSessions(concertId).getSessions();
    }
}
