package com.postgraduate.global.logging.dto;

public record LogRequest(String env, String logId, Integer executeTime, String methodName, String exceptionMessage) {
    public LogRequest(String env, String logId, Integer executeTime, String methodName) {
        this(env, logId, executeTime, methodName, null);
    }

    public LogRequest(String env, String logId, String methodName, String exceptionMessage) {
        this(env, logId, null, methodName, exceptionMessage);
    }

    public LogRequest(String env, String methodName, String exceptionMessage) {
        this(env, null, null, methodName, exceptionMessage);
    }
}
