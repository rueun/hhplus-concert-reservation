package com.hhplus.concertreservation.apps.concert.domain.model.dto;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertSeatsInfo {
    private final ConcertSession concertSession;
    private final List<ConcertSeat> availableSeats;
    private final List<ConcertSeat> unavailableSeats;

    public static ConcertSeatsInfo of(final ConcertSession concertSession, List<ConcertSeat> availableSeats, List<ConcertSeat> unavailableSeats) {
        return new ConcertSeatsInfo(concertSession, availableSeats, unavailableSeats);
    }
}
