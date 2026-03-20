package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 创建类别响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryCreateResponse {

    /**
     * 类别ID
     */
    @JsonProperty("category_id")
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
