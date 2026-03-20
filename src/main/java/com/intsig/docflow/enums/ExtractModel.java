package com.intsig.docflow.enums;

import lombok.Getter;

/**
 * 提取模型类型枚举
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Getter
public enum ExtractModel {
    MODEL_1("Model 1"),

    MODEL_2("Model 2"),

    MODEL_3("Model 3");

    private final String value;

    ExtractModel(String value) {
        this.value = value;
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
    public static ExtractModel fromValue(String value) {
        for (ExtractModel model : ExtractModel.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        return null;
    }
}
