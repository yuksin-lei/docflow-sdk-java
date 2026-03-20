package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 审核规则字段
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRuleField {

    /**
     * 字段ID
     */
    @JsonProperty("field_id")
    private String fieldId;

    /**
     * 字段名称
     */
    @JsonProperty("field_name")
    private String fieldName;
}
