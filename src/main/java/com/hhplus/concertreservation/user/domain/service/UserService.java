package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.user.domain.exception.UserNotFoundException;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public void checkUserExist(final Long userId) {
        if (!userReader.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    public UserPoint chargePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getUserPointByUserId(userId);
        userPoint.charge(amount);
        return userWriter.saveUserPoint(userPoint);
    }


    public UserPoint usePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getUserPointByUserId(userId);
        userPoint.use(amount);
        return userWriter.saveUserPoint(userPoint);
    }
}