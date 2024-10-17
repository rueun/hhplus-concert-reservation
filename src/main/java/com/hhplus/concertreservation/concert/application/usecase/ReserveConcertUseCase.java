package com.hhplus.concertreservation.concert.application.usecase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.concert.domain.model.dto.ConcertReservationInfo;
import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.service.ConcertService;
import com.hhplus.concertreservation.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ReserveConcertUseCase {

    private final UserService userService;
    private final ConcertService concertService;

    public ConcertReservationInfo reserveConcert(final ReserveConcertCommand command) {
        userService.checkUserExist(command.userId());
        return concertService.reserveConcert(command);
    }
}
