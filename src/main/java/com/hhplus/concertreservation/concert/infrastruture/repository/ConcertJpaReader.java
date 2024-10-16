package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertJpaReader implements ConcertReader {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public Concert getConcertById(final Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::toDomain)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public ConcertSeat getConcertSeatById(final Long concertSeatId) {
        return concertSeatJpaRepository.findById(concertSeatId)
                .map(ConcertSeatEntity::toDomain)
                .orElseThrow(IllegalArgumentException::new);
    }
}
