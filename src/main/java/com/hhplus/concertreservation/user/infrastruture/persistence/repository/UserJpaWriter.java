package com.hhplus.concertreservation.user.infrastruture.persistence.repository;

import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaWriter implements UserWriter {

    @Override
    public UserPoint save(UserPoint userPoint) {
        return null;
    }
}
