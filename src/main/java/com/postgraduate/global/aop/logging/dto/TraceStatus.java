package com.postgraduate.global.aop.logging.dto;

public record TraceStatus(String threadId, Long startTime, String methodName) {}
