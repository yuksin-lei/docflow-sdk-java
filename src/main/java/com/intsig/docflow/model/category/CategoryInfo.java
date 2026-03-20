package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 类别信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryInfo {

    /**
     * 类别ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 类别名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 类别描述
     */
    @JsonProperty("description")
    private String description;

    /**
     * 启用状态：0-未启用，1-已启用
     */
    @JsonProperty("enabled")
    private Integer enabled;

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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
