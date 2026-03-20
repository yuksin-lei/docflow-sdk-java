package com.intsig.docflow.exception;

/**
 * 权限不足异常
 *
 * <p>当用户没有足够权限访问资源时抛出此异常（HTTP 403）</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class PermissionDeniedException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造权限不足异常
     *
     * @param message 错误消息
     */
    public PermissionDeniedException(String message) {
        super(message, "error.permission.denied");
    }

    /**
     * 构造权限不足异常
     *
     * @param message 错误消息
     * @param cause   原因异常
     */
    public PermissionDeniedException(String message, Throwable cause) {
        super(message, "error.permission.denied", cause);
    }
}
