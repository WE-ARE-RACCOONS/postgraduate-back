package com.postgraduate.global.aop.logging;

import com.postgraduate.global.aop.logging.dto.TraceStatus;
import com.postgraduate.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.postgraduate.global.aop.logging.utils.LogUtils.clearLogId;
import static com.postgraduate.global.aop.logging.utils.LogUtils.setLogId;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogAspect {
    @Value("${log.Type}")
    private String env;
    private final LogTrace logTrace;

    @Around("com.postgraduate.global.aop.logging.LogPointCuts.allService()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("serviceLog: {}", joinPoint.getSignature().getName());
        return getObject(joinPoint);
    }

    @Around("com.postgraduate.global.aop.logging.LogPointCuts.allController()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        setLogId();
        log.info("controllerLog: {}", joinPoint.getSignature().getName());
        Object object = getObject(joinPoint);
        clearLogId();
        return object;
    }

    private Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus traceStatus = null;
        try {
            traceStatus = logTrace.start(joinPoint.getSignature().getDeclaringType().getSimpleName() + " : " + joinPoint.getSignature().getName());
            Object result = joinPoint.proceed();
            Integer executionTime = logTrace.end(traceStatus);
            log.info("ExecutionTime : {}", executionTime);
            return result;
        }catch (ApplicationException e) {
            if (traceStatus != null) {
                log.error("ErrorCode {} errorMessage {}",e.getCode(), e.getMessage());
                log.error("{}", e.getStackTrace());
            }
            throw e;
        }catch (Exception e) {
            if (traceStatus != null) {
                log.error("500 ERROR {}", e.getMessage());
                log.error("{}", e.getStackTrace());
            }
            throw e;
        }
    }
}
