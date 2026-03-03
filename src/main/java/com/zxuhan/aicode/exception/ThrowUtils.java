package com.zxuhan.aicode.exception;

public class ThrowUtils {

    /**
     * Throw the given exception if the condition is true.
     *
     * @param condition        condition to check
     * @param runtimeException exception to throw
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * Throw a BusinessException if the condition is true.
     *
     * @param condition condition to check
     * @param errorCode error code
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * Throw a BusinessException with a custom message if the condition is true.
     *
     * @param condition condition to check
     * @param errorCode error code
     * @param message   error message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
