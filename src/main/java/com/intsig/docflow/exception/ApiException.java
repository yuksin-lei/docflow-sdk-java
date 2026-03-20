package com.intsig.docflow.exception;

/**
 * API 调用异常
 *
 * <p>当 API 调用失败时抛出此异常</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ApiException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * HTTP 状态码
     */
    private final int statusCode;

    /**
     * 业务错误码
     */
    private final String code;

    /**
     * 追踪ID
     */
    private final String traceId;

    /**
     * 构造 API 异常
     *
     * @param statusCode HTTP 状态码
     * @param message    错误消息
     */
    public ApiException(int statusCode, String message) {
        this(statusCode, message, null, null);
    }

    /**
     * 构造 API 异常
     *
     * @param statusCode HTTP 状态码
     * @param message    错误消息
     * @param code       业务错误码
     * @param traceId    追踪ID
     */
    public ApiException(int statusCode, String message, String code, String traceId) {
        super(buildErrorMessage(statusCode, message, code, traceId), getI18nKeyForStatus(statusCode));
        this.statusCode = statusCode;
        this.code = code;
        this.traceId = traceId;
    }

    /**
     * 构造 API 异常
     *
     * @param statusCode HTTP 状态码
     * @param message    错误消息
     * @param cause      原因异常
     */
    public ApiException(int statusCode, String message, Throwable cause) {
        this(statusCode, message, null, null, cause);
    }

    /**
     * 构造 API 异常
     *
     * @param statusCode HTTP 状态码
     * @param message    错误消息
     * @param code       业务错误码
     * @param traceId    追踪ID
     * @param cause      原因异常
     */
    public ApiException(int statusCode, String message, String code, String traceId, Throwable cause) {
        super(buildErrorMessage(statusCode, message, code, traceId), getI18nKeyForStatus(statusCode), cause);
        this.statusCode = statusCode;
        this.code = code;
        this.traceId = traceId;
    }

    /**
     * 构建错误消息
     */
    private static String buildErrorMessage(int statusCode, String message, String code, String traceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(statusCode).append("] ").append(message);
        if (code != null) {
            sb.append(" (code: ").append(code).append(")");
        }
        if (traceId != null) {
            sb.append(" (traceId: ").append(traceId).append(")");
        }
        return sb.toString();
    }

    /**
     * 根据状态码获取国际化键
     */
    private static String getI18nKeyForStatus(int statusCode) {
        switch (statusCode) {
            case 400:
            case 401:
            case 403:
            case 404:
            case 423:
            case 429:
            case 500:
            case 502:
            case 503:
            case 504:
            case 900:
                return "error.http." + statusCode;
            default:
                return null;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCode() {
        return code;
    }

    public String getTraceId() {
        return traceId;
    }
}
