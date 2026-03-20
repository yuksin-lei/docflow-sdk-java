package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 审核规则
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRule {

    /**
     * 规则ID
     */
    @JsonProperty("rule_id")
    private String ruleId;

    /**
     * 规则名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 规则类型
     */
    @JsonProperty("rule_type")
    private String ruleType;

    /**
     * 规则配置
     */
    @JsonProperty("config")
    private Map<String, Object> config;

    /**
     * 规则提示词
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 关联的文件类别ID列表
     */
    @JsonProperty("category_ids")
    private List<String> categoryIds;

    /**
     * 风险等级（10:高风险 20:中风险 30:低风险）
     */
    @JsonProperty("risk_level")
    private Integer riskLevel;

    /**
     * 引用的字段列表
     */
    @JsonProperty("referenced_fields")
    private List<ReviewRuleReferencedField> referencedFields = new ArrayList<>();
}
