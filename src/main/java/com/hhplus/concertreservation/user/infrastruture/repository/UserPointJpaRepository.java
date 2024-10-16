package com.hhplus.concertreservation.user.infrastruture.repository;

import com.hhplus.concertreservation.user.infrastruture.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long> {
}
