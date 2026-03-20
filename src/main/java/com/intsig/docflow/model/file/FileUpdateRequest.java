package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件更新请求
 * <p>用于批量更新文件处理结果</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUpdateRequest {

    /**
     * 空间ID
     */
    @JsonProperty("workspace_id")
    private String workspaceId;

    /**
     * 文件ID
     */
    @JsonProperty("file_id")
    private String fileId;

    /**
     * 更新数据
     */
    @JsonProperty("data")
    private FileUpdateData data;
}
