package com.hhplus.concertreservation.concert.infrastruture.entity;

import com.hhplus.concertreservation.common.auditing.BaseEntity;
import com.hhplus.concertreservation.common.converter.LongListConverter;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertReservationStatus;
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
                .concertSessionId(concertSessionId)
                .seatIds(seatIds)
                .totalPrice(totalPrice)
                .reservationAt(reservationAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
