package com.hhplus.concertreservation.user.application.useccase;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("사용자 포인트 동시성 테스트")
@SpringBootTest
class ChargeUserPointConcurrencyTest {

    @Autowired
    private ChargeUserPointUseCase chargeUserPointUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private UserReader userReader;

    @Test
    void 동일한_사용자의_포인트를_동시에_충전하면_모두_성공한다() {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.create(1L));

        int numberOfThreads = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        //when
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        chargeUserPointUseCase.chargeUserPoint(1L, 100L);
                        successCount.incrementAndGet(); // 성공 시 카운트 증가
                    } catch (Exception e) {
                        failedCount.incrementAndGet(); // 실패 시 카운트 증가
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);
        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(1000L),
                () -> assertThat(successCount.get()).isEqualTo(10),
                () -> assertThat(failedCount.get()).isZero()
        );
    }

    @Test
    void 동일한_사용자의_포인트를_동시에_충전하면_한_번만_성공한다2() throws InterruptedException {
        //given
        userWriter.save(new User(1L, "홍길동", "hong@email.com"));
        userWriter.saveUserPoint(UserPoint.create(1L));

        final int threadCount = 30;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            executorService.execute(() -> {
                try {
                    chargeUserPointUseCase.chargeUserPoint(1L, 100L);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });

        }
        countDownLatch.await();

        //then
        final UserPoint userPoint = userReader.getByUserId(1L);

        assertAll(
                () -> assertThat(userPoint.getAmount()).isEqualTo(3000L),
                () -> assertThat(successCount.get()).isEqualTo(30),
                () -> assertThat(failedCount.get()).isZero()
        );
    }
}