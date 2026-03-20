package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 类别表格信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryTableInfo {

    /**
     * 表格ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 表格名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 表格描述
     */
    @JsonProperty("description")
    private String description;

    /**
     * 表格语义抽取提示词
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 多表合并
     */
    @JsonProperty("collect_from_multi_table")
    private Boolean collectFromMultiTable;

    /**
     * 表格字段列表
     */
    @JsonProperty("fields")
    private List<CategoryField> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Boolean getCollectFromMultiTable() {
        return collectFromMultiTable;
    }

    public void setCollectFromMultiTable(Boolean collectFromMultiTable) {
        this.collectFromMultiTable = collectFromMultiTable;
    }

    public List<CategoryField> getFields() {
        return fields;
    }

    public void setFields(List<CategoryField> fields) {
        this.fields = fields;
    }
}
