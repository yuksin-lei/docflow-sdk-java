package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 审核任务结果响应
 * <p>获取审核任务结果的完整响应</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewTaskResultResponse {

    /**
     * 审核任务ID
     */
    @JsonProperty("task_id")
    private String taskId;

    /**
     * 任务名称
     */
    @JsonProperty("task_name")
    private String taskName;

    /**
     * 审核任务详情URL
     */
    @JsonProperty("task_detail_url")
    private String taskDetailUrl;

    /**
     * 任务状态
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 规则库信息
     */
    @JsonProperty("rule_repo")
    private ReviewRuleRepoInfo ruleRepo;

    /**
     * 抽取任务ID列表
     */
    @JsonProperty("extract_task_ids")
    private List<String> extractTaskIds;

    /**
     * 统计信息
     */
    @JsonProperty("statistics")
    private ReviewStatistics statistics;

    /**
     * 审核结果组列表
     */
    @JsonProperty("groups")
    private List<ReviewResultGroup> groups;
}
