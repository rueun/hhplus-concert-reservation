package com.hhplus.concertreservation.user.domain.repository;

import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;

public interface UserReader {
    boolean existsById(Long userId);

    UserPoint getUserPointByUserId(Long userId);
}
