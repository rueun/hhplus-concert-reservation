package com.hhplus.concertreservation.apps.user.domain.service;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("사용자 포인트 충전 유스케이스 통합 테스트")
@SpringBootTest
class UserPointConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private UserReader userReader;

    // Logger 설정
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void 동일한_사용자의_포인트를_동시에_충전하면_모두_충전된다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(0L)
                .build());

        int numberOfThreads = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        List<Long> durations = new ArrayList<>();  // 각 작업의 소요 시간을 기록할 리스트
        long startTime = System.nanoTime();  // 전체 테스트 시작 시간

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    long taskStartTime = System.nanoTime();  // 작업 시작 시간
                    try {
                        userService.chargePoint(1L, 100L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failedCount.incrementAndGet();
                    } finally {
                        long taskEndTime = System.nanoTime();  // 작업 종료 시간
                        durations.add(taskEndTime - taskStartTime);  // 작업 시간 계산 후 리스트에 추가
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.nanoTime();  // 전체 테스트 종료 시간

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(500L),
                () -> assertThat(successCount.get()).isEqualTo(5),
                () -> assertThat(failedCount.get()).isZero()
        );


        long totalDuration = endTime - startTime;
        logger.info("전체 테스트 수행 시간 (ms): {}", totalDuration / 1_000_000);

        long minDuration = durations.stream().min(Long::compare).orElse(0L);
        logger.info("최소 소요 작업 시간 (ms): {}", minDuration / 1_000_000);

        long maxDuration = durations.stream().max(Long::compare).orElse(0L);
        logger.info("최대 소요 작업 시간 (ms): {}", maxDuration / 1_000_000);

        double avgDuration = durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        logger.info("평균 소요 작업 시간 (ms): {}", avgDuration / 1_000_000);
    }


    @Test
    void 동일한_사용자의_포인트를_동시에_사용하면_모두_사용된다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(5000L)
                .build());

        int numberOfThreads = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        userService.usePoint(1L, 100L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failedCount.incrementAndGet();
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(4500L),
                () -> assertThat(successCount.get()).isEqualTo(5),
                () -> assertThat(failedCount.get()).isZero()
        );
    }



    @Test
    void 동일한_사용자의_포인트를_동시에_충전_사용할_수_있다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(1000L)
                .build());

        int numberOfThreads = 4;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        List<Long> durations = new ArrayList<>();  // 각 작업의 소요 시간을 기록할 리스트
        long startTime = System.nanoTime();  // 전체 테스트 시작 시간

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    long taskStartTime = System.nanoTime();  // 작업 시작 시간
                    try {
                        if (i % 2 == 0) {
                            userService.chargePoint(1L, 100L);
                        } else {
                            userService.usePoint(1L, 200L);
                        }
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failedCount.incrementAndGet();
                    } finally {
                        long taskEndTime = System.nanoTime();  // 작업 종료 시간
                        durations.add(taskEndTime - taskStartTime);  // 작업 시간 계산 후 리스트에 추가
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.nanoTime();  // 전체 테스트 종료 시간

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(800L),
                () -> assertThat(successCount.get()).isEqualTo(4),
                () -> assertThat(failedCount.get()).isZero()
        );

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