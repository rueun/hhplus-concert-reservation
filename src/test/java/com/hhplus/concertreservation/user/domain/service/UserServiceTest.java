package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.user.domain.exception.UserNotFoundException;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 유저를 찾을 수 없습니다");
    }


}