package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.iterator.PageIterator;
import com.intsig.docflow.model.file.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件资源操作类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class FileResource {

    private static final Logger logger = LoggerFactory.getLogger(FileResource.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FileResource(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 上传文件（异步）
     *
     * @param workspaceId              空间ID
     * @param category                 文件类别
     * @param file                     文件对象
     * @param batchNumber              批次编号（可选）
     * @param autoVerifyVat            是否自动核验增值税发票（可选）
     * @param splitFlag                是否启用文档拆分（可选）
     * @param cropFlag                 是否启用多图切分（可选）
     * @param targetProcess            处理目标（recognition, classification）（可选）
     * @param parserRemoveWatermark    是否移除水印（可选）
     * @param parserCropDewarp         是否裁剪和去畸变（可选）
     * @param parserApplyMerge         是否应用合并（可选）
     * @param parserFormulaLevel       公式识别级别（可选）
     * @param parserTableTextSplitMode 表格文字分割模式（可选）
     * @return 上传响应
     */
    public FileUploadResponse upload(
            String workspaceId,
            String category,
            File file,
            String batchNumber,
            Boolean autoVerifyVat,
            Boolean splitFlag,
            Boolean cropFlag,
            String targetProcess,
            Boolean parserRemoveWatermark,
            Boolean parserCropDewarp,
            Boolean parserApplyMerge,
            Integer parserFormulaLevel,
            String parserTableTextSplitMode
    ) {
        // 参数校验
        validateWorkspaceId(workspaceId);
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("文件类别不能为空");
        }
        if (file == null || !file.exists()) {
            throw new ValidationException("文件不存在");
        }

        logger.info("上传文件: workspaceId={}, category={}, fileName={}",
                workspaceId, category, file.getName());

        // 构建 multipart 请求体
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("workspace_id", workspaceId)
                .addFormDataPart("category", category)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")));

        // 添加可选参数
        if (batchNumber != null) {
            bodyBuilder.addFormDataPart("batch_number", batchNumber);
        }
        if (autoVerifyVat != null) {
            bodyBuilder.addFormDataPart("auto_verify_vat", String.valueOf(autoVerifyVat));
        }
        if (splitFlag != null) {
            bodyBuilder.addFormDataPart("split_flag", String.valueOf(splitFlag));
        }
        if (cropFlag != null) {
            bodyBuilder.addFormDataPart("crop_flag", String.valueOf(cropFlag));
        }
        if (targetProcess != null) {
            bodyBuilder.addFormDataPart("target_process", targetProcess);
        }
        if (parserRemoveWatermark != null) {
            bodyBuilder.addFormDataPart("parser_remove_watermark", String.valueOf(parserRemoveWatermark));
        }
        if (parserCropDewarp != null) {
            bodyBuilder.addFormDataPart("parser_crop_dewarp", String.valueOf(parserCropDewarp));
        }
        if (parserApplyMerge != null) {
            bodyBuilder.addFormDataPart("parser_apply_merge", String.valueOf(parserApplyMerge));
        }
        if (parserFormulaLevel != null) {
            bodyBuilder.addFormDataPart("parser_formula_level", String.valueOf(parserFormulaLevel));
        }
        if (parserTableTextSplitMode != null) {
            bodyBuilder.addFormDataPart("parser_table_text_split_mode", parserTableTextSplitMode);
        }

        // 发送请求
        JsonNode response = httpClient.postMultipart(
                DocflowConstants.API_PREFIX + "/file/upload",
                bodyBuilder.build()
        );

        // 解析响应
        FileUploadResponse result = objectMapper.convertValue(
                response.get("result"),
                FileUploadResponse.class
        );

        logger.info("文件上传成功: batchNumber={}", result.getBatchNumber());
        return result;
    }

    /**
     * 上传文件（异步，简化参数）
     */
    public FileUploadResponse upload(String workspaceId, String category, File file) {
        return upload(workspaceId, category, file, null, null, null, null,
                null, null, null, null, null, null);
    }

    /**
     * 通过URL上传文件（异步）
     *
     * @param workspaceId              空间ID
     * @param category                 文件类别
     * @param urls                     文件URL列表（最多10个）
     * @param batchNumber              批次编号（可选）
     * @param autoVerifyVat            是否自动核验增值税发票（可选）
     * @param splitFlag                是否启用文档拆分（可选）
     * @param cropFlag                 是否启用多图切分（可选）
     * @param targetProcess            处理目标（recognition, classification）（可选）
     * @param parserRemoveWatermark    是否移除水印（可选）
     * @param parserCropDewarp         是否裁剪和去畸变（可选）
     * @param parserApplyMerge         是否应用合并（可选）
     * @param parserFormulaLevel       公式识别级别（可选）
     * @param parserTableTextSplitMode 表格文字分割模式（可选）
     * @return 上传响应
     */
    public FileUploadResponse uploadByUrls(
            String workspaceId,
            String category,
            List<String> urls,
            String batchNumber,
            Boolean autoVerifyVat,
            Boolean splitFlag,
            Boolean cropFlag,
            String targetProcess,
            Boolean parserRemoveWatermark,
            Boolean parserCropDewarp,
            Boolean parserApplyMerge,
            Integer parserFormulaLevel,
            String parserTableTextSplitMode
    ) {
        logger.info("通过URL上传文件: workspaceId={}, category={}, urlCount={}",
                workspaceId, category, urls != null ? urls.size() : 0);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("文件类别不能为空");
        }
        if (urls == null || urls.isEmpty()) {
            throw new ValidationException("文件URL列表不能为空");
        }
        if (urls.size() > 10) {
            throw new ValidationException("文件URL列表最多支持10个");
        }

        // 验证每个URL格式
        for (String url : urls) {
            if (url == null || url.trim().isEmpty()) {
                throw new ValidationException("文件URL不能为空");
            }
            if (!url.matches("^https?://.*$")) {
                throw new ValidationException("文件URL必须以http://或https://开头: " + url);
            }
        }

        // 构建查询参数
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("workspace_id", workspaceId);
        queryParams.put("category", category);

        if (batchNumber != null) {
            queryParams.put("batch_number", batchNumber);
        }
        if (autoVerifyVat != null) {
            queryParams.put("auto_verify_vat", String.valueOf(autoVerifyVat));
        }
        if (splitFlag != null) {
            queryParams.put("split_flag", String.valueOf(splitFlag));
        }
        if (cropFlag != null) {
            queryParams.put("crop_flag", String.valueOf(cropFlag));
        }
        if (targetProcess != null) {
            queryParams.put("target_process", targetProcess);
        }
        if (parserRemoveWatermark != null) {
            queryParams.put("parser_remove_watermark", String.valueOf(parserRemoveWatermark));
        }
        if (parserCropDewarp != null) {
            queryParams.put("parser_crop_dewarp", String.valueOf(parserCropDewarp));
        }
        if (parserApplyMerge != null) {
            queryParams.put("parser_apply_merge", String.valueOf(parserApplyMerge));
        }
        if (parserFormulaLevel != null) {
            queryParams.put("parser_formula_level", String.valueOf(parserFormulaLevel));
        }
        if (parserTableTextSplitMode != null) {
            queryParams.put("parser_table_text_split_mode", parserTableTextSplitMode);
        }

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("urls", urls);

        // 发送请求（使用通用 request 方法，同时传递查询参数和 JSON 请求体）
        JsonNode response = httpClient.request(
                "POST",
                DocflowConstants.API_PREFIX + "/file/upload",
                queryParams,
                requestBody,
                null,
                null,
                null
        );

        // 解析响应
        FileUploadResponse result = objectMapper.convertValue(
                response.get("result"),
                FileUploadResponse.class
        );

        logger.info("通过URL上传文件成功: batchNumber={}", result.getBatchNumber());
        return result;
    }

    /**
     * 通过URL上传文件（异步，简化参数）
     *
     * @param workspaceId 空间ID
     * @param category    文件类别
     * @param urls        文件URL列表
     * @return 上传响应
     */
    public FileUploadResponse uploadByUrls(String workspaceId, String category, List<String> urls) {
        return uploadByUrls(workspaceId, category, urls, null, null, null, null,
                null, null, null, null, null, null);
    }

    /**
     * 同步上传文件（等待处理完成）
     *
     * @param workspaceId              空间ID
     * @param category                 文件类别
     * @param file                     文件对象
     * @param batchNumber              批次编号（可选）
     * @param autoVerifyVat            是否自动核验增值税发票（可选）
     * @param splitFlag                是否启用文档拆分（可选）
     * @param cropFlag                 是否启用多图切分（可选）
     * @param targetProcess            处理目标（recognition, classification）（可选）
     * @param parserRemoveWatermark    是否移除水印（可选）
     * @param parserCropDewarp         是否裁剪和去畸变（可选）
     * @param parserApplyMerge         是否应用合并（可选）
     * @param parserFormulaLevel       公式识别级别（可选）
     * @param parserTableTextSplitMode 表格文字分割模式（可选）
     * @param withTaskDetailUrl        是否返回任务详情页URL（可选）
     * @return 处理结果
     */
    public FileFetchResponse uploadSync(
            String workspaceId,
            String category,
            File file,
            String batchNumber,
            Boolean autoVerifyVat,
            Boolean splitFlag,
            Boolean cropFlag,
            String targetProcess,
            Boolean parserRemoveWatermark,
            Boolean parserCropDewarp,
            Boolean parserApplyMerge,
            Integer parserFormulaLevel,
            String parserTableTextSplitMode,
            Boolean withTaskDetailUrl
    ) {
        logger.info("同步上传文件: workspaceId={}, category={}, fileName={}",
                workspaceId, category, file.getName());

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("文件类别不能为空");
        }
        if (file == null || !file.exists()) {
            throw new ValidationException("文件不存在");
        }

        // 构建 multipart 请求体
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("workspace_id", workspaceId)
                .addFormDataPart("category", category)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")));

        // 添加可选参数
        if (batchNumber != null) {
            bodyBuilder.addFormDataPart("batch_number", batchNumber);
        }
        if (autoVerifyVat != null) {
            bodyBuilder.addFormDataPart("auto_verify_vat", String.valueOf(autoVerifyVat));
        }
        if (splitFlag != null) {
            bodyBuilder.addFormDataPart("split_flag", String.valueOf(splitFlag));
        }
        if (cropFlag != null) {
            bodyBuilder.addFormDataPart("crop_flag", String.valueOf(cropFlag));
        }
        if (targetProcess != null) {
            bodyBuilder.addFormDataPart("target_process", targetProcess);
        }
        if (parserRemoveWatermark != null) {
            bodyBuilder.addFormDataPart("parser_remove_watermark", String.valueOf(parserRemoveWatermark));
        }
        if (parserCropDewarp != null) {
            bodyBuilder.addFormDataPart("parser_crop_dewarp", String.valueOf(parserCropDewarp));
        }
        if (parserApplyMerge != null) {
            bodyBuilder.addFormDataPart("parser_apply_merge", String.valueOf(parserApplyMerge));
        }
        if (parserFormulaLevel != null) {
            bodyBuilder.addFormDataPart("parser_formula_level", String.valueOf(parserFormulaLevel));
        }
        if (parserTableTextSplitMode != null) {
            bodyBuilder.addFormDataPart("parser_table_text_split_mode", parserTableTextSplitMode);
        }
        if (withTaskDetailUrl != null) {
            bodyBuilder.addFormDataPart("with_task_detail_url", String.valueOf(withTaskDetailUrl));
        }

        // 发送请求
        JsonNode response = httpClient.postMultipart(
                DocflowConstants.API_PREFIX + "/file/upload/sync",
                bodyBuilder.build()
        );

        // 解析响应
        FileFetchResponse result = objectMapper.convertValue(
                response.get("result"),
                FileFetchResponse.class
        );

        logger.info("同步上传文件成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 同步上传文件（简化参数）
     */
    public FileFetchResponse uploadSync(String workspaceId, String category, File file) {
        return uploadSync(workspaceId, category, file, null, null, null, null,
                null, null, null, null, null, null, null);
    }

    /**
     * 通过URL同步上传文件（等待处理完成）
     *
     * @param workspaceId              空间ID
     * @param category                 文件类别
     * @param urls                     文件URL列表（最多10个）
     * @param batchNumber              批次编号（可选）
     * @param autoVerifyVat            是否自动核验增值税发票（可选）
     * @param splitFlag                是否启用文档拆分（可选）
     * @param cropFlag                 是否启用多图切分（可选）
     * @param targetProcess            处理目标（recognition, classification）（可选）
     * @param parserRemoveWatermark    是否移除水印（可选）
     * @param parserCropDewarp         是否裁剪和去畸变（可选）
     * @param parserApplyMerge         是否应用合并（可选）
     * @param parserFormulaLevel       公式识别级别（可选）
     * @param parserTableTextSplitMode 表格文字分割模式（可选）
     * @param withTaskDetailUrl        是否返回任务详情页URL（可选）
     * @return 处理结果
     */
    public FileFetchResponse uploadSyncByUrls(
            String workspaceId,
            String category,
            List<String> urls,
            String batchNumber,
            Boolean autoVerifyVat,
            Boolean splitFlag,
            Boolean cropFlag,
            String targetProcess,
            Boolean parserRemoveWatermark,
            Boolean parserCropDewarp,
            Boolean parserApplyMerge,
            Integer parserFormulaLevel,
            String parserTableTextSplitMode,
            Boolean withTaskDetailUrl
    ) {
        logger.info("通过URL同步上传文件: workspaceId={}, category={}, urlCount={}",
                workspaceId, category, urls != null ? urls.size() : 0);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("文件类别不能为空");
        }
        if (urls == null || urls.isEmpty()) {
            throw new ValidationException("文件URL列表不能为空");
        }
        if (urls.size() > 10) {
            throw new ValidationException("文件URL列表最多支持10个");
        }

        // 验证每个URL格式
        for (String url : urls) {
            if (url == null || url.trim().isEmpty()) {
                throw new ValidationException("文件URL不能为空");
            }
            if (!url.matches("^https?://.*$")) {
                throw new ValidationException("文件URL必须以http://或https://开头: " + url);
            }
        }

        // 构建查询参数
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("workspace_id", workspaceId);
        queryParams.put("category", category);

        if (batchNumber != null) {
            queryParams.put("batch_number", batchNumber);
        }
        if (autoVerifyVat != null) {
            queryParams.put("auto_verify_vat", String.valueOf(autoVerifyVat));
        }
        if (splitFlag != null) {
            queryParams.put("split_flag", String.valueOf(splitFlag));
        }
        if (cropFlag != null) {
            queryParams.put("crop_flag", String.valueOf(cropFlag));
        }
        if (targetProcess != null) {
            queryParams.put("target_process", targetProcess);
        }
        if (parserRemoveWatermark != null) {
            queryParams.put("parser_remove_watermark", String.valueOf(parserRemoveWatermark));
        }
        if (parserCropDewarp != null) {
            queryParams.put("parser_crop_dewarp", String.valueOf(parserCropDewarp));
        }
        if (parserApplyMerge != null) {
            queryParams.put("parser_apply_merge", String.valueOf(parserApplyMerge));
        }
        if (parserFormulaLevel != null) {
            queryParams.put("parser_formula_level", String.valueOf(parserFormulaLevel));
        }
        if (parserTableTextSplitMode != null) {
            queryParams.put("parser_table_text_split_mode", parserTableTextSplitMode);
        }
        if (withTaskDetailUrl != null) {
            queryParams.put("with_task_detail_url", String.valueOf(withTaskDetailUrl));
        }

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("urls", urls);

        // 发送请求
        JsonNode response = httpClient.request(
                "POST",
                DocflowConstants.API_PREFIX + "/file/upload/sync",
                queryParams,
                requestBody,
                null,
                null,
                null
        );

        // 解析响应
        FileFetchResponse result = objectMapper.convertValue(
                response.get("result"),
                FileFetchResponse.class
        );

        logger.info("通过URL同步上传文件成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 通过URL同步上传文件（简化参数）
     *
     * @param workspaceId 空间ID
     * @param category    文件类别
     * @param urls        文件URL列表
     * @return 处理结果
     */
    public FileFetchResponse uploadSyncByUrls(String workspaceId, String category, List<String> urls) {
        return uploadSyncByUrls(workspaceId, category, urls, null, null, null, null,
                null, null, null, null, null, null, null);
    }

    /**
     * 获取文件处理结果列表
     *
     * @param workspaceId          空间ID
     * @param page                 页码（默认1）
     * @param pageSize             每页数量（默认1000）
     * @param batchNumber          批次编号（可选）
     * @param fileId               文件ID（可选）
     * @param category             文件类别（可选）
     * @param recognitionStatus    识别状态（可选）
     * @param verificationStatus   核对状态（可选）
     * @param startTime            开始时间（可选）
     * @param endTime              结束时间（可选）
     * @param withDocument         是否返回文档的全部文字识别结果（默认false）
     * @param withTaskDetailUrl    是否返回任务详情页URL（默认false）
     * @return 文件列表
     */
    public FileFetchResponse fetch(
            String workspaceId,
            Integer page,
            Integer pageSize,
            String batchNumber,
            String fileId,
            String category,
            String recognitionStatus,
            Integer verificationStatus,
            String startTime,
            String endTime,
            Boolean withDocument,
            Boolean withTaskDetailUrl
    ) {
        logger.info("获取文件处理结果列表: workspaceId={}, page={}, pageSize={}",
                workspaceId, page, pageSize);

        // 参数校验
        validateWorkspaceId(workspaceId);

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("page", String.valueOf(page != null ? page : DocflowConstants.DEFAULT_PAGE));
        params.put("page_size", String.valueOf(pageSize != null ? pageSize : DocflowConstants.MAX_PAGE_SIZE));
        params.put("with_document", String.valueOf(withDocument != null ? withDocument : false));
        params.put("with_task_detail_url", String.valueOf(withTaskDetailUrl != null ? withTaskDetailUrl : false));

        // 添加可选过滤条件
        if (batchNumber != null) {
            params.put("batch_number", batchNumber);
        }
        if (fileId != null) {
            params.put("file_id", fileId);
        }
        if (category != null) {
            params.put("category", category);
        }
        if (recognitionStatus != null) {
            params.put("recognition_status", recognitionStatus);
        }
        if (verificationStatus != null) {
            params.put("verification_status", String.valueOf(verificationStatus));
        }
        if (startTime != null) {
            params.put("start_time", startTime);
        }
        if (endTime != null) {
            params.put("end_time", endTime);
        }

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/file/fetch",
                params
        );

        // 解析响应
        FileFetchResponse result = objectMapper.convertValue(
                response.get("result"),
                FileFetchResponse.class
        );

        logger.info("获取文件列表成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 获取文件处理结果列表（简化参数）
     */
    public FileFetchResponse fetch(String workspaceId) {
        return fetch(workspaceId, null, null, null, null, null,
                null, null, null, null, null, null);
    }

    /**
     * 迭代获取所有文件
     * <p>
     * 自动处理分页，返回可迭代对象，支持 for-each 循环和 Stream API
     * </p>
     *
     * @param workspaceId         空间ID
     * @param pageSize            每页数量（默认1000）
     * @param maxPages            最大页数限制（可选，null表示不限制）
     * @param batchNumber         批次编号（可选）
     * @param fileId              文件ID（可选）
     * @param category            文件类别（可选）
     * @param recognitionStatus   识别状态（可选）
     * @param verificationStatus  核对状态（可选）
     * @param startTime           开始时间（可选）
     * @param endTime             结束时间（可选）
     * @param withDocument        是否返回文档的全部文字识别结果（默认false）
     * @param withTaskDetailUrl   是否返回任务详情页URL（默认false）
     * @return 文件信息的可迭代对象
     */
    public PageIterable<FileInfo> iter(
            String workspaceId,
            Integer pageSize,
            Integer maxPages,
            String batchNumber,
            String fileId,
            String category,
            String recognitionStatus,
            Integer verificationStatus,
            String startTime,
            String endTime,
            Boolean withDocument,
            Boolean withTaskDetailUrl
    ) {
        logger.info("创建文件迭代器: workspaceId={}, pageSize={}, maxPages={}",
                workspaceId, pageSize, maxPages);

        // 参数校验
        validateWorkspaceId(workspaceId);

        final int finalPageSize = pageSize != null ? pageSize : DocflowConstants.MAX_PAGE_SIZE;

        // 创建分页数据获取器
        PageIterator.PageFetcher<FileInfo> fetcher = page -> {
            FileFetchResponse response = fetch(
                    workspaceId,
                    page,
                    finalPageSize,
                    batchNumber,
                    fileId,
                    category,
                    recognitionStatus,
                    verificationStatus,
                    startTime,
                    endTime,
                    withDocument,
                    withTaskDetailUrl
            );

            return new PageIterator.PageResult<>(
                    response.getFiles(),
                    response.getTotal(),
                    finalPageSize
            );
        };

        return new PageIterable<>(fetcher, maxPages);
    }

    /**
     * 迭代获取所有文件（简化参数）
     *
     * @param workspaceId 空间ID
     * @return 文件信息的可迭代对象
     */
    public PageIterable<FileInfo> iter(String workspaceId) {
        return iter(workspaceId, null, null, null, null, null,
                null, null, null, null, null, null);
    }

    /**
     * 迭代获取所有文件（指定筛选条件）
     *
     * @param workspaceId        空间ID
     * @param batchNumber        批次编号
     * @param category           文件类别
     * @param recognitionStatus  识别状态
     * @param verificationStatus 核对状态
     * @return 文件信息的可迭代对象
     */
    public PageIterable<FileInfo> iter(
            String workspaceId,
            String batchNumber,
            String category,
            String recognitionStatus,
            Integer verificationStatus
    ) {
        return iter(workspaceId, null, null, batchNumber, null, category,
                recognitionStatus, verificationStatus, null, null, null, null);
    }

    /**
     * 更新文件处理结果
     *
     * @param workspaceId 空间ID
     * @param fileId      文件ID
     * @param data        更新的数据（包含 fields 和 items）
     * @return 更新响应
     */
    public FileUpdateResponse update(String workspaceId, String fileId, FileUpdateData data) {
        logger.info("更新文件处理结果: workspaceId={}, fileId={}", workspaceId, fileId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new ValidationException("文件ID不能为空");
        }
        if (data == null) {
            throw new ValidationException("更新数据不能为空");
        }

        // 构建请求体
        FileUpdateRequest request = FileUpdateRequest.builder()
                .workspaceId(workspaceId)
                .fileId(fileId)
                .data(data)
                .build();

        List<FileUpdateRequest> payload = new ArrayList<>();
        payload.add(request);

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/file/update",
                payload
        );

        // 解析响应
        FileUpdateResponse result = objectMapper.convertValue(
                response.get("result"),
                FileUpdateResponse.class
        );

        logger.info("更新文件处理结果成功: fileId={}", fileId);
        return result;
    }

    /**
     * 批量更新文件处理结果
     *
     * @param updates 更新请求列表
     * @return 更新响应
     */
    public FileUpdateResponse batchUpdate(List<FileUpdateRequest> updates) {
        logger.info("批量更新文件处理结果: count={}", updates != null ? updates.size() : 0);

        // 参数校验
        if (updates == null || updates.isEmpty()) {
            throw new ValidationException("更新列表不能为空");
        }

        // 验证每个请求
        for (FileUpdateRequest update : updates) {
            if (update.getWorkspaceId() == null || update.getWorkspaceId().trim().isEmpty()) {
                throw new ValidationException("工作空间ID不能为空");
            }
            if (update.getFileId() == null || update.getFileId().trim().isEmpty()) {
                throw new ValidationException("文件ID不能为空");
            }
            if (update.getData() == null) {
                throw new ValidationException("更新数据不能为空");
            }
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/file/update",
                updates
        );

        // 解析响应
        FileUpdateResponse result = objectMapper.convertValue(
                response.get("result"),
                FileUpdateResponse.class
        );

        logger.info("批量更新文件处理结果成功: count={}", result.getFiles().size());
        return result;
    }

    /**
     * 删除文件
     *
     * @param workspaceId  空间ID
     * @param batchNumber  批次编号列表（可选）
     * @param taskId       任务ID列表（可选）
     * @param fileId       文件ID列表（可选）
     * @param startTime    开始时间（Unix时间戳，可选）
     * @param endTime      结束时间（Unix时间戳，可选）
     * @return 删除响应
     */
    public FileDeleteResponse delete(
            String workspaceId,
            List<String> batchNumber,
            List<String> taskId,
            List<String> fileId,
            Long startTime,
            Long endTime
    ) {
        logger.info("删除文件: workspaceId={}", workspaceId);

        // 参数校验
        validateWorkspaceId(workspaceId);

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);

        if (batchNumber != null && !batchNumber.isEmpty()) {
            payload.put("batch_number", batchNumber);
        }
        if (taskId != null && !taskId.isEmpty()) {
            payload.put("task_id", taskId);
        }
        if (fileId != null && !fileId.isEmpty()) {
            payload.put("file_id", fileId);
        }
        if (startTime != null) {
            payload.put("start_time", startTime);
        }
        if (endTime != null) {
            payload.put("end_time", endTime);
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/file/delete",
                payload
        );

        // 解析响应
        FileDeleteResponse result = objectMapper.convertValue(
                response.get("result"),
                FileDeleteResponse.class
        );

        logger.info("删除文件成功: deletedCount={}", result.getDeletedCount());
        return result;
    }

    /**
     * 抽取特定字段
     *
     * @param workspaceId 空间ID
     * @param taskId      任务ID
     * @param fields      字段列表（可选）
     * @param tables      表格列表（可选）
     * @return 抽取结果
     */
    public FileFetchResponse extractFields(
            String workspaceId,
            String taskId,
            List<ExtractFieldRequest> fields,
            List<ExtractTableRequest> tables
    ) {
        logger.info("抽取特定字段: workspaceId={}, taskId={}", workspaceId, taskId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new ValidationException("任务ID不能为空");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);

        if (fields != null && !fields.isEmpty()) {
            payload.put("fields", fields);
        }
        if (tables != null && !tables.isEmpty()) {
            payload.put("tables", tables);
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/file/extract_fields",
                payload
        );

        // 解析响应
        FileFetchResponse result = objectMapper.convertValue(
                response.get("result"),
                FileFetchResponse.class
        );

        logger.info("抽取特定字段成功: taskId={}", taskId);
        return result;
    }

    /**
     * 重新处理文件
     *
     * @param workspaceId  空间ID
     * @param taskId       任务ID
     * @param parserParams 解析参数（可选）
     */
    public void retry(String workspaceId, String taskId, DocumentParserParams parserParams) {
        logger.info("重新处理文件: workspaceId={}, taskId={}", workspaceId, taskId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new ValidationException("任务ID不能为空");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);

        if (parserParams != null) {
            payload.put("parser_params", parserParams);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/file/retry",
                payload
        );

        logger.info("重新处理文件成功: taskId={}", taskId);
    }

    /**
     * 重新处理文件（简化参数）
     */
    public void retry(String workspaceId, String taskId) {
        retry(workspaceId, taskId, null);
    }

    /**
     * 修改文件类别
     *
     * @param workspaceId 空间ID
     * @param taskId      任务ID
     * @param category    新文件类别（普通任务，可选）
     * @param splitTasks  文档拆分任务列表（可选）
     * @param cropTasks   多图切分任务列表（可选）
     */
    public void amendCategory(
            String workspaceId,
            String taskId,
            String category,
            List<SplitTaskRequest> splitTasks,
            List<CropTaskRequest> cropTasks
    ) {
        logger.info("修改文件类别: workspaceId={}, taskId={}", workspaceId, taskId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new ValidationException("任务ID不能为空");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);

        if (category != null) {
            payload.put("category", category);
        }
        if (splitTasks != null && !splitTasks.isEmpty()) {
            payload.put("split_tasks", splitTasks);
        }
        if (cropTasks != null && !cropTasks.isEmpty()) {
            payload.put("crop_tasks", cropTasks);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/file/amend_category",
                payload
        );

        logger.info("修改文件类别成功: taskId={}", taskId);
    }

    /**
     * 修改文件类别（简化参数）
     */
    public void amendCategory(String workspaceId, String taskId, String category) {
        amendCategory(workspaceId, taskId, category, null, null);
    }

    /**
     * 校验工作空间ID
     */
    private void validateWorkspaceId(String workspaceId) {
        if (workspaceId == null || workspaceId.trim().isEmpty()) {
            throw new ValidationException("工作空间ID不能为空");
        }
    }
}
