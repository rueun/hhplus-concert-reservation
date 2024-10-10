package com.hhplus.concertreservation.user.presentation.controller;

import com.hhplus.concertreservation.user.presentation.dto.request.ChargeUserBalanceRequest;
import com.hhplus.concertreservation.user.presentation.dto.response.ChargeUserBalanceResponse;
import com.hhplus.concertreservation.user.presentation.dto.response.GetUserBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // 잔액 충전
    @PostMapping("/{userId}/balance/charge")
    public ResponseEntity<ChargeUserBalanceResponse> chargeBalance(
            @PathVariable Long userId,
            @RequestBody ChargeUserBalanceRequest request
    ) {
        ChargeUserBalanceResponse response = new ChargeUserBalanceResponse(1000);
        return ResponseEntity.ok(response);
    }

    // 잔액 조회
    @GetMapping("/{userId}/balance")
    public ResponseEntity<GetUserBalanceResponse> getBalance(
            @PathVariable Long userId
    ) {
        GetUserBalanceResponse response = new GetUserBalanceResponse(1000);
        return ResponseEntity.ok(response);
    }
}
