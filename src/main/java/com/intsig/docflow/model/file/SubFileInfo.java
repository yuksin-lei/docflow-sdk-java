package com.intsig.docflow.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 子文件信息
 * <p>拆分后的子任务信息</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubFileInfo {

    /**
     * 文件ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 任务ID
     */
    @JsonProperty("task_id")
    private String taskId;

    /**
     * 文件子任务详情页URL
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
     * 父任务ID（不为0时有意义）
     */
    @JsonProperty("parent_task_id")
    private String parentTaskId;

    /**
     * 批次编号
     */
    @JsonProperty("batch_number")
    private String batchNumber;

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
     * 识别状态
     * <p>0: 待识别, 1: 识别成功, 2: 识别失败, 3: 分类中, 4: 抽取中, 5: 准备中, 6: 文件拆分中, 7: 切图中, 10: 分类完成, 20: 解析中</p>
     */
    @JsonProperty("recognition_status")
    private Integer recognitionStatus;

    /**
     * 核对状态
     * <p>0: 待核对, 2: 已确认, 3: 已拒绝, 4: 已删除, 5: 推迟处理</p>
     */
    @JsonProperty("verification_status")
    private Integer verificationStatus;

    /**
     * 文件类别
     */
    @JsonProperty("category")
    private String category;

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
     * 切图结果在原文件中的坐标位置
     */
    @JsonProperty("from_parent_position_list")
    private List<Integer> fromParentPositionList;

    /**
     * 切图信息
     */
    @JsonProperty("crop_info")
    private CropInfo cropInfo;
}
