package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 类别表格列表响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryTablesListResponse {

    /**
     * 表格列表
     */
    @JsonProperty("tables")
    private List<CategoryTableInfo> tables;

    public List<CategoryTableInfo> getTables() {
        return tables;
    }

    public void setTables(List<CategoryTableInfo> tables) {
        this.tables = tables;
    }
}
