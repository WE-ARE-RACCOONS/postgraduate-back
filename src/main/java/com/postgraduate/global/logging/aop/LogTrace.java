package com.postgraduate.global.logging.aop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class LogTrace {
    private static final String TRACE_ID = "TraceId";

    public TraceStatus start(String method) {
        String id = createTraceId();
        MDC.put(TRACE_ID, id);
        // MDC란 ThreadLocal을 이용해 각 스레드에서만 유지되는 정보입니다.
        long startTime = System.currentTimeMillis();
        log.info("[{}]{} === start" + id, method );
        return new TraceStatus(id, startTime, method);
    }

    public Integer end(TraceStatus traceStatus) { // 걸린 시간 로그 처리 및 오래걸리면 경고
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - traceStatus.getStartTime();
        if (executionTime > 1000) {
            log.warn("[{}]{} === execute time {}ms", traceStatus.getThreadId(),traceStatus.getMethodName(), executionTime);
        } else {
            log.info("[{}]{} === execute time {}ms", traceStatus.getThreadId(),traceStatus.getMethodName(), executionTime);
        }
        removeMdcContext();
        return (int)executionTime;
    }
     /**
     * 일단은 댕충 ClassCastException으로 처리 실제 사용할 경우 알맞게 처리
     */
    public void apiException(ClassCastException e, TraceStatus traceStatus) {
        log.error("[{}]{} === API EXCEPTION [{}] {}", traceStatus.getThreadId(), traceStatus.getMethodName(), 500, e.getMessage());
        removeMdcContext();
    }

    public void exception(Exception e, TraceStatus traceStatus) {
        log.error("[{}]{} === Exception [{}] {}", traceStatus.getThreadId(), traceStatus.getMethodName(), 500, e.getMessage());
        removeMdcContext();
    }

    private String createTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void removeMdcContext() {
        MDC.remove(TRACE_ID);
    }
}
