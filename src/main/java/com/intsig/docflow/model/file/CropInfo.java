package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 切图信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropInfo {

    /**
     * 所属原文件的页索引（0代表源文件的第一页）
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 切分图片的角度信息
     */
    @JsonProperty("imageAngle")
    private String imageAngle;
}
