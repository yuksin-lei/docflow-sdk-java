package com.intsig.docflow.exception;

/**
 * 参数校验异常
 *
 * <p>当输入参数不符合要求时抛出此异常</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ValidationException extends DocflowException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造参数校验异常
     *
     * @param message 错误消息
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * 构造参数校验异常（支持国际化）
     *
     * @param message      错误消息
     * @param i18nKey      国际化消息键
     * @param formatParams 格式化参数
     */
    public ValidationException(String message, String i18nKey, Object... formatParams) {
        super(message, i18nKey, formatParams);
    }
}
