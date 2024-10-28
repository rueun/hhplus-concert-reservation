package com.hhplus.concertreservation.apps.user.infrastruture.repository;

import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import com.hhplus.concertreservation.apps.user.infrastruture.entity.UserEntity;
import com.hhplus.concertreservation.apps.user.infrastruture.entity.UserPointEntity;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
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
