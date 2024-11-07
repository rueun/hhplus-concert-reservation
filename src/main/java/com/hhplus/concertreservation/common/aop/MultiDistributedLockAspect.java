package com.hhplus.concertreservation.common.aop;

import com.hhplus.concertreservation.common.aop.annotation.MultiDistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MultiDistributedLockAspect {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.hhplus.concertreservation.common.aop.annotation.MultiDistributedLock)")
    public Object applyMultiLock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MultiDistributedLock multiDistributedLock = method.getAnnotation(MultiDistributedLock.class);

        List<String> keys = CustomSpringELParser.getDynamicValueForList(signature.getParameterNames(), joinPoint.getArgs(), multiDistributedLock.key())
                .stream().map( key -> REDISSON_LOCK_PREFIX + multiDistributedLock.prefix() + key.toString())
                .toList();

        // 각 키마다 RLock 생성
        List<RLock> locks = keys.stream()
                .map(redissonClient::getLock)
                .toList();

        // MultiLock 생성
        RLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));

        try {
            // 락 획득 시도
            boolean available = multiLock.tryLock(multiDistributedLock.waitTime(), multiDistributedLock.leaseTime(), TimeUnit.MILLISECONDS);
            if (!available) {
                log.info("MultiLock 획득 실패: {}", keys);
                return false;
            }
            log.info("MultiLock 획득: {}", keys);
            // 트랜잭션 내에서 로직 실행
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                // 종료 시 무조건 락 해제
                multiLock.unlock();
                log.info("MultiLock 해제: {}", keys);
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson MultiLock Already Unlocked {} {}", keys, e.getMessage());
            }
        }
    }
}