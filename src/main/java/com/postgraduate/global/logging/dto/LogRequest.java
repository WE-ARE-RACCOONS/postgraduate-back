package com.postgraduate.global.logging.dto;

public record LogRequest(String logId, Integer executeTime, String methodName, String exceptionMessage) {
    public LogRequest(String logId, Integer executeTime, String methodName) {
        this(logId, executeTime, methodName, null);
    }

    public LogRequest(String logId, String methodName, String exceptionMessage) {
        this(logId, null, methodName, exceptionMessage);
    }

    public LogRequest(String methodName, String exceptionMessage) {
        this(null, null, methodName, exceptionMessage);
    }
}
