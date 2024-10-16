package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertReservationEntity;
import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {
}
