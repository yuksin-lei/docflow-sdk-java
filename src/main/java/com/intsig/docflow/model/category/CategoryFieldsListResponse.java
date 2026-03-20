package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 类别字段列表响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryFieldsListResponse {

    /**
     * 普通字段列表
     */
    @JsonProperty("fields")
    private List<CategoryField> fields;

    /**
     * 表格列表
     */
    @JsonProperty("tables")
    private List<CategoryTableInfo> tables;

    public List<CategoryField> getFields() {
        return fields;
    }

    public void setFields(List<CategoryField> fields) {
        this.fields = fields;
    }

    public List<CategoryTableInfo> getTables() {
        return tables;
    }

    public void setTables(List<CategoryTableInfo> tables) {
        this.tables = tables;
    }
}
