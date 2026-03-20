package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽取字段请求
 * <p>用于指定需要抽取的字段</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractFieldRequest {

    /**
     * 字段名称
     */
    @JsonProperty("key")
    private String key;

    /**
     * 字段提示
     * <p>用于指导字段抽取的提示信息，例如："只保留年的部分"</p>
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 快速创建只包含字段名的请求
     *
     * @param key 字段名称
     * @return 抽取字段请求对象
     */
    public static ExtractFieldRequest of(String key) {
        return ExtractFieldRequest.builder()
                .key(key)
                .build();
    }

    /**
     * 快速创建包含字段名和提示的请求
     *
     * @param key    字段名称
     * @param prompt 字段提示
     * @return 抽取字段请求对象
     */
    public static ExtractFieldRequest of(String key, String prompt) {
        return ExtractFieldRequest.builder()
                .key(key)
                .prompt(prompt)
                .build();
    }
}
