package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.ConcertSeatUnavailableException;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
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

    public void reserve() {
        if (!isAvailableForReservation()) {
            throw new ConcertSeatUnavailableException("예약 가능한 좌석이 아닙니다.");
        }
        this.status = ConcertSeatStatus.TEMPORARY_RESERVED;
    }

    public boolean isAvailableForReservation() {
        return this.status == ConcertSeatStatus.AVAILABLE;
    }
}
