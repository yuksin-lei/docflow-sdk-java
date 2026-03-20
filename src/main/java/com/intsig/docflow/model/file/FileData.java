package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文件处理结果数据
 * <p>包含字段、表格、印章、手写体等抽取结果</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileData {

    /**
     * 字段列表
     */
    @JsonProperty("fields")
    private List<FieldKeyValue> fields;

    /**
     * 表格数据列表（简化格式，每行是字段列表）
     * <p>注意：这是一个二维数组，外层数组表示行，内层数组表示每行的字段</p>
     */
    @JsonProperty("items")
    private List<List<FieldKeyValue>> items;

    /**
     * 全部表格数据列表（含系统表格和手动配置表格）
     */
    @JsonProperty("tables")
    private List<TableInfo> tables;

    /**
     * 印章信息列表
     */
    @JsonProperty("stamps")
    private List<StampInfo> stamps;

    /**
     * 手写体信息列表
     */
    @JsonProperty("handwritings")
    private List<HandwritingInfo> handwritings;

    /**
     * 发票验真结果
     */
    @JsonProperty("invoiceVerifyResult")
    private InvoiceVerifyResult invoiceVerifyResult;
}
