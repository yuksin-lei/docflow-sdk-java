package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUploadResponse {

    /**
     * 批次编号
     */
    @JsonProperty("batch_number")
    private String batchNumber;

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }


    /**
     * 文件列表
     */
    @JsonProperty("files")
    private List<FileInfo> files = new ArrayList<>();
}
