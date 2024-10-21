package com.hhplus.concertreservation.concert.infrastruture.entity;

import com.hhplus.concertreservation.support.domain.auditing.BaseEntity;
import com.hhplus.concertreservation.concert.domain.model.entity.Concert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert")
public class ConcertEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String place;

    @Column(name = "reservation_open_at")
    private LocalDateTime reservationOpenAt;

    @Column(name = "reservation_close_at")
    private LocalDateTime reservationCloseAt;

    public ConcertEntity(final Concert concert) {
        this.id = concert.getId();
        this.name = concert.getName();
        this.place = concert.getPlace();
        this.reservationOpenAt = concert.getReservationOpenAt();
        this.reservationCloseAt = concert.getReservationCloseAt();
    }

    public Concert toDomain() {
        return Concert.builder()
                .id(id)
                .name(name)
                .place(place)
                .reservationOpenAt(reservationOpenAt)
                .reservationCloseAt(reservationCloseAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

