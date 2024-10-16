package com.hhplus.concertreservation.queue.domain.service;

import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.queue.domain.exception.WaitingQueueExpiredException;
import com.hhplus.concertreservation.queue.domain.exception.WaitingQueueNotActivatedException;
import com.hhplus.concertreservation.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.queue.domain.repository.WaitingQueueWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
    void 대기열_토큰_생성_시_정상적으로_대기열이_생성된다() {
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

    @ParameterizedTest
    @EnumSource(value = QueueStatus.class, names = {"ACTIVATED", "EXPIRED"})
    void 대기상태가_아닌_대기열_토큰의_대기번호는_항상_0이다(QueueStatus status) {
        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token(token)
                .status(status)
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);

        // when
        final WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(token);

        // then
        assertAll(
                () -> assertEquals(0L, waitingQueueInfo.waitingNumber()),
                () -> assertEquals(status, waitingQueueInfo.status()),
                () -> then(waitingQueueReader).should(times(1)).getByToken(token),
                () -> then(waitingQueueReader).should(never()).getLatestActivatedQueue()
        );
    }


    @Test
    void 최근_활성화된_대기열이_없을_경우_대기번호는_조회된_대기열의_id값이다() {

        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token(token)
                .status(QueueStatus.WAITING)
                .id(100L) // 대기열의 id 설정
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);
        // 최근 활성화된 대기열이 없는 경우 empty 반환
        given(waitingQueueReader.getLatestActivatedQueue()).willReturn(Optional.empty());

        // when
        final WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(token);

        // then
        assertAll(
                () -> assertEquals(100L, waitingQueueInfo.waitingNumber()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueueInfo.status()),
                () -> then(waitingQueueReader).should(times(1)).getByToken(token),
                () -> then(waitingQueueReader).should(times(1)).getLatestActivatedQueue()
        );
    }


    @Test
    void 최근_활성화된_대기열이_있을_경우_대기번호는_활성화된_대기열_id와_조회된_대기열_id의_차이이다() {
        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .id(200L)
                .token(token)
                .status(QueueStatus.WAITING)
                .build();

        final WaitingQueue latestActivatedQueue = WaitingQueue.builder()
                .id(150L)
                .token("latest-activated-token")
                .status(QueueStatus.ACTIVATED)
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);
        given(waitingQueueReader.getLatestActivatedQueue()).willReturn(Optional.of(latestActivatedQueue));

        // when
        final WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(token);

        // then
        assertAll(
                () -> assertEquals(50L, waitingQueueInfo.waitingNumber()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueueInfo.status()),
                () -> then(waitingQueueReader).should(times(1)).getByToken(token),
                () -> then(waitingQueueReader).should(times(1)).getLatestActivatedQueue()
        );
    }


    @Test
    void 활성화된_대기열이면_예외가_발생하지_않는다() {
        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .id(200L)
                .token(token)
                .status(QueueStatus.ACTIVATED)
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);

        // when & then
        assertDoesNotThrow(() -> waitingQueueService.checkActivatedQueue(token));

        then(waitingQueueReader).should(times(1)).getByToken(token);

    }

    @Test
    void 만료된_대기열이면_만료된_대기열_예외가_발생한다() {
        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .id(200L)
                .token(token)
                .status(QueueStatus.EXPIRED)
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);

        // when & then
        thenThrownBy(() -> waitingQueueService.checkActivatedQueue(token))
                .isInstanceOf(WaitingQueueExpiredException.class);
    }

    @Test
    void 활성화되지_않은_대기열이면_활성화되지_않은_대기열_예외가_발생한다() {
        // given
        final String token = "token-uuid";

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .id(200L)
                .token(token)
                .status(QueueStatus.WAITING)
                .build();

        given(waitingQueueReader.getByToken(token)).willReturn(waitingQueue);

        // when & then
        thenThrownBy(() -> waitingQueueService.checkActivatedQueue(token))
                .isInstanceOf(WaitingQueueNotActivatedException.class);
    }
}