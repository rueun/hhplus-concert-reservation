package com.hhplus.concertreservation.apps.concert.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concertreservation.apps.concert.domain.model.entity.Outbox;
import com.hhplus.concertreservation.apps.concert.infrastruture.repository.OutboxReader;
import com.hhplus.concertreservation.apps.concert.infrastruture.repository.OutboxWriter;
import com.hhplus.concertreservation.common.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxWriter outboxWriter;
    private final OutboxReader outboxReader;

    private final TimeProvider timeProvider;
    private final ObjectMapper objectMapper;

    /**
     * Outbox 을 생성한다.
     * @param eventType 이벤트 타입
     * @param eventKey 이벤트 키
     * @param payload 페이로드
     * @throws JsonProcessingException JSON 처리 예외
     */
    public void createOutbox(final String eventType, final String eventKey, final Object payload) throws JsonProcessingException {
        final String payloadJson = objectMapper.writeValueAsString(payload);
        final Outbox outbox = Outbox.create(eventType, eventKey, payloadJson);
        outboxWriter.save(outbox);
    }


    /**
     * Outbox 의 상태를 발행으로 변경한다.
     * @param eventKey 이벤트 키
     */
    public void publish(final String eventKey) {
        final Outbox outbox = outboxReader.getByEventKey(eventKey);
        outbox.publish();
        outboxWriter.save(outbox);
    }

    /**
     * 특정 기간이 지났음에도 대기 중인 Outbox 목록을 조회한다. (재발행을 위한 목적)
     * @param timeLimit 제한 시간
     * @return 대기 중인 Outbox 목록
     */
    public List<Outbox> findAllAfterThreshold(final int timeLimit) {
        return outboxReader.findAllAfterThreshold(timeLimit, timeProvider.now());
    }
}
