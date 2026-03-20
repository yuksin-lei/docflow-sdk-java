package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类别字段配置（用于创建和更新字段）
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFieldConfig {

    /**
     * 字段名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 字段描述
     */
    @JsonProperty("description")
    private String description;

    /**
     * 语义抽取提示词
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 是否使用语义提示词
     */
    @JsonProperty("use_prompt")
    private Boolean usePrompt;

    /**
     * 字段别名列表
     */
    @JsonProperty("alias")
    private List<String> alias;

    /**
     * 导出字段名
     */
    @JsonProperty("identity")
    private String identity;

    /**
     * 是否多值抽取
     */
    @JsonProperty("multi_value")
    private Boolean multiValue;

    /**
     * 是否重复值去重
     * <p>仅当 multi_value 为 true 时有效</p>
     */
    @JsonProperty("duplicate_value_distinct")
    private Boolean duplicateValueDistinct;

    /**
     * 转换配置
     */
    @JsonProperty("transform_settings")
    private TransformSettings transformSettings;
}
