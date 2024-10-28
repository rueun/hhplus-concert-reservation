package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.apps.user.domain.service.UserService;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.apps.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.apps.user.domain.repository.UserReader;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("사용자 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserReader userReader;

    @Mock
    private UserWriter userWriter;

    @InjectMocks
    private UserService userService;

    @Test
    void 유저가_존재한다면_에러를_발생시키지_않는다() {
        // given
        Long userId = 1L;
        given(userReader.existsById(userId)).willReturn(true);

        // when
        userService.checkUserExist(userId);

        // then
        then(userReader).should(times(1)).existsById(userId);
    }

    @Test
    void 유저가_존재하지_않으면_에러를_발생시킨다() {
        // given
        Long userId = 1L;
        given(userReader.existsById(userId)).willReturn(false);

        // when & then
        thenThrownBy(() -> userService.checkUserExist(userId))
                .isInstanceOf(CoreException.class)
                .hasMessage("해당 유저를 찾을 수 없습니다")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.USER_NOT_FOUND);
    }

    @Test
    void 포인트_충전_성공() {
        // given
        Long userId = 1L;
        long chargeAmount = 500L;
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .build();

        given(userReader.existsById(userId)).willReturn(true);
        given(userReader.getByUserIdWithPessimisticLock(userId)).willReturn(userPoint);
        given(userWriter.saveUserPoint(any(UserPoint.class))).willReturn(userPoint);

        // when
        UserPoint updatedUserPoint = userService.chargePoint(userId, chargeAmount);

        // then
        assertAll(
                () -> assertEquals(1500L, updatedUserPoint.getAmount()),
                () -> then(userReader).should(times(1)).existsById(userId),
                () -> then(userReader).should(times(1)).getByUserIdWithPessimisticLock(userId),
                () -> then(userWriter).should(times(1)).saveUserPoint(userPoint)
        );
    }

    @Test
    void 포인트_사용_성공() {
        // given
        Long userId = 1L;
        long useAmount = 300L;
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .build();

        given(userReader.existsById(userId)).willReturn(true);
        given(userReader.getByUserIdWithPessimisticLock(userId)).willReturn(userPoint);
        given(userWriter.saveUserPoint(any(UserPoint.class))).willReturn(userPoint);

        // when
        UserPoint updatedUserPoint = userService.usePoint(userId, useAmount);

        // then
        assertAll(
                () -> assertEquals(700L, updatedUserPoint.getAmount()),
                () -> then(userReader).should(times(1)).existsById(userId),
                () -> then(userReader).should(times(1)).getByUserIdWithPessimisticLock(userId),
                () -> then(userWriter).should(times(1)).saveUserPoint(userPoint)
        );
    }

    @Test
    void 포인트_충전_실패_충전_금액_부적절() {
        // given
        Long userId = 1L;
        long chargeAmount = -500L; // 충전 금액이 0보다 작은 경우
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .build();

        given(userReader.existsById(userId)).willReturn(true);
        given(userReader.getByUserIdWithPessimisticLock(userId)).willReturn(userPoint);

        // when & then
        assertThatThrownBy(() -> userService.chargePoint(userId, chargeAmount))
                .isInstanceOf(CoreException.class)
                .hasMessage("충전하려는 포인트는 0보다 커야 합니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.POINT_AMOUNT_INVALID);

        assertAll(
                () -> then(userReader).should(times(1)).existsById(userId),
                () -> then(userReader).should(times(1)).getByUserIdWithPessimisticLock(userId),
                () -> then(userWriter).should(never()).saveUserPoint(any())
        );
    }

    @Test
    void 포인트_사용_실패_잔여_포인트_부족() {
        // given
        Long userId = 1L;
        long useAmount = 1500L; // 잔여 포인트보다 많은 금액
        UserPoint userPoint = UserPoint.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .build();

        given(userReader.existsById(userId)).willReturn(true);
        given(userReader.getByUserIdWithPessimisticLock(userId)).willReturn(userPoint);

        // when & then
        assertThatThrownBy(() -> userService.usePoint(userId, useAmount))
                .isInstanceOf(CoreException.class)
                .hasMessage("잔여 포인트가 부족합니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.USER_POINT_NOT_ENOUGH);

        assertAll(
                () -> then(userReader).should(times(1)).existsById(userId),
                () -> then(userReader).should(times(1)).getByUserIdWithPessimisticLock(userId),
                () -> then(userWriter).should(never()).saveUserPoint(any())
        );

    }


}