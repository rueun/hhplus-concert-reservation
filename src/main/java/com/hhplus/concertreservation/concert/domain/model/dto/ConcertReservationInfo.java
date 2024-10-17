package com.hhplus.concertreservation.concert.domain.model.dto;

import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConcertReservationInfo {
    private final Long id;
    private final Long userId;
    private final Long concertId;
    private final Long concertSessionId;
    private final List<ConcertSeat> seats;
    private final ConcertReservationStatus status;
    private final long totalPrice;
    private final LocalDateTime reservationAt;

    public ConcertReservationInfo(final ConcertReservation concertReservation, final List<ConcertSeat> seats) {
        this.id = concertReservation.getId();
        this.userId = concertReservation.getUserId();
        this.concertId = concertReservation.getConcertId();
        this.concertSessionId = concertReservation.getConcertSessionId();
        this.seats = seats;
        this.status = concertReservation.getStatus();
        this.totalPrice = concertReservation.getTotalPrice();
        this.reservationAt = concertReservation.getReservationAt();
    }
}
