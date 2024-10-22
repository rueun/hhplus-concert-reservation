package com.hhplus.concertreservation.queue.application.usecase;

import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("대기열 조회 유스케이스 테스트")
@SpringBootTest
class GetWaitingQueueUseCaseTest {

    @Autowired
    private GetWaitingQueueUseCase getWaitingQueueUseCase;

    @Autowired
    private WaitingQueueWriter waitingQueueWriter;

    @Autowired
    private WaitingQueueReader waitingQueueReader;

    @Test
    void 대기열_조회_성공_대기_순서를_계산해주어_반환한다() {
        // given

        final WaitingQueue waitingQueue1 = WaitingQueue.builder()
                .id(1L)
                .token("token-uuid1")
                .status(QueueStatus.EXPIRED)
                .build();

        final WaitingQueue waitingQueue2 = WaitingQueue.builder()
                .id(2L)
                .token("token-uuid2")
                .status(QueueStatus.ACTIVATED)
                .build();

        final WaitingQueue waitingQueue3 = WaitingQueue.builder()
                .id(3L)
                .token("token-uuid3")
                .status(QueueStatus.WAITING)
                .build();

        waitingQueueWriter.save(waitingQueue1);
        waitingQueueWriter.save(waitingQueue2);
        waitingQueueWriter.save(waitingQueue3);

        // when
        final WaitingQueueInfo waitingQueueInfo1 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid1");
        final WaitingQueueInfo waitingQueueInfo2 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid2");
        final WaitingQueueInfo waitingQueueInfo3 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid3");

        // then
        assertAll(
                () -> assertEquals(0L, waitingQueueInfo1.waitingNumber()),
                () -> assertEquals(0L, waitingQueueInfo2.waitingNumber()),
                () -> assertEquals(1L, waitingQueueInfo3.waitingNumber())
        );
    }

    @Test
    void 대기열_조회_성공_활성화된_대기열이_없는_경우() {
        // given
        final WaitingQueue waitingQueue1 = WaitingQueue.builder()
                .id(1L)
                .token("token-uuid1")
                .status(QueueStatus.WAITING)
                .build();

        final WaitingQueue waitingQueue2 = WaitingQueue.builder()
                .id(2L)
                .token("token-uuid2")
                .status(QueueStatus.WAITING)
                .build();

        final WaitingQueue waitingQueue3 = WaitingQueue.builder()
                .id(3L)
                .token("token-uuid3")
                .status(QueueStatus.WAITING)
                .build();

        waitingQueueWriter.save(waitingQueue1);
        waitingQueueWriter.save(waitingQueue2);
        waitingQueueWriter.save(waitingQueue3);

        // when
        final WaitingQueueInfo waitingQueueInfo1 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid1");
        final WaitingQueueInfo waitingQueueInfo2 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid2");
        final WaitingQueueInfo waitingQueueInfo3 = getWaitingQueueUseCase.getWaitingQueueInfo("token-uuid3");

        // then
        assertAll(
                () -> assertEquals(1L, waitingQueueInfo1.waitingNumber()),
                () -> assertEquals(2L, waitingQueueInfo2.waitingNumber()),
                () -> assertEquals(3L, waitingQueueInfo3.waitingNumber())
        );
    }
}