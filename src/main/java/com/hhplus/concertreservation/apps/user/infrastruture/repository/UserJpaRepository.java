package com.hhplus.concertreservation.apps.user.infrastruture.repository;

import com.hhplus.concertreservation.apps.user.infrastruture.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
