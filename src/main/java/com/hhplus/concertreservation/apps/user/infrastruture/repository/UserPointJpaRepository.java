package com.hhplus.concertreservation.apps.user.infrastruture.repository;

import com.hhplus.concertreservation.apps.user.infrastruture.entity.UserPointEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT u FROM UserPointEntity u
            WHERE u.userId = :userId
            """)
    Optional<UserPointEntity> findByIdWithPessimisticLock(@Param("userId") Long userId);
}
