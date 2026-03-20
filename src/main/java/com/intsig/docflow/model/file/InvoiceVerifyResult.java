package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 发票验真结果
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceVerifyResult {

    /**
     * 发票验真是否成功
     * <p>0: 否, 1: 是, null: 未验真</p>
     */
    @JsonProperty("invoiceVerifyStatus")
    private Integer invoiceVerifyStatus;

    /**
     * 发票验真错误码
     */
    @JsonProperty("invoiceVerifyErrorCode")
    private Integer invoiceVerifyErrorCode;

    /**
     * 发票验真是否可以重试/发起
     * <p>0: 否, 1: 是</p>
     */
    @JsonProperty("invoiceVerifyCanRetry")
    private Integer invoiceVerifyCanRetry;

    /**
     * 发票验真失败原因
     */
    @JsonProperty("invoiceVerifyFailMsg")
    private String invoiceVerifyFailMsg;
}
