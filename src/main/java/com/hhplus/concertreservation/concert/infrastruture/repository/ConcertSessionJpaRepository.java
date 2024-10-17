package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSessionJpaRepository extends JpaRepository<ConcertSessionEntity, Long> {

    List<ConcertSessionEntity> findAllByConcertId(Long concertId);
}
