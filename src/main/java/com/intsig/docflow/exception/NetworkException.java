package com.intsig.docflow.exception;

/**
 * 网络异常
 *
 * <p>当网络请求失败时抛出此异常，如超时、连接失败等</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class NetworkException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造网络异常
     *
     * @param message 错误消息
     */
    public NetworkException(String message) {
        super(message, "error.network");
    }

    /**
     * 构造网络异常
     *
     * @param message 错误消息
     * @param cause   原因异常
     */
    public NetworkException(String message, Throwable cause) {
        super(message, "error.network", cause);
    }
}
