package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertReservationEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertJpaWriter implements ConcertWriter {

    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertReservationJpaRepository concertReservationJpaRepository;

    @Override
    public List<ConcertSeat> saveAll(final List<ConcertSeat> concertSeats) {

        final List<ConcertSeatEntity> concertSeatEntities = concertSeats.stream()
                .map(ConcertSeatEntity::new)
                .toList();

        return concertSeatJpaRepository.saveAll(concertSeatEntities)
                .stream()
                .map(ConcertSeatEntity::toDomain)
                .toList();
    }

    @Override
    public ConcertReservation save(final ConcertReservation concertReservation) {
        return concertReservationJpaRepository.save(new ConcertReservationEntity(concertReservation))
                .toDomain();
    }
}
