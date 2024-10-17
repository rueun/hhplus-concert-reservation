package com.hhplus.concertreservation.queue.application.usecase;

import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.user.domain.exception.UserNotFoundException;
import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("대기열 생성 유스케이스 테스트")
@SpringBootTest
class CreateWaitingQueueUseCaseTest {

    @Autowired
    private CreateWaitingQueueUseCase createWaitingQueueUseCase;

    @Autowired
    private UserWriter userWriter;


    @Test
    void 대기열_생성_성공_대기열이_생성되고_대기열_정보를_반환한다() {
        // given
        final Long userId = 1L;
        userWriter.save(new User(userId, "user1", "user@email.com"));

        // when
        final WaitingQueue waitingQueue1 = createWaitingQueueUseCase.createWaitingQueue(userId);
        final WaitingQueue waitingQueue2 = createWaitingQueueUseCase.createWaitingQueue(userId);


        // then
        assertAll(
                () -> assertEquals(1L, waitingQueue1.getId()),
                () -> assertEquals(1L, waitingQueue1.getUserId()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueue1.getStatus()),
                () -> assertEquals(2L, waitingQueue2.getId()),
                () -> assertEquals(1L, waitingQueue2.getUserId()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueue2.getStatus())
        );
    }

    @Test
    void 대기열_생성_실패_존재하지_않는_사용자인_경우_예외가_발생한다() {
        // given
        final Long userId = 1L;

        // when & then
        thenThrownBy(() -> createWaitingQueueUseCase.createWaitingQueue(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}