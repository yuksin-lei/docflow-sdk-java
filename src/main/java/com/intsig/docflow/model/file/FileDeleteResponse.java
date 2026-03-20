package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 文件删除响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDeleteResponse {

    /**
     * 删除的文件数量
     */
    @JsonProperty("deleted_count")
    private Integer deletedCount = 0;
}
