package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文档行信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentLine {

    /**
     * 行文本
     */
    @JsonProperty("text")
    private String text;

    /**
     * 行文本的外接四边形坐标
     * <p>固定8个元素：[左上角x, 左上角y, 右上角x, 右上角y, 右下角x, 右下角y, 左下角x, 左下角y]</p>
     */
    @JsonProperty("position")
    private List<Integer> position;

    /**
     * 字符位置列表
     * <p>每个字符的外接四边形坐标（8个元素）</p>
     */
    @JsonProperty("charPositions")
    private List<List<Integer>> charPositions;
}
