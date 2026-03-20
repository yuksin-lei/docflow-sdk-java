package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档解析参数
 * <p>用于重新处理文件时指定解析参数</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentParserParams {

    /**
     * 公式识别等级
     * <ul>
     *   <li>0: 行间公式和行内公式都识别</li>
     *   <li>1: 仅识别行间公式，行内公式不识别</li>
     *   <li>2: 不识别公式</li>
     * </ul>
     */
    @JsonProperty("formula_level")
    private Integer formulaLevel;

    /**
     * 跨页段落表格合并
     * <ul>
     *   <li>0: 不合并</li>
     *   <li>1: 合并</li>
     * </ul>
     */
    @JsonProperty("apply_merge")
    private Integer applyMerge;

    /**
     * 切分被表格线穿过的文本块
     * <ul>
     *   <li>0: 不切分</li>
     *   <li>1: 切分</li>
     * </ul>
     */
    @JsonProperty("table_text_split_mode")
    private Integer tableTextSplitMode;

    /**
     * 是否进行切边矫正
     * <ul>
     *   <li>0: 不进行切边矫正</li>
     *   <li>1: 进行切边矫正</li>
     * </ul>
     */
    @JsonProperty("crop_dewarp")
    private Integer cropDewarp;

    /**
     * 是否去水印
     * <ul>
     *   <li>0: 不去水印</li>
     *   <li>1: 去水印</li>
     * </ul>
     */
    @JsonProperty("remove_watermark")
    private Integer removeWatermark;
}
