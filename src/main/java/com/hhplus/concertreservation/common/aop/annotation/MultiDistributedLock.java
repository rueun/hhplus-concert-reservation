package com.hhplus.concertreservation.common.aop.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * MultiDistributedLock 어노테이션은 멀티락 기능을 제공하며, 메서드에 적용하여 여러 리소스에 대해 동시성 제어를 수행할 수 있습니다.
 * prefix는 멀티락을 유연하게 적용하기 위한 식별자 역할을 하며, key는 EL 표현식을 통해 동적으로 생성됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiDistributedLock {

    /**
     * 멀티락의 고유한 식별자 접두사
     */
    String prefix();

    /**
     * 락의 키 값(EL 표현식을 통한 동적 생성 가능)
     */
    String key();

    /**
     * 락을 기다리는 최대 시간 (기본값: 5000ms)
     */
    long waitTime() default 5000;

    /**
     * 락을 획득한 후 점유하는 최대 시간 (기본값: 3000ms)
     * 락을 획득한 후, 이 시간이 지나면 자동으로 락이 해제됩니다.
     */
    long leaseTime() default 3000;

    /**
     * 락의 시간 단위 (기본값: 밀리초)
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}