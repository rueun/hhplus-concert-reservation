package com.hhplus.concertreservation.concert.domain.model.entity;

import com.hhplus.concertreservation.concert.domain.exception.InvalidConcertReservationStatusException;
import com.hhplus.concertreservation.concert.domain.model.dto.command.ReserveConcertCommand;
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
    private Long concertId;
    private Long concertSessionId;
    private List<Long> seatIds;
    private ConcertReservationStatus status;
    private long totalPrice;
    private LocalDateTime reservationAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ConcertReservation create(final ReserveConcertCommand command, final long totalPrice, final LocalDateTime reservationAt) {
        return ConcertReservation.builder()
                .userId(command.userId())
                .concertId(command.concertId())
                .concertSessionId(command.concertSessionId())
                .seatIds(command.seatIds())
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .totalPrice(totalPrice)
                .reservationAt(reservationAt)
                .build();
    }

    public boolean isTemporaryReserved() {
        return this.status == ConcertReservationStatus.TEMPORARY_RESERVED;
    }

    public void complete() {
        if (this.status != ConcertReservationStatus.TEMPORARY_RESERVED) {
            throw new InvalidConcertReservationStatusException("임시 예약 상태인 경우만 결제 완료 처리할 수 있습니다.");
        }

        this.status = ConcertReservationStatus.CONFIRMED;
    }

    public void cancelTemporaryReservation() {
        if (this.status != ConcertReservationStatus.TEMPORARY_RESERVED) {
            throw new InvalidConcertReservationStatusException("임시 예약 상태인 경우만 취소할 수 있습니다.");
        }

        this.status = ConcertReservationStatus.CANCELED;
    }
}
