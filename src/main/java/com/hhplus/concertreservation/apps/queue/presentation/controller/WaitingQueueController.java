package com.hhplus.concertreservation.apps.queue.presentation.controller;

import com.hhplus.concertreservation.apps.queue.application.usecase.CreateWaitingQueueUseCase;
import com.hhplus.concertreservation.apps.queue.application.usecase.GetWaitingQueueUseCase;
import com.hhplus.concertreservation.apps.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.presentation.dto.request.CreateWaitingQueueRequest;
import com.hhplus.concertreservation.apps.queue.presentation.dto.response.CreateWaitingQueueResponse;
import com.hhplus.concertreservation.apps.queue.presentation.dto.response.GetWaitingQueueStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waiting-queues")
@RequiredArgsConstructor
@Tag(name = "WaitingQueue", description = "대기열 API")
public class WaitingQueueController {

    private final CreateWaitingQueueUseCase createWaitingQueueUseCase;
    private final GetWaitingQueueUseCase getWaitingQueueUseCase;

    @Operation(summary = "대기열 생성")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateWaitingQueueResponse.class)))
    @PostMapping
    public ResponseEntity<CreateWaitingQueueResponse> createWaitingQueue(
            @RequestBody CreateWaitingQueueRequest request
    ) {
        final WaitingQueue waitingQueue = createWaitingQueueUseCase.createWaitingQueue(request.userId());
        return ResponseEntity.status(201)
                .body(CreateWaitingQueueResponse.of(waitingQueue));
    }

    @Operation(summary = "대기열 조회")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetWaitingQueueStatusResponse.class)))
    @GetMapping
    public ResponseEntity<GetWaitingQueueStatusResponse> getWaitingQueue(
            @RequestHeader("QUEUE-TOKEN") String token
    ) {
        final WaitingQueueInfo waitingQueueInfo = getWaitingQueueUseCase.getWaitingQueueInfo(token);
        return ResponseEntity.ok(GetWaitingQueueStatusResponse.of(waitingQueueInfo));
    }

}
