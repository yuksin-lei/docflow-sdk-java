package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件查询响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFetchResponse {

    /**
     * 文件列表
     */
    @JsonProperty("files")
    private List<FileInfo> files = new ArrayList<>();

    /**
     * 文件总数
     */
    @JsonProperty("total")
    private Integer total = 0;

    /**
     * 当前页码
     */
    @JsonProperty("page")
    private Integer page = 1;

    /**
     * 每页数量
     */
    @JsonProperty("page_size")
    private Integer pageSize = 1000;
}
