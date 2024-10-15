package com.hhplus.concertreservation.user.domain.model.entity;

import com.hhplus.concertreservation.common.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_point")
public class UserPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int amount;

    public UserPoint(final User user) {
        this.user = user;
        this.amount = 0;
    }

    public void charge(final int amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("The amount must be greater than 0.");
        }

        this.amount += amount;
    }

    public void use(final int amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("The amount must be greater than 0.");
        }

        if (this.amount < amount) {
            throw new IllegalArgumentException("The amount is insufficient.");
        }

        this.amount -= amount;
    }
}
