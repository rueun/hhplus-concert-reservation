package com.hhplus.concertreservation.apps.queue.domain.service;

import com.hhplus.concertreservation.apps.queue.domain.service.WaitingQueueService;
import com.hhplus.concertreservation.common.time.TimeProvider;
import com.hhplus.concertreservation.common.uuid.UUIDGenerator;
import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.model.dto.WaitingQueueInfo;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("대기열 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueReader waitingQueueReader;

    @Mock
    private WaitingQueueWriter waitingQueueWriter;

    @Mock
    private UUIDGenerator uuidGenerator;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private WaitingQueueService waitingQueueService;

    @Test
    void 대기열_토큰_생성_시_정상적으로_대기열이_생성된다() {
        // given
        final Long userId = 1L;
        final String token = "token-uuid";

        given(uuidGenerator.generate()).willReturn(token);

        WaitingQueue expectedWaitingQueue = new WaitingQueue(userId, token);
        given(waitingQueueWriter.createWaitingQueue(any(WaitingQueue.class))).willReturn(expectedWaitingQueue);

        // when
        final WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(userId);

        // then
        assertAll(
                () -> assertEquals(userId, waitingQueue.getUserId()),
                () -> assertEquals(token, waitingQueue.getToken()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueue.getStatus()),
                () -> then(uuidGenerator).should(times(1)).generate()
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
                () -> then(waitingQueueReader).should(times(1)).getByToken(token)
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

        // when
        final WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(token);

        // then
        assertAll(
                () -> assertEquals(100L, waitingQueueInfo.waitingNumber()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueueInfo.status()),
                () -> then(waitingQueueReader).should(times(1)).getByToken(token)
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
                .isInstanceOf(CoreException.class)
                .hasMessage("대기열이 만료되었습니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED);

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
                .isInstanceOf(CoreException.class)
                .hasMessage("대기열이 활성상태가 아닙니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_NOT_ACTIVATED);
    }

    @Test
    void 대기열이_정상적으로_활성화된다() {
        // given
        String token = "test-token";
        LocalDateTime now = LocalDateTime.now();

        // Mock 대기열 생성
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token(token)
                .status(QueueStatus.WAITING)
                .build();

        when(waitingQueueReader.getByToken(token)).thenReturn(waitingQueue);
        when(timeProvider.now()).thenReturn(now);

        // when
        waitingQueueService.activateQueue(token);

        // then
        assertAll(
                () -> assertEquals(QueueStatus.ACTIVATED, waitingQueue.getStatus()),
                () -> assertEquals(now, waitingQueue.getActivatedAt())
        );
    }

    @Test
    void 이미_활성화된_대기열을_활성화_시키는_경우_활성화_예외가_발생한다() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.ACTIVATED)
                .build();

        // when & then
        thenThrownBy(() -> waitingQueue.activate(LocalDateTime.now()))
                .isInstanceOf(CoreException.class)
                .hasMessage("이미 활성화된 대기열입니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_ALREADY_ACTIVATED);
    }

    @Test
    void 만료된_대기열을_활성화_시키는_경우_활성화_예외가_발생한다() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.EXPIRED)
                .build();

        // when & then
        thenThrownBy(() -> waitingQueue.activate(LocalDateTime.now()))
                .isInstanceOf(CoreException.class)
                .hasMessage("만료된 대기열은 활성화할 수 없습니다.")
                .extracting(e -> ((CoreException) e).getErrorType())
                .isEqualTo(WaitingQueueErrorType.WAITING_QUEUE_EXPIRED);
    }


    @Test
    void 대기열이_정상적으로_만료된다() {
        // given
        String token = "test-token";
        LocalDateTime now = LocalDateTime.now();

        // Mock 대기열 생성
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token(token)
                .status(QueueStatus.ACTIVATED)
                .build();


        when(waitingQueueReader.getByToken(token)).thenReturn(waitingQueue);
        when(timeProvider.now()).thenReturn(now);

        // when
        waitingQueueService.expireActiveQueue(token);

        // then
        assertAll(
                () -> assertEquals(QueueStatus.EXPIRED, waitingQueue.getStatus()),
                () -> assertEquals(now, waitingQueue.getExpiredAt())
        );
    }

}