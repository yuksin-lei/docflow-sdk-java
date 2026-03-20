package com.intsig.docflow.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 类别样本列表响应
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategorySampleListResponse {

    /**
     * 总数
     */
    @JsonProperty("total")
    private Long total;

    /**
     * 当前页码
     */
    @JsonProperty("page")
    private Long page;

    /**
     * 每页数量
     */
    @JsonProperty("page_size")
    private Long pageSize;

    /**
     * 样本列表
     */
    @JsonProperty("samples")
    private List<CategorySample> samples;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<CategorySample> getSamples() {
        return samples;
    }

    public void setSamples(List<CategorySample> samples) {
        this.samples = samples;
    }
}
