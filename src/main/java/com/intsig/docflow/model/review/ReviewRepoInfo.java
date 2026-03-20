package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核规则库信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRepoInfo {

    /**
     * 规则库ID
     */
    @JsonProperty("repo_id")
    private String repoId;

    /**
     * 规则库名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 规则组列表
     */
    @JsonProperty("groups")
    private List<ReviewGroup> groups = new ArrayList<>();

    /**
     * 关联的文件类别ID列表
     */
    @JsonProperty("category_ids")
    private List<String> categoryIds;
}
