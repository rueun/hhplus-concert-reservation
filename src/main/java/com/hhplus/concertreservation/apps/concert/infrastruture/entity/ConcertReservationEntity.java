package com.hhplus.concertreservation.apps.concert.infrastruture.entity;

import com.hhplus.concertreservation.support.domain.auditing.BaseEntity;
import com.hhplus.concertreservation.common.converter.LongListConverter;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_reservation")
public class ConcertReservationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ConcertReservationStatus status;

    private Long concertId;

    private Long concertSessionId;

    @Column(name = "seat_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> seatIds;

    @Column(name = "total_price")
    private long totalPrice;

    @Column(name = "reservation_at")
    private LocalDateTime reservationAt;


    public ConcertReservationEntity (final ConcertReservation concertReservation) {
        this.id = concertReservation.getId();
        this.userId = concertReservation.getUserId();
        this.status = concertReservation.getStatus();
        this.concertId = concertReservation.getConcertId();
        this.concertSessionId = concertReservation.getConcertSessionId();
        this.seatIds = concertReservation.getSeatIds();
        this.totalPrice = concertReservation.getTotalPrice();
        this.reservationAt = concertReservation.getReservationAt();
        this.createdAt = concertReservation.getCreatedAt();
        this.updatedAt = concertReservation.getUpdatedAt();
    }

    public ConcertReservation toDomain() {
        return ConcertReservation.builder()
                .id(id)
                .userId(userId)
                .status(status)
                .concertId(concertId)
                .concertSessionId(concertSessionId)
                .seatIds(seatIds)
                .totalPrice(totalPrice)
                .reservationAt(reservationAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

