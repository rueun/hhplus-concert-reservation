package com.hhplus.concertreservation.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChargeUserPointResponse(
        @Schema(name = "총 포인트") int totalAmount
) {
}