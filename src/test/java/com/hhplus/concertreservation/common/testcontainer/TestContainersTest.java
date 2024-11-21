package com.hhplus.concertreservation.common.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class TestContainersTest {

    @DynamicPropertySource
    public static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> TestContainersConfig.getKafkaContainer().getBootstrapServers());
        registry.add("spring.data.redis.accumulator.database", () -> 0);
        registry.add("spring.data.redis.accumulator.master.host", () -> TestContainersConfig.getRedisContainer().getHost());
        registry.add("spring.data.redis.accumulator.master.port", () -> TestContainersConfig.getRedisContainer().getFirstMappedPort());
        registry.add("spring.data.redis.accumulator.replicas[0].host", () -> TestContainersConfig.getRedisContainer().getHost());
        registry.add("spring.data.redis.accumulator.replicas[0].port", () -> TestContainersConfig.getRedisContainer().getFirstMappedPort());
        registry.add("spring.data.redis.default.database", () -> 1);
        registry.add("spring.data.redis.default.master.host", () -> TestContainersConfig.getRedisContainer().getHost());
        registry.add("spring.data.redis.default.master.port", () -> TestContainersConfig.getRedisContainer().getFirstMappedPort());
        registry.add("spring.data.redis.default.replicas[0].host", () -> TestContainersConfig.getRedisContainer().getHost());
        registry.add("spring.data.redis.default.replicas[0].port", () -> TestContainersConfig.getRedisContainer().getFirstMappedPort());
    }
}