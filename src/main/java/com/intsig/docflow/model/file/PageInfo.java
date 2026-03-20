package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 页面信息
 * <p>文件每页用于渲染的数据。配合data.fields中的page和position字段，可以准确画出字段在页面上的位置。</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {

    /**
     * 页码（从0开始）
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 旋转角度（0, 90, 180, 270）
     */
    @JsonProperty("angle")
    private Integer angle;

    /**
     * 转正后的页宽
     */
    @JsonProperty("width")
    private Integer width;

    /**
     * 转正后的页高
     */
    @JsonProperty("height")
    private Integer height;

    /**
     * PDF转图片时的分辨率。非PDF文件该值为0
     */
    @JsonProperty("dpi")
    private Integer dpi;
}
