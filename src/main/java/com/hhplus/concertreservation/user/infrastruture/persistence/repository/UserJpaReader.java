package com.hhplus.concertreservation.user.infrastruture.persistence.repository;

import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaReader implements UserReader {

    private final UserJpaRepository userJpaRepository;
    private final UserPointJpaRepository userPointJpaRepository;

    @Override
    public boolean existsById(final Long userId) {
        return userJpaRepository.findById(userId).isPresent();
    }

    @Override
    public UserPoint getUserPointByUserId(final Long userId) {
        return userPointJpaRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
