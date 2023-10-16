package com.postgraduate.global.logging.aop;

import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 기본적인 로그 설정으로 수정할 수 있음
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final LogTrace logTrace;
    private final LogService logService;

    @Around("com.postgraduate.global.logging.aop.PointCuts.allService()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("serviceLog: {}", joinPoint.getSignature().getName());
        return getObject(joinPoint);
    }

    @Around("com.postgraduate.global.logging.aop.PointCuts.allController()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("controllerLog: {}", joinPoint.getSignature().getName());
        return getObject(joinPoint);
    }

    private Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus traceStatus = null;
        try {
            traceStatus = logTrace.start(joinPoint.getSignature().getDeclaringType() + " : " + joinPoint.getSignature().getName());
            Object result = joinPoint.proceed();
            Integer executionTime = logTrace.end(traceStatus);
            logService.save(new LogRequest(traceStatus.getThreadId(), executionTime, traceStatus.getMethodName(), null));
            return result;
        } catch (ClassCastException e) {
            if (traceStatus != null) {
                logTrace.apiException(e, traceStatus);
                logService.save(new LogRequest(traceStatus.getThreadId(), 0, traceStatus.getMethodName(), e.getMessage()));
            }
            throw e;
        }catch (Exception e) {
            if (traceStatus != null) {
                logTrace.exception(e, traceStatus);
                logService.save(new LogRequest(traceStatus.getThreadId(), 0, traceStatus.getMethodName(), e.getMessage()));
            }
            throw e;
        }
    }
}
