package com.intsig.docflow.enums;

/**
 * 风险等级枚举
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public enum RiskLevel {
    /**
     * 高风险
     */
    HIGH(10),

    /**
     * 中风险
     */
    MEDIUM(20),

    /**
     * 低风险
     */
    LOW(30);

    private final int value;

    RiskLevel(int value) {
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
     * 根据值获取枚举
     *
     * @param value 值
     * @return 对应的枚举，如果不存在则返回 null
     */
    public static RiskLevel fromValue(int value) {
        for (RiskLevel level : RiskLevel.values()) {
            if (level.value == value) {
                return level;
            }
        }
        return null;
    }
}
