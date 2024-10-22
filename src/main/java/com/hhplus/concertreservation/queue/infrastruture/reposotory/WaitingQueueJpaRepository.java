package com.hhplus.concertreservation.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
