package com.zxuhan.aicodebackend.exception;

public class ThrowUtils {
    /**
     * Throw exception if condition is true
     *
     * @param condition        condition
     * @param runtimeException exception
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * Throw exception if condition is true
     *
     * @param condition condition
     * @param errorCode error code
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * Throw exception if condition is true
     *
     * @param condition condition
     * @param errorCode error code
     * @param message   error message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}