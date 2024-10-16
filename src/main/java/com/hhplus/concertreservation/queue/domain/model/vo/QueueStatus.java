package com.hhplus.concertreservation.queue.domain.model.vo;

import lombok.Getter;

@Getter
public enum QueueStatus {
    WAITING("대기"),
    ACTIVATED("활성"),
    EXPIRED("만료");

    private final String value;

    QueueStatus(String value) {
        this.value = value;
    }
}
