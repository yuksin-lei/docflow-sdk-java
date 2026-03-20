package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建审核规则库响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRepoCreateResponse {

    /**
     * 规则库ID
     */
    @JsonProperty("repo_id")
    private String repoId;

}
