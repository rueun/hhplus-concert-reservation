package com.hhplus.concertreservation.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.queue.infrastruture.entity.WaitingQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueueEntity, Long> {

    Optional<WaitingQueueEntity> findByToken(final String token);
}
