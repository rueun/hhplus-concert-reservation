package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.domain.exception.ConcertNotFoundException;
import com.hhplus.concertreservation.concert.domain.exception.ConcertReservationNotFoundException;
import com.hhplus.concertreservation.concert.domain.exception.ConcertSeatNotFoundException;
import com.hhplus.concertreservation.concert.domain.exception.ConcertSesstionNotFoundException;
import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertReservationEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSessionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertJpaReader implements ConcertReader {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertSessionJpaRepository concertSessionJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertReservationJpaRepository concertReservationJpaRepository;

    @Override
    public Concert getConcertById(final Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::toDomain)
                .orElseThrow(ConcertNotFoundException::new);
    }

    @Override
    public ConcertSession getConcertSessionById(final Long concertSessionId) {
        return concertSessionJpaRepository.findById(concertSessionId)
                .map(ConcertSessionEntity::toDomain)
                .orElseThrow(ConcertSesstionNotFoundException::new);
    }

    @Override
    public ConcertSeat getConcertSeatById(final Long concertSeatId) {
        return concertSeatJpaRepository.findById(concertSeatId)
                .map(ConcertSeatEntity::toDomain)
                .orElseThrow(ConcertSeatNotFoundException::new);
    }

    @Override
    public ConcertReservation getConcertReservationById(final Long concertReservationId) {
        return concertReservationJpaRepository.findById(concertReservationId)
                .map(ConcertReservationEntity::toDomain)
                .orElseThrow(ConcertReservationNotFoundException::new);
    }

    @Override
    public List<ConcertSeat> getConcertSeatsByIds(final List<Long> seatIds) {
        return concertSeatJpaRepository.findAllById(seatIds).stream()
                .map(ConcertSeatEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertSession> getConcertSessionsByConcertId(final Long concertId) {
        return concertSessionJpaRepository.findAllByConcertId(concertId).stream()
                .map(ConcertSessionEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertSeat> getConcertSeatsBySessionId(final Long concertSessionId) {
        return concertSeatJpaRepository.findAllByConcertSessionId(concertSessionId).stream()
                .map(ConcertSeatEntity::toDomain)
                .toList();
    }
}
