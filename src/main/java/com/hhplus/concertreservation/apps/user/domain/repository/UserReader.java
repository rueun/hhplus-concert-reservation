package com.hhplus.concertreservation.apps.user.domain.repository;

import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;

public interface UserReader {

    User getById(Long userId);
    boolean existsById(Long userId);
    UserPoint getByUserId(Long userId);

    UserPoint getByUserIdWithPessimisticLock(Long userId);
}
