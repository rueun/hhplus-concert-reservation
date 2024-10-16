package com.hhplus.concertreservation.common.time;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime now();
}
