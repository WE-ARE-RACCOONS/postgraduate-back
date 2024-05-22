package com.postgraduate.global.aop.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributeLockAspect {
    private final RedissonClient redissonClient;
    private static final String PREFIX = "mentoring";

    @Around("com.postgraduate.global.aop.lock.DistributeLockPointCut.allMentoringService()")
    public Object startDistributeLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String lockKey = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long) {
                lockKey = PREFIX + arg;
            }
        }
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean available = lock.tryLock(5, 5, SECONDS);
            if (!available)
                throw new LockException();
            log.info("lock 획득 {}", lockKey);
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("예외 발생 {}", e.getMessage());
            throw e;
        } finally {
            lock.unlock();
            log.info("lock 반납 {}", lockKey);
        }
    }
}
