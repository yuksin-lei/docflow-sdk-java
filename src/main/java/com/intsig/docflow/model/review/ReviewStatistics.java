package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审核统计信息
 * <p>审核任务的统计数据</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewStatistics {

    /**
     * 通过数
     */
    @JsonProperty("pass_count")
    private Integer passCount;

    /**
     * 不通过数
     */
    @JsonProperty("failure_count")
    private Integer failureCount;

    /**
     * 任务执行异常数
     */
    @JsonProperty("error_count")
    private Integer errorCount;
}
