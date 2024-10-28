package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.infrastruture.entity.ConcertReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {

    @Query("""
        SELECT cr
        FROM ConcertReservationEntity cr
        WHERE cr.status = 'TEMPORARY_RESERVED'
        AND cr.reservationAt <= :expirationTime
    """)
    List<ConcertReservationEntity> findTemporaryReservationsToBeExpired(@Param("expirationTime") LocalDateTime expirationTime);
}
