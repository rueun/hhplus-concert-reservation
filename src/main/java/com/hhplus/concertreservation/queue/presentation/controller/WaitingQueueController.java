package com.hhplus.concertreservation.queue.presentation.controller;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.presentation.dto.request.CreateWaitingQueueRequest;
import com.hhplus.concertreservation.queue.presentation.dto.response.WaitingQueueResponse.CreateWaitingQueueResponse;
import com.hhplus.concertreservation.queue.presentation.dto.response.WaitingQueueResponse.GetWaitingQueueStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/waiting-queues")
public class WaitingQueueController {

    @PostMapping
    public ResponseEntity<CreateWaitingQueueResponse> createWaitingQueue(
            @RequestBody CreateWaitingQueueRequest request
    ) {
        return ResponseEntity.ok(new CreateWaitingQueueResponse("queueId", "token", LocalDateTime.parse("2021-01-01T00:00:00")));
    }

    @GetMapping
    public ResponseEntity<GetWaitingQueueStatusResponse> getWaitingQueue(
            @RequestHeader("USER-ID") Long userId
    ) {
        return ResponseEntity.ok(new GetWaitingQueueStatusResponse(1L, 1L, QueueStatus.WAITING, 10L));
    }

}
