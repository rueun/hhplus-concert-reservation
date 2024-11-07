package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertReservationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {

    @Query("""
        SELECT cr
        FROM ConcertReservationEntity cr
        WHERE cr.status = 'TEMPORARY_RESERVED'
        AND cr.reservationAt <= :expirationTime
    """)
    List<ConcertReservationEntity> findTemporaryReservationsToBeExpired(@Param("expirationTime") LocalDateTime expirationTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ConcertReservationEntity c WHERE c.id = :id")
    Optional<ConcertReservationEntity> findByWithPessimisticLock(@Param("id") Long id);
}
