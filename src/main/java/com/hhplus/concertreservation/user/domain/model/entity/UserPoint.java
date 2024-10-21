package com.hhplus.concertreservation.user.domain.model.entity;

import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.exception.UserErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPoint {

    private Long id;
    private Long userId;
    private long amount;

    public void charge(final long amount) {
        if (amount <= 0) {
            throw new CoreException(UserErrorType.POINT_AMOUNT_INVALID, "충전하려는 포인트는 0보다 커야 합니다.");
        }

        this.amount += amount;
    }

    public void use(final long amount) {

        if (amount <= 0) {
            throw new CoreException(UserErrorType.POINT_AMOUNT_INVALID, "사용하려는 포인트는 0보다 커야 합니다.");
        }

        if (this.amount < amount) {
            throw new CoreException(UserErrorType.USER_POINT_NOT_ENOUGH, "잔여 포인트가 부족합니다.");
        }

        this.amount -= amount;
    }
}
