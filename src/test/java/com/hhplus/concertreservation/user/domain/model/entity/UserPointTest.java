package com.hhplus.concertreservation.user.domain.model.entity;

import com.hhplus.concertreservation.user.domain.exception.PointAmountInvalidException;
import com.hhplus.concertreservation.user.domain.exception.UserPointNotEnoughException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("사용자 포인트 단위 테스트")
class UserPointTest {

    @Test
    void 포인트_충전_성공() {
        // given
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(123L)
                .amount(1000L)
                .build();

        // when
        userPoint.charge(500L);

        // then
        assertEquals(1500L, userPoint.getAmount());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -100L})
    void 포인트_충전_실패_유효하지_않은_포인트_충전(long chargeAmount) {
        // given
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(123L)
                .amount(1000L)
                .build();

        // when & then
        assertThatThrownBy(() -> userPoint.charge(chargeAmount))
                .isInstanceOf(PointAmountInvalidException.class)
                .hasMessage("충전하려는 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 포인트_사용_성공() {
        // given
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(123L)
                .amount(1000L)
                .build();

        // when
        userPoint.use(500L);

        // then
        assertEquals(500L, userPoint.getAmount());
    }


    @ParameterizedTest
    @ValueSource(longs = {0L, -100L})
    void 포인트_사용_실패_유효하지_않은_포인트_사용(long useAmount) {
        // given
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(123L)
                .amount(1000L)
                .build();

        // when & then
        assertThatThrownBy(() -> userPoint.use(useAmount))
                .isInstanceOf(PointAmountInvalidException.class)
                .hasMessage("사용하려는 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 포인트_사용_실패_잔여_포인트_부족() {
        // given
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(123L)
                .amount(1000L)
                .build();

        // when & then
        assertThatThrownBy(() -> userPoint.use(1500L))
                .isInstanceOf(UserPointNotEnoughException.class)
                .hasMessage("잔여 포인트가 부족합니다.");
    }

}