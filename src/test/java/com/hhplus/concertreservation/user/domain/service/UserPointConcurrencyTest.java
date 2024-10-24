package com.hhplus.concertreservation.user.domain.service;

import com.hhplus.concertreservation.user.domain.model.entity.User;
import com.hhplus.concertreservation.user.domain.model.entity.UserPoint;
import com.hhplus.concertreservation.user.domain.repository.UserReader;
import com.hhplus.concertreservation.user.domain.repository.UserWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    void 동일한_사용자의_포인트를_동시에_충전하면_모두_충전된다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(0L)
                .build());

        int numberOfThreads = 20;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        userService.chargePoint(1L, 100L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failedCount.incrementAndGet();
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(2000L),
                () -> assertThat(successCount.get()).isEqualTo(20),
                () -> assertThat(failedCount.get()).isZero()
        );
    }


    @Test
    void 동일한_사용자의_포인트를_동시에_사용하면_모두_사용된다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(5000L)
                .build());

        int numberOfThreads = 20;
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
                () -> assertThat(userPoint.getAmount()).isEqualTo(3000L),
                () -> assertThat(successCount.get()).isEqualTo(20),
                () -> assertThat(failedCount.get()).isZero()
        );
    }



    @Test
    void 동일한_사용자의_포인트를_동시에_충전_사용할_수_있다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.builder()
                .userId(1L)
                .amount(10000L)
                .build());

        int numberOfThreads = 20;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        if (i % 2 == 0) {
                            userService.chargePoint(1L, 100L);
                        } else {
                            userService.usePoint(1L, 200L);
                        }
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failedCount.incrementAndGet();
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(9000L),
                () -> assertThat(successCount.get()).isEqualTo(20),
                () -> assertThat(failedCount.get()).isZero()
        );
    }
}