package com.intsig.docflow.exception;

/**
 * DocFlow SDK 基础异常类
 *
 * <p>所有 SDK 异常的基类，支持国际化消息</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class DocflowException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 国际化消息键
     */
    private final String i18nKey;

    /**
     * 格式化参数
     */
    private final Object[] formatParams;

    /**
     * 构造异常
     *
     * @param message 错误消息
     */
    public DocflowException(String message) {
        this(message, null, null);
    }

    /**
     * 构造异常
     *
     * @param message 错误消息
     * @param cause   原因异常
     */
    public DocflowException(String message, Throwable cause) {
        this(message, null, cause);
    }

    /**
     * 构造异常（支持国际化）
     *
     * @param message      错误消息（用作后备）
     * @param i18nKey      国际化消息键
     * @param formatParams 格式化参数
     */
    public DocflowException(String message, String i18nKey, Object... formatParams) {
        super(message);
        this.i18nKey = i18nKey;
        this.formatParams = formatParams;
    }

    /**
     * 构造异常（支持国际化和原因异常）
     *
     * @param message      错误消息（用作后备）
     * @param i18nKey      国际化消息键
     * @param cause        原因异常
     * @param formatParams 格式化参数
     */
    public DocflowException(String message, String i18nKey, Throwable cause, Object... formatParams) {
        super(message, cause);
        this.i18nKey = i18nKey;
        this.formatParams = formatParams;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public Object[] getFormatParams() {
        return formatParams;
    }
}
