package com.hhplus.concertreservation.apps.concert.application.usecase;

import com.hhplus.concertreservation.apps.concert.domain.model.dto.command.ReserveConcertCommand;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Concert;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSeat;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.ConcertSession;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.ConcertSeatStatus;
import com.hhplus.concertreservation.apps.concert.domain.model.enums.OutboxStatus;
import com.hhplus.concertreservation.apps.concert.domain.repository.ConcertWriter;
import com.hhplus.concertreservation.apps.concert.infrastruture.entity.OutboxEntity;
import com.hhplus.concertreservation.apps.concert.infrastruture.repository.OutboxJpaRepository;
import com.hhplus.concertreservation.apps.user.domain.model.entity.User;
import com.hhplus.concertreservation.apps.user.domain.repository.UserWriter;
import com.hhplus.concertreservation.common.time.FakeTimeProvider;
import com.hhplus.concertreservation.common.time.TimeProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("콘서트 좌석 예약 시 이벤트 테스트")
@SpringBootTest
class ReserveConcertEventTest {

    @Autowired
    private ReserveConcertUseCase reserveConcertUseCase;

    @Autowired
    private UserWriter userWriter;

    @Autowired
    private ConcertWriter concertWriter;

    @Autowired
    private OutboxJpaRepository outboxJpaRepository;

    @Test
    void 콘서트_좌석_예약_성공_예약_좌석을_반환한다() throws InterruptedException {

        // given
        User user = User.builder()
                .id(1L)
                .name("사용자1")
                .build();

        Concert concert = Concert.builder()
                .id(1L)
                .name("콘서트1")
                .reservationOpenAt(LocalDateTime.parse("2024-10-01T00:00:00"))
                .reservationCloseAt(LocalDateTime.parse("2024-10-30T00:00:00"))
                .build();

        ConcertSession concertSession = ConcertSession.builder()
                .id(1L)
                .concertId(concert.getId())
                .build();

        ConcertSeat concertSeat1 = ConcertSeat.builder()
                .id(1L)
                .version(0L)
                .concertSessionId(concertSession.getId())
                .status(ConcertSeatStatus.AVAILABLE)
                .price(10000)
                .build();

        userWriter.save(user);
        concertWriter.save(concert);
        concertWriter.save(concertSession);
        concertWriter.saveAll(List.of(concertSeat1));

        // when
        final ReserveConcertCommand command = new ReserveConcertCommand(1L, 1L, 1L, List.of(1L));
        reserveConcertUseCase.reserveConcert(command);

        Thread.sleep(5000);

        // then
        final OutboxEntity outboxEntity = outboxJpaRepository.findAll().get(0);
        assertAll(
                () -> assertThat(outboxEntity.getEventType()).isEqualTo("ConcertReservedEvent"),
                () -> assertThat(outboxEntity.getStatus()).isEqualTo(OutboxStatus.PUBLISHED)
        );


    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public TimeProvider timeProvider() {
            return new FakeTimeProvider(LocalDateTime.parse("2024-10-18T10:00"));
        }
    }
}