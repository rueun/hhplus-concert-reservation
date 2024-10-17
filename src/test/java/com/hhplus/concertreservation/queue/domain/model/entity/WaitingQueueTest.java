package com.hhplus.concertreservation.queue.domain.model.entity;

import com.hhplus.concertreservation.queue.domain.model.vo.QueueStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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


}