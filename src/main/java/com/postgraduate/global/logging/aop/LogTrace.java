package com.postgraduate.global.logging.aop;

import com.postgraduate.global.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.postgraduate.global.logging.aop.LogUtils.getLogId;

@Component
@Slf4j
public class LogTrace {
    private static final String TRACE_ID = "TraceId";
    private static final String LOG_FORMAT = "ThreadID : {}, Code : {}, Message : {}";

    public TraceStatus start(String method) {
        String id = getLogId();
        MDC.put(TRACE_ID, id);
        // MDC란 ThreadLocal을 이용해 각 스레드에서만 유지되는 정보입니다.
        long startTime = System.currentTimeMillis();
        return new TraceStatus(id, startTime, method);
    }

    public Integer end(TraceStatus traceStatus) { // 걸린 시간 로그 처리 및 오래걸리면 경고
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - traceStatus.startTime();
        removeMdcContext();
        return (int)executionTime;
    }

    public void exception(ApplicationException e, TraceStatus traceStatus) {
        log.error("class : {}, trace : {}", traceStatus.methodName(), e.getStackTrace());
        log.error(LOG_FORMAT, traceStatus.threadId(), e.getErrorCode(), e.getMessage());
        removeMdcContext();
    }

    public void exception(Exception e, TraceStatus traceStatus) {
        log.error("class : {}, trace : {}", traceStatus.methodName(), e.getStackTrace());
        log.error(LOG_FORMAT, traceStatus.threadId(), traceStatus.methodName(), 500, e.getMessage());
        removeMdcContext();
    }

    private void removeMdcContext() {
        MDC.remove(TRACE_ID);
    }
}
