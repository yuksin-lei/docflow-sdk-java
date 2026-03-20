package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核规则组
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewGroup {

    /**
     * 规则组ID
     */
    @JsonProperty("group_id")
    private String groupId;

    /**
     * 规则组名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 规则列表
     */
    @JsonProperty("rules")
    private List<ReviewRule> rules = new ArrayList<>();
}
