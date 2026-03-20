package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 字段键值对
 * <p>用于表示抽取的字段信息</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldKeyValue {

    /**
     * 字段名
     */
    @JsonProperty("key")
    private String key;

    /**
     * 导出字段名
     */
    @JsonProperty("identifier")
    private String identifier;

    /**
     * 字段值
     */
    @JsonProperty("value")
    private String value;

    /**
     * 字段值的位置列表（字段值可能跨页或跨行）
     */
    @JsonProperty("position")
    private List<PositionInfo> position;

    /**
     * 索引（仅在表格字段中使用）
     */
    @JsonProperty("index")
    private Integer index;
}
