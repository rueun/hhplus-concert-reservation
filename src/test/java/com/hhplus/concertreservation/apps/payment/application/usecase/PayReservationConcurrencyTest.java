package com.hhplus.concertreservation.apps.payment.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertReservation;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertReservationStatus;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertReader;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueWriter;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.apps.user.domain.repository.UserReader;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("좌석 예약 건 결제 동시성 테스트")
@SpringBootTest
class PayReservationConcurrencyTest {

    @Autowired
    private PayReservationUseCase payReservationUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private ConcertWriter concertWriter;

    @Autowired
    private WaitingQueueWriter waitingQueueWriter;

    @Autowired
    private UserReader userReader;

    @Autowired
    private ConcertReader concertReader;

    @Autowired
    private WaitingQueueReader waitingQueueReader;


    // Logger 설정
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void 동시에_동일한_예약건에_대한_결제_요청이_들어온_경우_하나의_요청만_성공한다() {
        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        UserPoint userPoint = UserPoint.builder()
                .userId(user.getId())
                .amount(100000)
                .build();

        ConcertSeat concertSeat = ConcertSeat.builder()
                .id(1L)
                .status(ConcertSeatStatus.TEMPORARY_RESERVED)
                .build();

        ConcertReservation concertReservation = ConcertReservation.builder()
                .id(1L)
                .seatIds(List.of(concertSeat.getId()))
                .totalPrice(20000)
                .status(ConcertReservationStatus.TEMPORARY_RESERVED)
                .build();

        final WaitingQueue waitingQueue = WaitingQueue.builder()
                .token("token")
                .status(QueueStatus.ACTIVATED)
                .build();

        userWriter.save(user);
        userWriter.saveUserPoint(userPoint);
        concertWriter.saveAll(List.of(concertSeat));
        concertWriter.save(concertReservation);
        waitingQueueWriter.save(waitingQueue);


        final int threadCount = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failedCount = new AtomicInteger(0);
        List<Long> durations = new ArrayList<>();  // 각 작업의 소요 시간을 기록할 리스트
        long startTime = System.nanoTime();  // 전체 테스트 시작 시간

        // when
        IntStream.range(0, threadCount).forEach(i -> {
            executorService.submit(() -> {
                long taskStartTime = System.nanoTime();  // 작업 시작 시간
                try {
                    payReservationUseCase.payReservation(1L, 1L, "token");
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                    logger.error("결제 실패: {}", e.getMessage(), e);
                } finally {
                    long taskEndTime = System.nanoTime();  // 작업 종료 시간
                    durations.add(taskEndTime - taskStartTime);  // 작업 시간 계산 후 리스트에 추가
                    countDownLatch.countDown();
                }
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();  // 전체 테스트 종료 시간

        // then
        assertAll(
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(2, failedCount.get())
        );

        // 포인트 차감
        final UserPoint updatedUserPoint = userReader.getByUserId(1L);
        assertThat(updatedUserPoint.getAmount()).isEqualTo(80000);

        // 결제된 예약건 상태 변경
        final ConcertReservation updatedConcertReservation = concertReader.getConcertReservationById(1L);
        assertThat(updatedConcertReservation.getStatus()).isEqualTo(ConcertReservationStatus.CONFIRMED);

        // 결제된 좌석 상태 변경
        final ConcertSeat updatedConcertSeat = concertReader.getConcertSeatById(1L);
        assertThat(updatedConcertSeat.getStatus()).isEqualTo(ConcertSeatStatus.CONFIRMED);

        // 대기열 만료
        final WaitingQueue updatedWaitingQueue = waitingQueueReader.getByToken("token");
        assertThat(updatedWaitingQueue.getStatus()).isEqualTo(QueueStatus.EXPIRED);

        // 테스트 수행 시간 분석
        long totalDuration = endTime - startTime;
        logger.info("전체 테스트 수행 시간 (ms): {}", totalDuration / 1_000_000);

        long minDuration = durations.stream().min(Long::compare).orElse(0L);
        logger.info("최소 소요 작업 시간 (ms): {}", minDuration / 1_000_000);

        long maxDuration = durations.stream().max(Long::compare).orElse(0L);
        logger.info("최대 소요 작업 시간 (ms): {}", maxDuration / 1_000_000);

        double avgDuration = durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        logger.info("평균 소요 작업 시간 (ms): {}", avgDuration / 1_000_000);
    }
}