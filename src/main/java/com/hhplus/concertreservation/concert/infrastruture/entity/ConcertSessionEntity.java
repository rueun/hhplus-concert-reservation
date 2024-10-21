package com.hhplus.concertreservation.concert.infrastruture.entity;

import com.hhplus.concertreservation.support.domain.auditing.BaseEntity;
import com.hhplus.concertreservation.concert.domain.model.entity.ConcertSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_session")
public class ConcertSessionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "concert_at")
    private LocalDateTime concertAt;

    @Column(name = "total_seat_count")
    private int totalSeatCount;

    public ConcertSessionEntity (final ConcertSession concertSession) {
        this.id = concertSession.getId();
        this.concertId = concertSession.getConcertId();
        this.concertAt = concertSession.getConcertAt();
        this.totalSeatCount = concertSession.getTotalSeatCount();
    }

    public ConcertSession toDomain() {
        return ConcertSession.builder()
                .id(id)
                .concertId(concertId)
                .concertAt(concertAt)
                .totalSeatCount(totalSeatCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

