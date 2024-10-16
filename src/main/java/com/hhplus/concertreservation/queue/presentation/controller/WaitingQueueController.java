package com.hhplus.concertreservation.queue.presentation.controller;

import com.hhplus.concertreservation.queue.application.usecase.CreateWaitingQueueUseCase;
import com.hhplus.concertreservation.queue.application.usecase.GetWaitingQueueUseCase;
import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
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
    private final GetWaitingQueueUseCase getWaitingQueueUseCase;

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
            @RequestHeader("QUEUE-TOKEN") String token,
            @RequestHeader("USER-ID") Long userId
    ) {
        final WaitingQueueInfo waitingQueueInfo = getWaitingQueueUseCase.getWaitingQueueInfo(token);
        return ResponseEntity.ok(GetWaitingQueueStatusResponse.of(waitingQueueInfo));
    }

}
