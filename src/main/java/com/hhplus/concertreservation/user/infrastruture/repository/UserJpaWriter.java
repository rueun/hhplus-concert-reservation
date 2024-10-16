package com.hhplus.concertreservation.user.infrastruture.repository;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.infrastruture.entity.UserEntity;
import com.hhplus.concertreservation.user.infrastruture.entity.UserPointEntity;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaWriter implements UserWriter {

    final UserJpaRepository userJpaRepository;
    final UserPointJpaRepository userPointJpaRepository;

    @Override
    public User save(final User user) {
        return userJpaRepository.save(new UserEntity(user))
                .toDomain();
    }

    @Override
    public UserPoint saveUserPoint(final UserPoint userPoint) {
        return userPointJpaRepository.save(new UserPointEntity(userPoint))
                .toDomain();
    }
}
