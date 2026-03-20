package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 审核任务详情
 * <p>单个审核规则的执行详情</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewTaskDetail {

    /**
     * 审核子任务ID
     */
    @JsonProperty("rule_task_id")
    private String ruleTaskId;

    /**
     * 审核规则ID
     */
    @JsonProperty("rule_id")
    private String ruleId;

    /**
     * 审核规则名称
     */
    @JsonProperty("rule_name")
    private String ruleName;

    /**
     * 风险等级
     */
    @JsonProperty("risk_level")
    private Integer riskLevel;

    /**
     * 审核规则提示词
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 审核结果状态
     */
    @JsonProperty("review_result")
    private Integer reviewResult;

    /**
     * 审核依据
     */
    @JsonProperty("reasoning")
    private String reasoning;

    /**
     * 审核依据的位置回溯信息列表
     */
    @JsonProperty("anchors")
    private List<ReviewAnchor> anchors;

    /**
     * 人工复核结果
     * <ul>
     *   <li>0: 人工复核不通过</li>
     *   <li>1: 人工复核通过</li>
     * </ul>
     */
    @JsonProperty("audit_result")
    private Integer auditResult;

    /**
     * 人工复核批注
     */
    @JsonProperty("audit_message")
    private String auditMessage;
}
