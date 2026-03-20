package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文档页面信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentPage {

    /**
     * 页面的旋转角度
     */
    @JsonProperty("angle")
    private Integer angle;

    /**
     * 页面的宽度
     */
    @JsonProperty("width")
    private Integer width;

    /**
     * 页面的高度
     */
    @JsonProperty("height")
    private Integer height;

    /**
     * 页面的文本行数组
     */
    @JsonProperty("lines")
    private List<DocumentLine> lines;
}
