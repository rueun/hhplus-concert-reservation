package com.hhplus.concertreservation.apps.queue.infrastruture.reposotory;

import com.hhplus.concertreservation.apps.queue.domain.exception.WaitingQueueErrorType;
import com.hhplus.concertreservation.apps.queue.domain.model.entity.WaitingQueue;
import com.hhplus.concertreservation.apps.queue.domain.model.enums.QueueStatus;
import com.hhplus.concertreservation.apps.queue.domain.repository.WaitingQueueReader;
import com.hhplus.concertreservation.support.domain.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisWaitingQueueReader implements WaitingQueueReader {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String WAITING_QUEUE_KEY = "waiting-queues";
    private static final String ACTIVE_QUEUE_KEY = "active-queues";
    private static final String TOKEN_META_KEY_PREFIX = "token-meta:";

    @Override
    public WaitingQueue getByToken(final String token) {
        Long rank = redisTemplate.opsForZSet().rank(WAITING_QUEUE_KEY, token);

        if (rank != null) { // 대기열에 있는 경우
            return buildWaitingQueue(token, rank, QueueStatus.WAITING);
        }

        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ACTIVE_QUEUE_KEY, token))) { // 활성열에 있는 경우
            return buildWaitingQueue(token, 0L, QueueStatus.ACTIVATED);
        }

        throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_FOUND);
    }

    private WaitingQueue buildWaitingQueue(final String token, final Long order, final QueueStatus status) {
        final String userId = (String) redisTemplate.opsForHash().get(TOKEN_META_KEY_PREFIX + token, "userId");
        if (userId == null) {
            throw new CoreException(WaitingQueueErrorType.WAITING_QUEUE_NOT_FOUND);
        }

        return WaitingQueue.builder()
                .token(token)
                .userId(Long.parseLong(userId))
                .waitingOrder(order)
                .status(status)
                .build();
    }


    @Override
    public List<WaitingQueue> getWaitingQueuesToBeActivated(final int activationCount) {
        final Set<Object> range = redisTemplate.opsForZSet().range(WAITING_QUEUE_KEY, 0, activationCount - 1);

        if (range == null || range.isEmpty()) {
            return Collections.emptyList();
        }

        return range.stream()
                .map(token -> {
                    final String userId = (String) redisTemplate.opsForHash().get(TOKEN_META_KEY_PREFIX + token, "userId");

                    return WaitingQueue.builder()
                            .token((String) token)
                            .userId(Long.parseLong(userId))
                            .status(QueueStatus.WAITING)
                            .build();
                }).toList();
    }

    @Override
    public WaitingQueue getActiveQueueByToken(final String token) {
        final Boolean isActiveQueue = redisTemplate.opsForSet().isMember(ACTIVE_QUEUE_KEY, token);
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(TOKEN_META_KEY_PREFIX + token);

        final QueueStatus status = Boolean.TRUE.equals(isActiveQueue) && !entries.isEmpty() ? QueueStatus.ACTIVATED : QueueStatus.EXPIRED;

        return WaitingQueue.builder()
                .token(token)
                .status(status)
                .build();
    }
}
