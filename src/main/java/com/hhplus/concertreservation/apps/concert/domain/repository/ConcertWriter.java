
package com.hhplus.concertreservation.apps.concert.domain.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;

import java.util.List;

public interface ConcertWriter {

    List<ConcertSeat> saveAll(List<ConcertSeat> concertSeats);
    Concert save(Concert concert);
    ConcertSession save(ConcertSession concertSession);
    ConcertSeat save(ConcertSeat concertSeat);
    ConcertReservation save(ConcertReservation concertReservation);
}
