package com.hhplus.concertreservation.queue.presentation.controller;

import com.hhplus.concertreservation.queue.application.usecase.CreateWaitingQueueUseCase;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.presentation.dto.request.CreateWaitingQueueRequest;
import com.hhplus.concertreservation.queue.presentation.dto.response.CreateWaitingQueueResponse;
import com.hhplus.concertreservation.queue.presentation.dto.response.GetWaitingQueueStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waiting-queues")
@RequiredArgsConstructor
public class WaitingQueueController {

    private final CreateWaitingQueueUseCase createWaitingQueueUseCase;

    @PostMapping
    public ResponseEntity<CreateWaitingQueueResponse> createWaitingQueue(
            @RequestBody CreateWaitingQueueRequest request
    ) {
        final WaitingQueue waitingQueue = createWaitingQueueUseCase.createWaitingQueue(request.userId());
        return ResponseEntity.status(201)
                .body(CreateWaitingQueueResponse.of(waitingQueue));
    }

    @GetMapping
    public ResponseEntity<GetWaitingQueueStatusResponse> getWaitingQueue(
            @RequestHeader("USER-ID") Long userId
    ) {
        return ResponseEntity.ok(new GetWaitingQueueStatusResponse(1L, 1L, QueueStatus.WAITING, 10L));
    }

}
