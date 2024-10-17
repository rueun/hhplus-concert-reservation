package com.hhplus.concertreservation.user.presentation.dto.response;

import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChargeUserPointResponse(
        @Schema(name = "총 포인트") long totalAmount
) {
    public static ChargeUserPointResponse of(final UserPoint userPoint) {
        return new ChargeUserPointResponse(userPoint.getAmount());
    }
}