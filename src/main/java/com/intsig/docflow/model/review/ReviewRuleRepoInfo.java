package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审核规则库信息（简化版）
 * <p>用于审核任务结果中的规则库信息</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRuleRepoInfo {

    /**
     * 审核规则库ID
     */
    @JsonProperty("repo_id")
    private String repoId;

    /**
     * 审核规则库名称
     */
    @JsonProperty("name")
    private String name;
}
