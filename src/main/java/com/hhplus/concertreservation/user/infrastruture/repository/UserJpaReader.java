package com.hhplus.concertreservation.user.infrastruture.repository;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.infrastruture.entity.UserEntity;
import com.hhplus.concertreservation.user.infrastruture.entity.UserPointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaReader implements UserReader {

    private final UserJpaRepository userJpaRepository;
    private final UserPointJpaRepository userPointJpaRepository;

    @Override
    public User getById(final Long userId) {
        return userJpaRepository.findById(userId)
                .map(UserEntity::toDomain)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean existsById(final Long userId) {
        return userJpaRepository.findById(userId).isPresent();
    }

    @Override
    public UserPoint getUserPointByUserId(final Long userId) {
        return userPointJpaRepository.findById(userId)
                .map(UserPointEntity::toDomain)
                .orElseThrow(IllegalArgumentException::new);
    }
}
