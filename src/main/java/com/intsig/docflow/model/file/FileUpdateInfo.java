package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 文件更新信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUpdateInfo {

    /**
     * 空间ID
     */
    @JsonProperty("workspace_id")
    private String workspaceId;

    /**
     * 文件ID
     */
    @JsonProperty("id")
    private String id;
}
