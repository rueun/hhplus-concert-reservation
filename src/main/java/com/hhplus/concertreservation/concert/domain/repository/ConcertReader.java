package com.hhplus.concertreservation.concert.domain.repository;

import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;

public interface ConcertReader {
    Concert getConcertById(final Long concertId);
    ConcertSeat getConcertSeatById(final Long concertSeatId);
}
