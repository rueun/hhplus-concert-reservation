package com.hhplus.concertreservation.queue.domain.model.entity;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("대기열 객체 단위 테스트")
class WaitingQueueTest {

    @Test
    void 대기열_생성_시_기본_상태는_대기중이다() {
        // given
        Long userId = 1L;
        String token = "test-token";

        // when
        WaitingQueue waitingQueue = new WaitingQueue(userId, token);

        // then
        assertAll(
                () -> assertEquals(userId, waitingQueue.getUserId()),
                () -> assertEquals(token, waitingQueue.getToken()),
                () -> assertEquals(QueueStatus.WAITING, waitingQueue.getStatus())
        );
    }

    @Test
    void 대기열_상태가_활성화되어_있으면_isActivated_는_참을_반환한다() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.ACTIVATED)
                .build();

        // when
        boolean isActivated = waitingQueue.isActivated();

        // then
        assertTrue(isActivated);
    }

    @Test
    void 대기열_상태가_만료되면_isExpired_는_참을_반환한다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now();
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.WAITING)
                .build();

        // when
        waitingQueue.expire(expiredAt);

        // then
        assertAll(
                () -> assertEquals(QueueStatus.EXPIRED, waitingQueue.getStatus()),
                () -> assertTrue(waitingQueue.isExpired()),
                () -> assertEquals(expiredAt, waitingQueue.getExpiredAt())
        );
    }

    @Test
    void expire_메서드를_호출하면_상태는_EXPIRED로_변경된다() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.WAITING)
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        waitingQueue.expire(now);

        // then
        assertAll(
                () -> assertEquals(QueueStatus.EXPIRED, waitingQueue.getStatus()),
                () -> assertTrue(waitingQueue.isExpired()),
                () -> assertEquals(now, waitingQueue.getExpiredAt())
        );
    }

    @Test
    void activate_메서드를_호출하면_상태는_ACTIVATED로_변경된다() {
        // given
        WaitingQueue waitingQueue = WaitingQueue.builder()
                .userId(1L)
                .token("test-token")
                .status(QueueStatus.WAITING)
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        waitingQueue.activate(now);

        // then
        assertAll(
                () -> assertEquals(QueueStatus.ACTIVATED, waitingQueue.getStatus()),
                () -> assertTrue(waitingQueue.isActivated()),
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
    void 만료된_대기열을_활성화시키려는_경우_예외가_발생한다() {
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


}