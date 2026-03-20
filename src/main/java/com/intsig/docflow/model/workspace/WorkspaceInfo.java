package com.intsig.docflow.model.workspace;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 工作空间信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
public class WorkspaceInfo {

    /**
     * 工作空间ID
     */
    @JsonProperty("id")
    @JsonAlias("workspace_id")
    private String id;

    /**
     * 工作空间名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 工作空间描述
     */
    @JsonProperty("description")
    private String description;

    /**
     * 协作范围
     */
    @JsonProperty("auth_scope")
    private Integer authScope;

    /**
     * 回调URL
     */
    @JsonProperty("callback_url")
    private String callbackUrl;

    /**
     * 回调重试次数
     */
    @JsonProperty("callback_retry_time")
    private Integer callbackRetryTime;
}
