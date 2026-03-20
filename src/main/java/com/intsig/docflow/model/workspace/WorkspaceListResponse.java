package com.intsig.docflow.model.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作空间列表响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
public class WorkspaceListResponse {

    /**
     * 总数
     */
    @JsonProperty("total")
    private Long total;

    /**
     * 当前页码
     */
    @JsonProperty("page")
    private Long page;

    /**
     * 每页数量
     */
    @JsonProperty("page_size")
    private Long pageSize;

    /**
     * 工作空间列表
     */
    @JsonProperty("workspaces")
    private List<WorkspaceInfo> workspaces;
}
