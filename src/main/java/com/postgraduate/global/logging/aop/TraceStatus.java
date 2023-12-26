package com.postgraduate.global.logging.aop;

public record TraceStatus(String threadId, Long startTime, String methodName) {}
