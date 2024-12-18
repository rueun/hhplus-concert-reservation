package com.hhplus.concertreservation.apps.user.application.useccase;

import com.hhplus.concertreservation.apps.user.application.useccase.GetUserPointUseCase;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import com.hhplus.concertreservation.apps.user.domain.exception.UserErrorType;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("사용자 포인트 조회 유스케이스 통합 테스트")
@SpringBootTest
class GetUserPointUseCaseTest {

    @Autowired
    private GetUserPointUseCase getUserPointUseCase;

    @Autowired
    private UserWriter userWriter;

    @Test
    void 사용자_포인트_조회_성공() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.create(1L));

        //when
        final UserPoint userPoint = getUserPointUseCase.getUserPoint(1L);

        //then
        assertEquals(0L, userPoint.getAmount());
    }

    @Test
    void 사용자_정보가_없는_경우_예외_발생() {
        //when & then
        assertThatThrownBy(() -> getUserPointUseCase.getUserPoint(1L))
                .isInstanceOf(CoreException.class)
                .hasMessage("해당 유저를 찾을 수 없습니다")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.USER_NOT_FOUND);
    }

    @Test
    void 사용자_포인트_정보가_없는_경우_예외_발생() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));

        //when & then
        assertThatThrownBy(() -> getUserPointUseCase.getUserPoint(1L))
                .hasMessage("포인트를 찾을 수 없습니다")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(UserErrorType.USER_POINT_NOT_FOUND);
    }
}