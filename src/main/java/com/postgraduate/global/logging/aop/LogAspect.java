package com.postgraduate.global.logging.aop;

import com.postgraduate.global.exception.ApplicationException;
import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.postgraduate.global.logging.aop.LogUtils.clearLogId;
import static com.postgraduate.global.logging.aop.LogUtils.setLogId;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogAspect {
    @Value("${log.Type}")
    private String env;
    private final LogTrace logTrace;
    private final LogService logService;

    @Around("com.postgraduate.global.logging.aop.PointCuts.allService()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("serviceLog: {}", joinPoint.getSignature().getName());
        return getObject(joinPoint);
    }

    @Around("com.postgraduate.global.logging.aop.PointCuts.allController()")
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
            logService.save(new LogRequest(env, traceStatus.threadId(), executionTime, traceStatus.methodName()));
            return result;
        }catch (ApplicationException e) {
            if (traceStatus != null) {
                log.error("ErrorCode {} errorMessage {}",e.getErrorCode(), e.getMessage());
                log.error("{}", e.getStackTrace());
                logService.save(new LogRequest(env, traceStatus.threadId(), traceStatus.methodName(), e.getMessage()));
            }
            throw e;
        }catch (Exception e) {
            if (traceStatus != null) {
                log.error("500 ERROR {}", e.getMessage());
                log.error("{}", e.getStackTrace());
                logService.save(new LogRequest(env, traceStatus.threadId(), traceStatus.methodName(), e.getMessage()));
            }
            throw e;
        }
    }
}
