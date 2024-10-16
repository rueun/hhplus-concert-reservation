package com.hhplus.concertreservation.user.domain.repository;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;

public interface UserWriter {

    User save(User user);
    UserPoint saveUserPoint(UserPoint userPoint);

}
