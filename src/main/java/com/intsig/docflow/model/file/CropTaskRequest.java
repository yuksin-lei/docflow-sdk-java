package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 多图切分任务请求
 * <p>用于修改多图切分后的子任务类别</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropTaskRequest {

    /**
     * 多图切分子任务ID
     */
    @JsonProperty("crop_child_task_id")
    private String cropChildTaskId;

    /**
     * 子任务文件类别
     */
    @JsonProperty("category")
    private String category;
}
