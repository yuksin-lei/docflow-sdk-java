package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 字段转换配置
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformSettings {

    /**
     * 转换类型
     * <p>
     * 可选值：
     * - datetime: 日期时间转换
     * - enumerate: 枚举转换
     * - regex: 正则表达式转换
     * </p>
     */
    @JsonProperty("type")
    private String type;

    /**
     * 时间类型转换配置
     */
    @JsonProperty("datetime_settings")
    private DatetimeSettings datetimeSettings;

    /**
     * 枚举类型转换配置
     */
    @JsonProperty("enumerate_settings")
    private EnumerateSettings enumerateSettings;

    /**
     * 正则类型转换配置
     */
    @JsonProperty("regex_settings")
    private RegexSettings regexSettings;

    /**
     * 字段值不匹配时的处理动作
     */
    @JsonProperty("mismatch_action")
    private MismatchAction mismatchAction;

    /**
     * 时间类型转换配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatetimeSettings {
        /**
         * 日期时间格式
         * <p>
         * 参考 DocFlow 日期格式说明
         * https://docflow.textin.com/markdown?key=dateFormatGuide
         * </p>
         *
         * @example "yyyy-MM-dd HH:mm:ss"
         */
        @JsonProperty("format")
        private String format;
    }

    /**
     * 枚举类型转换配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnumerateSettings {
        /**
         * 可选枚举值列表
         */
        @JsonProperty("items")
        private List<String> items;
    }

    /**
     * 正则类型转换配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegexSettings {
        /**
         * 匹配用的正则表达式
         *
         * @example "^\\d{4}-\\d{2}-\\d{2}$"
         */
        @JsonProperty("match")
        private String match;

        /**
         * 替换用的正则表达式
         *
         * @example "$1-$2-$3"
         */
        @JsonProperty("replace")
        private String replace;
    }

    /**
     * 字段值不匹配时的处理动作
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MismatchAction {
        /**
         * 不匹配时的处理模式
         * <p>
         * 可选值：
         * - default: 使用默认值
         * - warning: 给出警告
         * </p>
         */
        @JsonProperty("mode")
        private String mode;

        /**
         * 当模式为 default 时使用的默认值
         */
        @JsonProperty("default_value")
        private String defaultValue;
    }
}
