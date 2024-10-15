package com.hhplus.concertreservation.user.infrastruture.persistence.repository;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
