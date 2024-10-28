package com.hhplus.concertreservation.apps.user.presentation.dto.response;

import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetUserPointResponse(
        @Schema(name = "총 포인트") long totalAmount
) {
    public static GetUserPointResponse of(final UserPoint userPoint) {
        return new GetUserPointResponse(userPoint.getAmount());
    }
}