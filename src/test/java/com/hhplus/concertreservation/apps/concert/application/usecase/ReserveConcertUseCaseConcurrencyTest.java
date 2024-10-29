package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("콘서트 좌석 예약 동시성 테스트")
@SpringBootTest
class ReserveConcertUseCaseConcurrencyTest {

    @Autowired
    private ReserveConcertUseCase reserveConcertUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private ConcertWriter concertWriter;

    @BeforeEach
    void setUp() {
        User user1 = new User(1L, "사용자1", "email1@example.com");
        User user2 = new User(2L, "사용자2", "email2@example.com");
        User user3 = new User(3L, "사용자3", "email3@example.com");

        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-11-30T00:00:00"))
                .build();

        ConcertSession concertSession = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .version(0L)
                .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                .id(2L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .version(0L)
                .build();

        ConcertSeat concertSeat3 = ConcertSeat.builder()
                .id(3L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .version(0L)
                .build();


        userWriter.save(user1);
        userWriter.save(user2);
        userWriter.save(user3);

        concertWriter.save(concert);
        concertWriter.save(concertSession);
        concertWriter.saveAll(List.of(concertSeat1, concertSeat2, concertSeat3));
    }


    @Test
    void 동시에_여러_사용자가_동일한_좌석에_대해서_예약_요청을_하는경우_한_명만_예약_성공한다() {
        // given
        final int threadCount = 3; // 스레드 수를 동적으로 설정
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failedCount = new AtomicInteger(0);

        // when
        IntStream.range(0, threadCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    final ReserveConcertCommand command = new ReserveConcertCommand((long) i + 1, 1L, 1L, List.of(1L, 2L));
                    reserveConcertUseCase.reserveConcert(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // then
        assertAll(
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(2, failedCount.get())
        );

    }

    @Test
    void 동시에_동일한_사용자가_동일한_좌석에_대해서_예약_요청을_하는경우_한_번만_예약_성공한다() {
        // given
        final int threadCount = 10; // 스레드 수를 동적으로 설정
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failedCount = new AtomicInteger(0);

        // when
        IntStream.range(0, threadCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    final ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L, 2L));
                    reserveConcertUseCase.reserveConcert(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // then
        assertAll(
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(9, failedCount.get())
        );
    }

    @Test
    void 동시에_여러_사용자가_동일한_좌석에_대해서_예약_요청을_하는경우_한_명만_예약_성공한다2() {
        // given
        final int threadCount = 3; // 스레드 수를 동적으로 설정
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failedCount = new AtomicInteger(0);

        // 좌석 매핑
        Map<Integer, List<Long>> userSeatMap = new HashMap<>();
        userSeatMap.put(1, List.of(1L, 2L)); // 첫 번째 사용자: 좌석 1, 2
        userSeatMap.put(2, List.of(2L, 3L)); // 두 번째 사용자: 좌석 2, 3
        userSeatMap.put(3, List.of(1L, 3L)); // 세 번째 사용자: 좌석 1, 3

        // when
        IntStream.range(0, threadCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    List<Long> seatIds = userSeatMap.get(i + 1);
                    final ReserveConcertCommand command = new ReserveConcertCommand((long) i + 1, 1L, 1L, seatIds);
                    reserveConcertUseCase.reserveConcert(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // then
        assertAll(
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(2, failedCount.get())
        );
    }
}