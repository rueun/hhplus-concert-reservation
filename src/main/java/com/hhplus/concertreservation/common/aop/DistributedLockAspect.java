package com.hhplus.concertreservation.common.aop;

import com.hhplus.concertreservation.common.aop.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.hhplus.concertreservation.common.aop.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + distributedLock.prefix() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        // (1) 락의 이름으로 RLock 인스턴스를 생성
        RLock rLock = redissonClient.getLock(key);

        try {
            // (2) 정의된 waitTime 까지 락 획득 시도, leaseTime 이 지나면 자동으로 락 해제
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) {
                log.info("Lock 획득 실패: {}", key);
                return false;
            }
            log.info("Lock 획득: {}", key);

            // (3) DistributedLock 어노테이션이 선언된 메서드를 별도의 트랜잭션으로 실행한다.
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                // (4) 종료 시 무조건 락 해제
                rLock.unlock();
                log.info("Lock 해제: {}", key);
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}", key, e.getMessage());
            }
        }
    }

}