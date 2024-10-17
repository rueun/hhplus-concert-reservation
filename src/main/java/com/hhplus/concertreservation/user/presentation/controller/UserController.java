package com.hhplus.concertreservation.user.presentation.controller;

import com.hhplus.concertreservation.user.application.useccase.ChargeUserPointUseCase;
import com.hhplus.concertreservation.user.application.useccase.GetUserPointUseCase;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.presentation.dto.request.ChargeUserPointRequest;
import com.hhplus.concertreservation.user.presentation.dto.response.ChargeUserPointResponse;
import com.hhplus.concertreservation.user.presentation.dto.response.GetUserPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

    private final ChargeUserPointUseCase chargeUserPointUseCase;
    private final GetUserPointUseCase getUserPointUseCase;


    @Operation(summary = "포인트 충전")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargeUserPointResponse.class)))
    @PostMapping("/{userId}/points/charge")
    public ResponseEntity<ChargeUserPointResponse> chargeUserPoint(
            @PathVariable Long userId,
            @RequestBody ChargeUserPointRequest request
    ) {

        final UserPoint userPoint = chargeUserPointUseCase.chargeUserPoint(userId, request.amount());
        return ResponseEntity.ok(ChargeUserPointResponse.of(userPoint));
    }


    @Operation(summary = "포인트 조회")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserPointResponse.class)))
    @GetMapping("/{userId}/points")
    public ResponseEntity<GetUserPointResponse> getUserPoint(
            @PathVariable Long userId
    ) {

        final UserPoint userPoint = getUserPointUseCase.getUserPoint(userId);
        return ResponseEntity.ok(GetUserPointResponse.of(userPoint));
    }
}
