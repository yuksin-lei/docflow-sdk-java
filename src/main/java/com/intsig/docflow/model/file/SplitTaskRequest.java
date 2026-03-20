package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文档拆分任务请求
 * <p>用于修改文档拆分后的子任务类别和页码</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplitTaskRequest {

    /**
     * 子任务文件类别
     */
    @JsonProperty("category")
    private String category;

    /**
     * 子文件页码列表
     * <p>页码从0开始计数</p>
     */
    @JsonProperty("pages")
    private List<Integer> pages;
}
