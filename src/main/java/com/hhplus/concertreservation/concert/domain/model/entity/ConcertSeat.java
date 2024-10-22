package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.ConcertErrorType;
import com.hhplus.concertreservation.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertSeat {
    private Long id;
    private Long concertSessionId;
    private int seatNumber;
    private ConcertSeatStatus status;
    private long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void reserveTemporary() {
        if (!isAvailable()) {
            throw new CoreException(ConcertErrorType.CONCERT_SEAT_UNAVAILABLE, "예약 가능한 좌석이 아닙니다.");
        }
        this.status = ConcertSeatStatus.TEMPORARY_RESERVED;
    }

    public void confirm() {
        if (this.status != ConcertSeatStatus.TEMPORARY_RESERVED) {
            throw new CoreException(ConcertErrorType.CONCERT_SEAT_UNAVAILABLE, "임시 예약된 좌석만 확정할 수 있습니다.");
        }

        this.status = ConcertSeatStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ConcertSeatStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == ConcertSeatStatus.AVAILABLE;
    }

    public boolean isUnavailable() {
        return !isAvailable();
    }
}
