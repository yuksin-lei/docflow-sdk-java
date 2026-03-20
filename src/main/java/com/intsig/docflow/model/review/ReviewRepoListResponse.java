package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核规则库列表响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRepoListResponse {

    /**
     * 规则库列表
     */
    @JsonProperty("repos")
    private List<ReviewRepoInfo> repos = new ArrayList<>();

    /**
     * 总条数
     */
    @JsonProperty("total")
    private Integer total = 0;

    /**
     * 页码
     */
    @JsonProperty("page")
    private Integer page = 1;

    /**
     * 每页条数
     */
    @JsonProperty("page_size")
    private Integer pageSize = 10;
}
