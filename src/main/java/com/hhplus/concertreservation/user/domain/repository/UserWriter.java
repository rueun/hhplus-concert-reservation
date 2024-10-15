package com.hhplus.concertreservation.user.domain.repository;

import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;

public interface UserWriter {
    UserPoint save(UserPoint userPoint);
}
