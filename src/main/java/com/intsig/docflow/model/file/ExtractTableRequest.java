package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 抽取表格请求
 * <p>用于指定需要抽取的表格及其字段</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractTableRequest {

    /**
     * 表格名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 表格字段列表
     * <p>指定需要从表格中抽取的字段</p>
     */
    @JsonProperty("fields")
    private List<ExtractFieldRequest> fields;
}
