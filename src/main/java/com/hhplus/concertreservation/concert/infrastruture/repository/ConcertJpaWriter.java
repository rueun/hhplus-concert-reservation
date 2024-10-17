package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertReservationEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSessionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertJpaWriter implements ConcertWriter {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertSessionJpaRepository concertSessionJpaRepository;
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
    public Concert save(final Concert concert) {
        return concertJpaRepository.save(new ConcertEntity(concert))
                .toDomain();
    }

    @Override
    public ConcertSession save(final ConcertSession concertSession) {
        return concertSessionJpaRepository.save(new ConcertSessionEntity(concertSession))
                .toDomain();
    }

    @Override
    public ConcertSeat save(final ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(new ConcertSeatEntity(concertSeat))
                .toDomain();
    }

    @Override
    public ConcertReservation save(final ConcertReservation concertReservation) {
        return concertReservationJpaRepository.save(new ConcertReservationEntity(concertReservation))
                .toDomain();
    }
}
