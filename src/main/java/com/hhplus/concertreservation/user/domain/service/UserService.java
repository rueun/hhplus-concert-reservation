package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public void checkUserExist(final Long userId) {
        if (!userReader.existsById(userId)) {
            throw new CoreException(UserErrorType.USER_NOT_FOUND);
        }
    }

    public UserPoint getUserPoint(final Long userId) {
        checkUserExist(userId);
        return userReader.getUserPointByUserId(userId);
    }

    public UserPoint chargePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getUserPointByUserId(userId);
        userPoint.charge(amount);
        final UserPoint savedUserPoint = userWriter.saveUserPoint(userPoint);

        log.info("포인트 충전 완료: 사용자 ID = {}, 충전 금액 = {}", userId, amount);
        return savedUserPoint;
    }

    public UserPoint usePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getUserPointByUserId(userId);
        userPoint.use(amount);
        final UserPoint savedUserPoint = userWriter.saveUserPoint(userPoint);

        log.info("포인트 사용 완료: 사용자 ID = {}, 사용 금액 = {}", userId, amount);
        return savedUserPoint;
    }
}