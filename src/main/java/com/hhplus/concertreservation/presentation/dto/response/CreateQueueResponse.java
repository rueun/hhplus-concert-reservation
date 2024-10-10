package com.hhplus.concertreservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CreateQueueResponse (
        String id,
        String token,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime joinedAt
) {
}