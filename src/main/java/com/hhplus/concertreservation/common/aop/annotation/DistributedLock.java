package com.hhplus.concertreservation.common.aop.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 락의 키 값(고유 값)
     */
    String key();

    /**
     * 락의 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 락을 기다리는 최대 시간 (default: 5000ms)
     */
    long waitTime() default 5000L;

    /**
     * 락을 획득한 후, 점유하는 최대 시간 (default: 3000ms)
     * 락을 획득한 후, 이 시간이 지나면 자동으로 락이 해제됨
     */
    long leaseTime() default 3000L;
}