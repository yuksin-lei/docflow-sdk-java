package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核规则引用字段
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRuleReferencedField {

    /**
     * 分类ID
     */
    @JsonProperty("category_id")
    private String categoryId;

    /**
     * 分类名称
     */
    @JsonProperty("category_name")
    private String categoryName;

    /**
     * 字段列表
     */
    @JsonProperty("fields")
    private List<ReviewRuleField> fields = new ArrayList<>();

    /**
     * 表格列表
     */
    @JsonProperty("tables")
    private List<ReviewRuleTable> tables = new ArrayList<>();
}
