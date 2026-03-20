package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 印章信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StampInfo {

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
     * 印章文本内容
     */
    @JsonProperty("text")
    private String text;

    /**
     * 印章类型
     */
    @JsonProperty("type")
    private String type;

    /**
     * 印章颜色
     */
    @JsonProperty("color")
    private String color;

    /**
     * 印章形状
     */
    @JsonProperty("shape")
    private String shape;

    /**
     * 位置信息（外接四边形坐标数组）
     */
    @JsonProperty("position")
    private List<Integer> position;
}
