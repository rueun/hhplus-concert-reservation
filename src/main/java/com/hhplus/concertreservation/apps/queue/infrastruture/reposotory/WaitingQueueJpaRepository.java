package com.hhplus.concertreservation.apps.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.apps.queue.infrastruture.entity.WaitingQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueueEntity, Long> {

    Optional<WaitingQueueEntity> findByToken(final String token);


    @Query("""
                SELECT e
                FROM WaitingQueueEntity e
                WHERE e.status = 'ACTIVATED'
                ORDER BY e.id DESC
                LIMIT 1
            """)
    Optional<WaitingQueueEntity> getLatestActivatedQueue();


    @Query(value = """
        SELECT * FROM waiting_queue q
        WHERE q.status = 'WAITING'
        ORDER BY q.id ASC
        LIMIT :activationCount
    """, nativeQuery = true)
    List<WaitingQueueEntity> findWaitingQueues(final int activationCount);


    @Query("""
        SELECT wq
        FROM WaitingQueueEntity wq
        WHERE wq.status = com.hhplus.concertreservation.queue.domain.model.enums.QueueStatus.ACTIVATED
        AND wq.activatedAt <= :expirationTime
    """)
    List<WaitingQueueEntity> findWaitingQueueToBeExpired(@Param("expirationTime") LocalDateTime expirationTime);
}
