package com.intsig.docflow.exception;

/**
 * 资源不存在异常
 *
 * <p>当请求的资源不存在时抛出此异常（HTTP 404）</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ResourceNotFoundException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造资源不存在异常
     *
     * @param message 错误消息
     */
    public ResourceNotFoundException(String message) {
        super(message, "error.resource.not_found");
    }

    /**
     * 构造资源不存在异常
     *
     * @param message 错误消息
     * @param cause   原因异常
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, "error.resource.not_found", cause);
    }
}
