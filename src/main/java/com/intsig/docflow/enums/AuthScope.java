package com.intsig.docflow.enums;

/**
 * 权限范围枚举
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public enum AuthScope {
    /**
     * 私有权限（仅自己可见）
     */
    PRIVATE(0),

    /**
     * 公共权限（企业成员可见）
     */
    PUBLIC(1);

    private final int value;

    AuthScope(int value) {
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
    public static AuthScope fromValue(int value) {
        for (AuthScope scope : AuthScope.values()) {
            if (scope.value == value) {
                return scope;
            }
        }
        return null;
    }
}
