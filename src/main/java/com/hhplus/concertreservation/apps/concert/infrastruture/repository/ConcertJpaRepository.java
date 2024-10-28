package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
}
