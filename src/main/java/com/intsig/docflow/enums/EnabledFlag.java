package com.intsig.docflow.enums;

/**
 * 启用标志枚举（用于更新）
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public enum EnabledFlag {
    /**
     * 未启用
     */
    DISABLED(0),

    /**
     * 已启用
     */
    ENABLED(1);

    private final int value;

    EnabledFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * 根据整数值获取枚举
     *
     * @param value 整数值
     * @return 对应的枚举，如果不存在则返回 null
     */
    public static EnabledFlag fromValue(int value) {
        for (EnabledFlag flag : EnabledFlag.values()) {
            if (flag.value == value) {
                return flag;
            }
        }
        return null;
    }
}
