package com.hhplus.concertreservation.apps.user.application.useccase;

import com.hhplus.concertreservation.common.UseCase;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.apps.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ChargeUserPointUseCase {

    private final UserService userService;

    public UserPoint chargeUserPoint(final Long userId, final long amount) {
        return userService.chargePoint(userId, amount);
    }
}
