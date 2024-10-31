package com.hhplus.concertreservation.apps.user.domain.service;

import com.hhplus.concertreservation.apps.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.apps.user.domain.repository.UserReader;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userReader.getByUserId(userId);
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public UserPoint chargePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getByUserId(userId);
        userPoint.charge(amount);
        final UserPoint savedUserPoint = userWriter.saveUserPoint(userPoint);

        log.info("포인트 충전 완료: 사용자 ID = {}, 충전 금액 = {}", userId, amount);
        return savedUserPoint;
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public UserPoint usePoint(final Long userId, final long amount) {
        checkUserExist(userId);
        final UserPoint userPoint = userReader.getByUserId(userId);
        userPoint.use(amount);
        final UserPoint savedUserPoint = userWriter.saveUserPoint(userPoint);

        log.info("포인트 사용 완료: 사용자 ID = {}, 사용 금액 = {}", userId, amount);
        return savedUserPoint;
    }
}