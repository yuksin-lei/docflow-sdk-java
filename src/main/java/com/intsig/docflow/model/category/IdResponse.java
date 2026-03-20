package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ID响应（通用）
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class IdResponse {

    /**
     * 字段ID
     */
    @JsonProperty("field_id")
    private String fieldId;

    /**
     * 表格ID
     */
    @JsonProperty("table_id")
    private String tableId;

    /**
     * 样本ID
     */
    @JsonProperty("sample_id")
    private String sampleId;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
}
