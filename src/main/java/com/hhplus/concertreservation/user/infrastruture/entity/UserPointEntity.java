package com.hhplus.concertreservation.user.infrastruture.entity;

import com.hhplus.concertreservation.support.domain.auditing.BaseEntity;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_point")
public class UserPointEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private long amount;

    public UserPointEntity(final UserPoint userPoint) {
        this.id = userPoint.getId();
        this.userId = userPoint.getUserId();
        this.amount = userPoint.getAmount();
    }

    public UserPoint toDomain() {
        return UserPoint.builder()
                .id(id)
                .userId(userId)
                .amount(amount)
                .build();
    }
}
