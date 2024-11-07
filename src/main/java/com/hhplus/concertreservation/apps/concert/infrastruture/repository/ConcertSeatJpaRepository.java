package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertSeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {

    List<ConcertSeatEntity> findAllByConcertSessionId(Long concertSessionId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT c FROM ConcertSeatEntity c WHERE c.id = :id")
    Optional<ConcertSeatEntity> findByIdWithPesimisticLock(@Param("id") Long id);
}
