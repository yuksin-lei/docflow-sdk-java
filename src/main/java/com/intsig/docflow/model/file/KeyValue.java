package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 键值对
 * <p>用于更新文件处理结果</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyValue {

    /**
     * 字段名
     */
    @JsonProperty("key")
    private String key;

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
     * 快速创建不带位置信息的键值对
     *
     * @param key   字段名
     * @param value 字段值
     * @return 键值对对象
     */
    public static KeyValue of(String key, String value) {
        return KeyValue.builder()
                .key(key)
                .value(value)
                .build();
    }
}
