package com.hhplus.concertreservation.concert.infrastruture.entity;

import com.hhplus.concertreservation.common.auditing.BaseEntity;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.concert.domain.model.vo.ConcertSeatStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_seat")
public class ConcertSeatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concert_session_id")
    private Long concertSessionId;

    @Column(name = "seat_number")
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private ConcertSeatStatus status;

    private long price;

    public ConcertSeatEntity (final ConcertSeat concertSeat) {
        this.id = concertSeat.getId();
        this.concertSessionId = concertSeat.getConcertSessionId();
        this.seatNumber = concertSeat.getSeatNumber();
        this.status = concertSeat.getStatus();
        this.price = concertSeat.getPrice();
    }

    public ConcertSeat toDomain() {
        return ConcertSeat.builder()
                .id(id)
                .concertSessionId(concertSessionId)
                .seatNumber(seatNumber)
                .status(status)
                .price(price)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

