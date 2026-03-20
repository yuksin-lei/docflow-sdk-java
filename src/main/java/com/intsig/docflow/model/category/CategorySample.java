package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 类别样本信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
public class CategorySample {

    /**
     * 样本ID
     */
    @JsonProperty("sample_id")
    private String sampleId;

    /**
     * 样本文件名
     */
    @JsonProperty("file_name")
    private String fileName;

}
