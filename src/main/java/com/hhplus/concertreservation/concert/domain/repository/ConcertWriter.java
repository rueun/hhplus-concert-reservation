
package com.hhplus.concertreservation.concert.domain.repository;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;

import java.util.List;

public interface ConcertWriter {

    List<ConcertSeat> saveAll(List<ConcertSeat> concertSeats);
    ConcertReservation save(ConcertReservation concertReservation);
}
