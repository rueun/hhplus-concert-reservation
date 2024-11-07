package com.hhplus.concertreservation.apps.queue.infrastruture.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ACTIVE_QUEUE_KEY = "active-queues";
    private static final String TOKEN_META_KEY_PREFIX = "token-meta:";

    @Autowired
    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer, RedisTemplate<String, String> redisTemplate) {
        super(listenerContainer);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        final String expiredKey = message.toString();
        if (expiredKey.startsWith(TOKEN_META_KEY_PREFIX)) {
            final String token = expiredKey.substring(TOKEN_META_KEY_PREFIX.length());
            redisTemplate.opsForSet().remove(ACTIVE_QUEUE_KEY, token);
        }
    }
}
