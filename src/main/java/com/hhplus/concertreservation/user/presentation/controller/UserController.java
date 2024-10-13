package com.hhplus.concertreservation.user.presentation.controller;

import com.hhplus.concertreservation.user.presentation.dto.request.ChargeUserPointRequest;
import com.hhplus.concertreservation.user.presentation.dto.response.ChargeUserPointResponse;
import com.hhplus.concertreservation.user.presentation.dto.response.GetUserPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // 잔액 충전
    @PostMapping("/{userId}/points/charge")
    public ResponseEntity<ChargeUserPointResponse> chargeUserPoint(
            @PathVariable Long userId,
            @RequestBody ChargeUserPointRequest request
    ) {
        ChargeUserPointResponse response = new ChargeUserPointResponse(1000);
        return ResponseEntity.ok(response);
    }

    // 잔액 조회
    @GetMapping("/{userId}/points")
    public ResponseEntity<GetUserPointResponse> getUserPoint(
            @PathVariable Long userId
    ) {
        GetUserPointResponse response = new GetUserPointResponse(1000);
        return ResponseEntity.ok(response);
    }
}
