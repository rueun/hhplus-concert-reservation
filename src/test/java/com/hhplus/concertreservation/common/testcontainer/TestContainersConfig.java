package com.hhplus.concertreservation.common.testcontainer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainersConfig {

    private static final KafkaContainer kafkaContainer;
    private static final GenericContainer<?> redisContainer;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        redisContainer = new GenericContainer<>("redis:latest")
                .withExposedPorts(6379);
    }

    @PostConstruct
    public void startContainers() {
        kafkaContainer.start();
        redisContainer.start();
    }

    @PreDestroy
    public void endContainers() {
        kafkaContainer.stop();
        redisContainer.stop();
    }

    public static KafkaContainer getKafkaContainer() {
        return kafkaContainer;
    }

    public static GenericContainer<?> getRedisContainer() {
        return redisContainer;
    }
}