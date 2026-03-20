package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核规则表格
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRuleTable {

    /**
     * 表格ID
     */
    @JsonProperty("table_id")
    private String tableId;

    /**
     * 表格名称
     */
    @JsonProperty("table_name")
    private String tableName;

    /**
     * 字段列表
     */
    @JsonProperty("fields")
    private List<ReviewRuleField> fields = new ArrayList<>();
}
