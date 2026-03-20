package com.intsig.docflow.exception;

/**
 * 认证失败异常
 *
 * <p>当 API 认证失败时抛出此异常（HTTP 401）</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class AuthenticationException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造认证失败异常
     *
     * @param message 错误消息
     */
    public AuthenticationException(String message) {
        super(message, "error.auth.failed");
    }

    /**
     * 构造认证失败异常
     *
     * @param message 错误消息
     * @param cause   原因异常
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, "error.auth.failed", cause);
    }
}
