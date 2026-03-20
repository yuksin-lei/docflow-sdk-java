package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 表格信息
 * <p>全部表格数据列表（含系统表格和手动配置表格）</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableInfo {

    /**
     * 表格名称
     */
    @JsonProperty("tableName")
    private String tableName;

    /**
     * 表格类型
     * <p>0: 系统配置，1: 手动添加</p>
     */
    @JsonProperty("tableType")
    private String tableType;

    /**
     * 表格行数据
     * <p>每一行是一个字段列表</p>
     */
    @JsonProperty("items")
    private List<List<FieldKeyValue>> items;
}
