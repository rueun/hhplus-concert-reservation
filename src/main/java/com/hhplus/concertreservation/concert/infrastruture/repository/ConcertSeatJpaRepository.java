package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {

    List<ConcertSeatEntity> findAllByConcertSessionId(Long concertSessionId);
}
