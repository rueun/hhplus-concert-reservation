package com.hhplus.concertreservation.concert.infrastruture.repository;

import com.hhplus.concertreservation.concert.infrastruture.entity.ConcertSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSessionJpaRepository extends JpaRepository<ConcertSessionEntity, Long> {
}
