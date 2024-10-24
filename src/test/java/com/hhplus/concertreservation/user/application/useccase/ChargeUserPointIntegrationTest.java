package com.hhplus.concertreservation.user.application.useccase;

import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("사용자 포인트 충전 유스케이스 통합 테스트")
@SpringBootTest
class ChargeUserPointIntegrationTest {

    @Autowired
    private ChargeUserPointUseCase chargeUserPointUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private UserReader userReader;


    @Test
    void 사용자_포인트_충전_성공() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.create(1L));

        //when
        chargeUserPointUseCase.chargeUserPoint(1L, 500L);

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertThat(userPoint.getAmount()).isEqualTo(500L);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -500L})
    void 충전하려는_포인트가_0보다_작을_경우_예외_발생(long amount) {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.create(1L));

        //when & then
        assertThatThrownBy(() -> chargeUserPointUseCase.chargeUserPoint(1L, amount))
                .isInstanceOf(CoreException.class)
                .hasMessage("충전하려는 포인트는 0보다 커야 합니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.POINT_AMOUNT_INVALID);
    }
}