package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 审核结果组
 * <p>按规则组分组的审核结果</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResultGroup {

    /**
     * 审核规则组ID
     */
    @JsonProperty("group_id")
    private String groupId;

    /**
     * 审核规则组名称
     */
    @JsonProperty("group_name")
    private String groupName;

    /**
     * 审核任务详情列表
     */
    @JsonProperty("review_tasks")
    private List<ReviewTaskDetail> reviewTasks;
}
