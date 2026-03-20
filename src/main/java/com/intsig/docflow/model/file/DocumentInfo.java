package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文档信息
 * <p>文档的全部文字识别结果</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentInfo {

    /**
     * 文档页面列表
     */
    @JsonProperty("pages")
    private List<DocumentPage> pages;
}
