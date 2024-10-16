package com.hhplus.concertreservation.user.domain.repository;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;

public interface UserReader {

    User getById(Long userId);
    boolean existsById(Long userId);
    UserPoint getUserPointByUserId(Long userId);
}
