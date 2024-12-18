package com.hhplus.concertreservation.apps.concert.domain.repository;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;

import java.util.List;

public interface ConcertReader {
    Concert getConcertById(Long concertId);
    ConcertSession getConcertSessionById(Long concertSessionId);
    ConcertSeat getConcertSeatById(Long concertSeatId);
    ConcertSeat getConcertSeatByIdWithPesimisticLock(Long concertSeatId);
    ConcertReservation getConcertReservationById(Long concertReservationId);
    List<ConcertSeat> getConcertSeatsByIds(List<Long> seatIds);
    List<ConcertSession> getConcertSessionsByConcertId(Long concertId);
    List<ConcertSeat> getConcertSeatsBySessionId(Long concertSessionId);
    List<ConcertReservation> getTemporaryReservationsToBeExpired(int minutes);

    ConcertReservation getByIdWithPessimisticLock(Long concertReservationId);
}
