package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 手写体信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandwritingInfo {

    /**
     * 角度
     */
    @JsonProperty("angel")
    private Integer angel;

    /**
     * 页码
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 手写体文本内容
     */
    @JsonProperty("text")
    private String text;

    /**
     * 位置信息列表（手写体可能跨页或跨行）
     */
    @JsonProperty("position")
    private List<PositionInfo> position;
}
