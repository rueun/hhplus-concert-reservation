package com.hhplus.concertreservation.presentation.controller;

import com.hhplus.concertreservation.presentation.dto.request.CreateQueueRequest;
import com.hhplus.concertreservation.presentation.dto.response.CreateQueueResponse;
import com.hhplus.concertreservation.presentation.dto.response.GetQueueStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/queues")
public class QueueController {

    @PostMapping
    public ResponseEntity<CreateQueueResponse> createQueue(
            @RequestBody CreateQueueRequest request
    ) {
        return ResponseEntity.ok(new CreateQueueResponse("queueId", "token", LocalDateTime.parse("2021-01-01T00:00:00")));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GetQueueStatusResponse> getQueue(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(new GetQueueStatusResponse(1L, 1L, "WAITING", 10L));
    }

}
