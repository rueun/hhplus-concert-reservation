package com.hhplus.concertreservation.queue.presentation.controller;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.presentation.dto.request.CreateQueueRequest;
import com.hhplus.concertreservation.queue.presentation.dto.response.QueueResponse.CreateQueueResponse;
import com.hhplus.concertreservation.queue.presentation.dto.response.QueueResponse.GetQueueStatusResponse;
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
        return ResponseEntity.ok(new GetQueueStatusResponse(1L, 1L, QueueStatus.WAITING, 10L));
    }

}
