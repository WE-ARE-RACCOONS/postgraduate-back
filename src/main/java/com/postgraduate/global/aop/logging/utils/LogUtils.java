package com.postgraduate.global.aop.logging.utils;

import java.util.UUID;

public class LogUtils {
    private LogUtils() {
        throw new IllegalArgumentException();
    }

    private static final ThreadLocal<String> logIdThreadLocal = new ThreadLocal<>();

    public static void setLogId() {
        logIdThreadLocal.set(UUID.randomUUID().toString());
    }

    public static String getLogId() {
        return logIdThreadLocal.get();
    }

    public static void clearLogId() {
        logIdThreadLocal.remove();
    }
}
