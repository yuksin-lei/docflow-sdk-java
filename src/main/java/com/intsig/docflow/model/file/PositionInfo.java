package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 位置信息
 * <p>字段值的位置。字段值可能跨页或跨行，因此用数组展示。</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionInfo {

    /**
     * 字段所在页（是pages数组的索引）
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 字段值的外接四边形坐标
     * <p>图像以左上为原点，数组值为 [左上角x, 左上角y, 右上角x, 右上角y, 右下角x, 右下角y, 左下角x, 左下角y]</p>
     * <p>固定8个元素</p>
     */
    @JsonProperty("vertices")
    private List<Integer> vertices;
}
