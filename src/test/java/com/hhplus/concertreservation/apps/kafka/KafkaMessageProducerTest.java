package com.hhplus.concertreservation.apps.kafka;

import com.hhplus.concertreservation.common.testcontainer.TestContainersTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class KafkaMessageProducerTest extends TestContainersTest {

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    // Logger 설정
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // given
    final String TOPIC = "test-topic";

    private final AtomicInteger receivedMessageCount = new AtomicInteger(0);

    @Test
    void 카프카_메시지가_정상적으로_발행되고_수신되는지_확인한다() {
        int messageCount = 50;

        for (int i = 0; i < messageCount; i++) {
            kafkaMessageProducer.send(TOPIC, "Hello, Kafka! " + i);
        }

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(receivedMessageCount.get()).isEqualTo(messageCount));

    }

    // 발행 확인을 위한 리스너 설정
    @KafkaListener(topics = TOPIC, groupId = "test-group")
    public void consumeTestMessage(String message) {
        logger.info("Received message: {}", message);
        receivedMessageCount.incrementAndGet();
    }
}
