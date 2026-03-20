package com.intsig.docflow.enums;

/**
 * 启用状态枚举（用于查询）
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public enum EnabledStatus {
    /**
     * 全部
     */
    ALL("all"),

    /**
     * 未启用
     */
    DISABLED("0"),

    /**
     * 已启用
     */
    ENABLED("1"),

    /**
     * 草稿
     */
    DRAFT("2");

    private final String value;

    EnabledStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * 根据字符串值获取枚举
     *
     * @param value 字符串值
     * @return 对应的枚举，如果不存在则返回 null
     */
    public static EnabledStatus fromValue(String value) {
        for (EnabledStatus status : EnabledStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
