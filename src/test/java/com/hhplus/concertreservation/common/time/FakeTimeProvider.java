package com.hhplus.concertreservation.common.time;

import java.time.LocalDateTime;

public class FakeTimeProvider implements TimeProvider {

    private final LocalDateTime fakeNow;

    public FakeTimeProvider(LocalDateTime fakeNow) {
        this.fakeNow = fakeNow;
    }

    @Override
    public LocalDateTime now() {
        return fakeNow != null ? fakeNow : LocalDateTime.now();
    }
}