package com.intsig.docflow.model.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 创建工作空间响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class WorkspaceCreateResponse {

    /**
     * 工作空间ID
     */
    @JsonProperty("workspace_id")
    private String workspaceId;

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
}
