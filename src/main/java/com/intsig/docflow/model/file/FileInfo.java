package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文件信息
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {

    /**
     * 文件ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 文件名
     */
    @JsonProperty("name")
    private String name;

    /**
     * 文件格式
     */
    @JsonProperty("format")
    private String format;

    /**
     * 任务ID
     */
    @JsonProperty("task_id")
    private String taskId;

    /**
     * 任务详情页URL
     */
    @JsonProperty("task_detail_url")
    private String taskDetailUrl;

    /**
     * 任务类型
     * <p>0: 文件拆分子任务, 1: 普通任务, 2: 父任务, 3: 切图子任务</p>
     */
    @JsonProperty("task_type")
    private Integer taskType;

    /**
     * 批次编号
     */
    @JsonProperty("batch_number")
    private String batchNumber;

    /**
     * 文件类别
     */
    @JsonProperty("category")
    private String category;

    /**
     * 识别状态
     * <p>0: 待识别, 1: 识别成功, 2: 识别失败, 3: 分类中, 4: 抽取中, 5: 准备中, 6: 文件拆分中, 7: 切图中, 10: 分类完成, 20: 解析中</p>
     */
    @JsonProperty("recognition_status")
    private Integer recognitionStatus;

    /**
     * 任务失败原因（当recognition_status为2时有此字段）
     */
    @JsonProperty("failure_causes")
    private String failureCauses;

    /**
     * 核对状态
     * <p>0: 待核对, 2: 已确认, 3: 已拒绝, 4: 已删除, 5: 推迟处理</p>
     */
    @JsonProperty("verification_status")
    private Integer verificationStatus;

    /**
     * 页面信息列表
     */
    @JsonProperty("pages")
    private List<PageInfo> pages;

    /**
     * 文件处理结果数据
     */
    @JsonProperty("data")
    private FileData data;

    /**
     * 文档的全部文字识别结果
     */
    @JsonProperty("document")
    private DocumentInfo document;

    /**
     * 子任务信息列表（拆分任务）
     */
    @JsonProperty("child_files")
    private List<SubFileInfo> childFiles;

    /**
     * 任务处理时间（毫秒）
     * <p>计算的是从任务开始至完成(成功或失败)的时间，不包括排队等待时间</p>
     */
    @JsonProperty("duration_ms")
    private Integer durationMs;

    /**
     * 当前文件总页数
     */
    @JsonProperty("total_page_num")
    private Integer totalPageNum;
}
