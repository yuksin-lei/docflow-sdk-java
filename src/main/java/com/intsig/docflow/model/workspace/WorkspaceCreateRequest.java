package com.intsig.docflow.model.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.intsig.docflow.enums.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建工作空间请求
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceCreateRequest {

    /**
     * 空间名称（必填，最大50字符）
     */
    @JsonProperty("name")
    private String name;

    /**
     * 空间描述（可选，最大200字符）
     */
    @JsonProperty("description")
    private String description;

    /**
     * 企业组织ID（必填）
     */
    @JsonProperty("enterprise_id")
    private Long enterpriseId;

    /**
     * 协作范围（必填）
     * 0: 仅自己可见
     * 1: 企业成员可见
     */
    @JsonProperty("auth_scope")
    private Integer authScope;

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

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getAuthScope() {
        return authScope;
    }

    public void setAuthScope(Integer authScope) {
        this.authScope = authScope;
    }

    /**
     * 设置协作范围（使用枚举）
     *
     * @param authScope 协作范围枚举
     */
    public void setAuthScope(AuthScope authScope) {
        this.authScope = authScope.getValue();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final WorkspaceCreateRequest request = new WorkspaceCreateRequest();

        public Builder name(String name) {
            request.name = name;
            return this;
        }

        public Builder description(String description) {
            request.description = description;
            return this;
        }

        public Builder enterpriseId(Long enterpriseId) {
            request.enterpriseId = enterpriseId;
            return this;
        }

        public Builder authScope(Integer authScope) {
            request.authScope = authScope;
            return this;
        }

        public Builder authScope(AuthScope authScope) {
            request.authScope = authScope.getValue();
            return this;
        }

        public WorkspaceCreateRequest build() {
            return request;
        }
    }
}
