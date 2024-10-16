package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.model.dto.ReserveConcertCommand;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertReservationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertReservation {
    private Long id;
    private Long userId;
    private Long concertSessionId;
    private List<Long> seatIds;
    private ConcertReservationStatus status;
    private long totalPrice;
    private LocalDateTime reservationAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ConcertReservation (final ReserveConcertCommand command, final long totalPrice, final LocalDateTime reservationAt) {
        this.userId = command.userId();
        this.concertSessionId = command.concertSessionId();
        this.seatIds = command.seatIds();
        this.totalPrice = totalPrice;
        this.reservationAt = reservationAt;
        this.status = ConcertReservationStatus.TEMPORARY_RESERVED;
    }
}
