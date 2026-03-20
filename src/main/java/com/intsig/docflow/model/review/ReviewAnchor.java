package com.intsig.docflow.model.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 审核依据位置信息
 * <p>用于定位审核依据在原文中的位置</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewAnchor {

    /**
     * 在reasoning中的起始字符位置
     */
    @JsonProperty("start_pos")
    private Integer startPos;

    /**
     * 在reasoning中的结束字符位置
     */
    @JsonProperty("end_pos")
    private Integer endPos;

    /**
     * 原文内容
     */
    @JsonProperty("text")
    private String text;

    /**
     * 原文内容的外接四边形坐标
     * <p>8个整数表示四个顶点的坐标：[x1, y1, x2, y2, x3, y3, x4, y4]</p>
     */
    @JsonProperty("vertices")
    private List<Integer> vertices;

    /**
     * 文件ID
     */
    @JsonProperty("file_id")
    private String fileId;

    /**
     * 抽取任务ID
     */
    @JsonProperty("extract_task_id")
    private String extractTaskId;

    /**
     * 所在文件中的页码
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 位置回溯来源
     * <ul>
     *   <li>ocr: 识别</li>
     *   <li>extract_field: 关联字段</li>
     * </ul>
     */
    @JsonProperty("source")
    private String source;

    /**
     * 文件类别
     */
    @JsonProperty("file_category")
    private String fileCategory;

    /**
     * 关联字段名称
     */
    @JsonProperty("field_name")
    private String fieldName;
}
