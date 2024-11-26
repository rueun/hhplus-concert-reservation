package com.hhplus.concertreservation.apps.concert.infrastruture.repository;

import com.hhplus.concertreservation.apps.concert.infrastruture.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {

    @Query("""
            SELECT o
            FROM OutboxEntity o
            WHERE o.status = com.hhplus.concertreservation.apps.concert.domain.model.enums.OutboxStatus.INIT
            AND o.createdAt < :thresholdTime
            """
    )
    List<OutboxEntity> findAllAfterThreshold(@Param("thresholdTime") LocalDateTime thresholdTime);

    Optional<OutboxEntity> findByEventKey(final String eventKey);
}
