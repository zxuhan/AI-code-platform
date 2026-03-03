package com.zxuhan.aicode.common;

import com.zxuhan.aicode.exception.ErrorCode;

/**
 * Helpers for building API responses.
 */
public class ResultUtils {

    /**
     * Build a success response.
     *
     * @param data payload
     * @param <T>  payload type
     * @return response
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * Build an error response.
     *
     * @param errorCode error code
     * @return response
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * Build an error response.
     *
     * @param code    error code
     * @param message error message
     * @return response
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * Build an error response.
     *
     * @param errorCode error code
     * @return response
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}