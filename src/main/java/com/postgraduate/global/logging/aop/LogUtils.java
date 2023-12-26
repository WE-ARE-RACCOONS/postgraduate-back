package com.postgraduate.global.logging.aop;

import java.util.UUID;

public class LogUtils {
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
