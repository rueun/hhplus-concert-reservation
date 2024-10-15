package com.hhplus.concertreservation.queue.domain.service;

import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueReader waitingQueueReader;

    @Mock
    private WaitingQueueWriter waitingQueueWriter;

    @Mock
    private UUIDGenerator uuidGenerator;

    @InjectMocks
    private WaitingQueueService waitingQueueService;

    @Test
    void 대기열_토큰을_생성할_수_있다() {
        // given
        final Long userId = 1L;
        final String token = "token-uuid";

        given(uuidGenerator.generate()).willReturn(token);

        WaitingQueue expectedWaitingQueue = new WaitingQueue(userId, token);
        given(waitingQueueWriter.save(any(WaitingQueue.class))).willReturn(expectedWaitingQueue);

        // when
        final WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(userId);

        // then
        assertAll(
                () -> assertEquals(userId, waitingQueue.getUserId()),
                () -> assertEquals(token, waitingQueue.getToken()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueue.getStatus()),
                () -> then(uuidGenerator).should(times(1)).generate(),
                () -> then(waitingQueueWriter).should(times(1)).save(any(WaitingQueue.class))
        );
    }



}