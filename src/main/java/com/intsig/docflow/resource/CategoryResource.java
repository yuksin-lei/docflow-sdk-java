package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.enums.ExtractModel;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.iterator.PageIterator;
import com.intsig.docflow.model.category.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类别资源操作类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryResource {

    private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CategoryResource(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取类别列表
     *
     * @param workspaceId 工作空间ID
     * @param page        页码（默认1）
     * @param pageSize    每页数量（默认20）
     * @param enabled     启用状态（默认"1"）
     * @return 类别列表
     */
    public CategoryListResponse list(String workspaceId, Integer page, Integer pageSize, String enabled) {
        logger.info("获取类别列表: workspaceId={}, page={}, pageSize={}, enabled={}",
                workspaceId, page, pageSize, enabled);

        // 参数校验
        validateWorkspaceId(workspaceId);

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("page", String.valueOf(page != null ? page : DocflowConstants.DEFAULT_PAGE));
        params.put("page_size", String.valueOf(pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE));
        params.put("enabled", enabled != null ? enabled : EnabledStatus.ENABLED.getValue());

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/category/list",
                params
        );

        // 解析响应
        CategoryListResponse result = objectMapper.convertValue(
                response.get("result"),
                CategoryListResponse.class
        );

        logger.info("获取类别列表成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 获取类别列表（使用枚举状态）
     *
     * @param workspaceId 工作空间ID
     * @param page        页码
     * @param pageSize    每页数量
     * @param enabled     启用状态枚举
     * @return 类别列表
     */
    public CategoryListResponse list(String workspaceId, Integer page, Integer pageSize, EnabledStatus enabled) {
        return list(workspaceId, page, pageSize, enabled != null ? enabled.getValue() : null);
    }

    /**
     * 获取类别列表（简化版）
     *
     * @param workspaceId 工作空间ID
     * @return 类别列表
     */
    public CategoryListResponse list(String workspaceId) {
        return list(workspaceId, null, null, (String) null);
    }

    /**
     * 迭代获取所有类别
     * <p>
     * 自动处理分页，返回可迭代对象，支持 for-each 循环和 Stream API
     * </p>
     *
     * @param workspaceId 工作空间ID
     * @param pageSize    每页数量（默认20）
     * @param maxPages    最大页数限制（可选，null表示不限制）
     * @param enabled     启用状态（默认"1"）
     * @return 类别信息的可迭代对象
     */
    public PageIterable<CategoryInfo> iter(String workspaceId, Integer pageSize, Integer maxPages, String enabled) {
        logger.info("创建类别迭代器: workspaceId={}, pageSize={}, maxPages={}, enabled={}",
                workspaceId, pageSize, maxPages, enabled);

        // 参数校验
        validateWorkspaceId(workspaceId);

        final int finalPageSize = pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE;
        final String finalEnabled = enabled != null ? enabled : EnabledStatus.ENABLED.getValue();

        // 创建分页数据获取器
        PageIterator.PageFetcher<CategoryInfo> fetcher = page -> {
            CategoryListResponse response = list(workspaceId, page, finalPageSize, finalEnabled);

            return new PageIterator.PageResult<>(
                    response.getCategories(),
                    response.getTotal() != null ? response.getTotal().intValue() : 0,
                    finalPageSize
            );
        };

        return new PageIterable<>(fetcher, maxPages);
    }

    /**
     * 迭代获取所有类别（简化参数）
     *
     * @param workspaceId 工作空间ID
     * @return 类别信息的可迭代对象
     */
    public PageIterable<CategoryInfo> iter(String workspaceId) {
        return iter(workspaceId, null, null, null);
    }

    /**
     * 迭代获取所有类别（使用枚举状态）
     *
     * @param workspaceId 工作空间ID
     * @param enabled     启用状态枚举
     * @return 类别信息的可迭代对象
     */
    public PageIterable<CategoryInfo> iter(String workspaceId, EnabledStatus enabled) {
        return iter(workspaceId, null, null, enabled != null ? enabled.getValue() : null);
    }

    /**
     * 创建文件类别
     *
     * @param workspaceId     工作空间ID
     * @param name            类别名称
     * @param categoryPrompt  分类提示词
     * @param extractModel    抽取模型
     * @param sampleFiles     样本文件列表
     * @param fields          字段配置列表
     * @return 创建响应
     */
    public CategoryCreateResponse create(String workspaceId, String name, String categoryPrompt,
                                         ExtractModel extractModel, List<File> sampleFiles,
                                         List<CategoryFieldConfig> fields) {
        logger.info("创建文件类别: workspaceId={}, name={}", workspaceId, name);

        validateWorkspaceId(workspaceId);
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("类别名称不能为空", "error.validation.name_required");
        }
        if (extractModel == null) {
            throw new ValidationException("抽取模型不能为空", "error.validation.extract_model_required");
        }
        if (sampleFiles == null || sampleFiles.isEmpty()) {
            throw new ValidationException("至少需要上传一个样本文件", "error.validation.sample_files_required");
        }
        if (fields == null || fields.isEmpty()) {
            throw new ValidationException("至少需要配置一个字段", "error.validation.fields_required");
        }

        try {
            // 构建 multipart 请求
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("workspace_id", workspaceId)
                    .addFormDataPart("name", name)
                    .addFormDataPart("extract_model", extractModel.getValue());

            if (categoryPrompt != null) {
                builder.addFormDataPart("category_prompt", categoryPrompt);
            }

            // 添加样本文件
            for (File file : sampleFiles) {
                builder.addFormDataPart("sample_files", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")));
            }

            // 添加字段配置
            String fieldsJson = objectMapper.writeValueAsString(fields);
            builder.addFormDataPart("fields", fieldsJson);

            JsonNode response = httpClient.postMultipart(
                    DocflowConstants.API_PREFIX + "/category/create",
                    builder.build()
            );

            CategoryCreateResponse result = objectMapper.convertValue(
                    response.get("result"),
                    CategoryCreateResponse.class
            );

            logger.info("创建文件类别成功: categoryId={}", result.getCategoryId());
            return result;
        } catch (IOException e) {
            throw new ValidationException("字段配置序列化失败", "error.validation.fields_invalid");
        }
    }

    /**
     * 更新文件类别
     *
     * @param workspaceId    工作空间ID
     * @param categoryId     类别ID
     * @param name           类别名称
     * @param categoryPrompt 分类提示词
     * @param enabled        启用状态
     */
    public void update(String workspaceId, String categoryId, String name, String categoryPrompt, EnabledStatus enabled) {
        logger.info("更新文件类别: workspaceId={}, categoryId={}", workspaceId, categoryId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        if (name != null) {
            data.put("name", name);
        }
        if (categoryPrompt != null) {
            data.put("category_prompt", categoryPrompt);
        }
        if (enabled != null) {
            data.put("enabled", enabled.getValue());
        }

        httpClient.post(DocflowConstants.API_PREFIX + "/category/update", data);
        logger.info("更新文件类别成功");
    }

    /**
     * 删除文件类别
     *
     * @param workspaceId 工作空间ID
     * @param categoryIds 类别ID列表
     */
    public void delete(String workspaceId, List<String> categoryIds) {
        logger.info("删除文件类别: workspaceId={}, categoryIds={}", workspaceId, categoryIds);

        validateWorkspaceId(workspaceId);
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new ValidationException("类别ID列表不能为空", "error.validation.category_ids_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_ids", categoryIds);

        httpClient.post(DocflowConstants.API_PREFIX + "/category/delete", data);
        logger.info("删除文件类别成功: count={}", categoryIds.size());
    }

    // ==================== 表格管理 ====================

    /**
     * 获取文件类别下的表格列表
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @return 表格列表
     */
    public CategoryTablesListResponse listTables(String workspaceId, String categoryId) {
        logger.info("获取表格列表: workspaceId={}, categoryId={}", workspaceId, categoryId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);

        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("category_id", categoryId);

        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/category/tables/list",
                params
        );

        CategoryTablesListResponse result = objectMapper.convertValue(
                response.get("result"),
                CategoryTablesListResponse.class
        );

        logger.info("获取表格列表成功: count={}", result.getTables() != null ? result.getTables().size() : 0);
        return result;
    }

    /**
     * 新增文件类别表格
     *
     * @param workspaceId            工作空间ID
     * @param categoryId             类别ID
     * @param name                   表格名称
     * @param prompt                 表格语义抽取提示词
     * @param collectFromMultiTable  多表合并
     * @return 表格ID
     */
    public String addTable(String workspaceId, String categoryId, String name, String prompt, Boolean collectFromMultiTable) {
        logger.info("新增表格: workspaceId={}, categoryId={}, name={}", workspaceId, categoryId, name);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("表格名称不能为空", "error.validation.name_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("name", name);
        if (prompt != null) {
            data.put("prompt", prompt);
        }
        if (collectFromMultiTable != null) {
            data.put("collect_from_multi_table", collectFromMultiTable);
        }

        JsonNode response = httpClient.post(DocflowConstants.API_PREFIX + "/category/tables/add", data);
        IdResponse result = objectMapper.convertValue(response.get("result"), IdResponse.class);

        logger.info("新增表格成功: tableId={}", result.getTableId());
        return result.getTableId();
    }

    /**
     * 更新文件类别表格
     *
     * @param workspaceId            工作空间ID
     * @param categoryId             类别ID
     * @param tableId                表格ID
     * @param name                   表格名称
     * @param prompt                 表格语义抽取提示词
     * @param collectFromMultiTable  多表合并
     */
    public void updateTable(String workspaceId, String categoryId, String tableId, String name,
                           String prompt, Boolean collectFromMultiTable) {
        logger.info("更新表格: workspaceId={}, categoryId={}, tableId={}", workspaceId, categoryId, tableId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (tableId == null || tableId.trim().isEmpty()) {
            throw new ValidationException("表格ID不能为空", "error.validation.table_id_required");
        }
        if (collectFromMultiTable == null) {
            throw new ValidationException("多表合并参数不能为空", "error.validation.collect_from_multi_table_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("table_id", tableId);
        data.put("collect_from_multi_table", collectFromMultiTable);
        if (name != null) {
            data.put("name", name);
        }
        if (prompt != null) {
            data.put("prompt", prompt);
        }

        httpClient.post(DocflowConstants.API_PREFIX + "/category/tables/update", data);
        logger.info("更新表格成功");
    }

    /**
     * 删除文件类别表格
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param tableIds    表格ID列表
     */
    public void deleteTables(String workspaceId, String categoryId, List<String> tableIds) {
        logger.info("删除表格: workspaceId={}, categoryId={}, tableIds={}", workspaceId, categoryId, tableIds);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (tableIds == null || tableIds.isEmpty()) {
            throw new ValidationException("表格ID列表不能为空", "error.validation.table_ids_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("table_ids", tableIds);

        httpClient.post(DocflowConstants.API_PREFIX + "/category/tables/delete", data);
        logger.info("删除表格成功: count={}", tableIds.size());
    }

    // ==================== 字段管理 ====================

    /**
     * 获取文件类别字段列表
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @return 字段列表
     */
    public CategoryFieldsListResponse listFields(String workspaceId, String categoryId) {
        logger.info("获取字段列表: workspaceId={}, categoryId={}", workspaceId, categoryId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);

        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("category_id", categoryId);

        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/category/fields/list",
                params
        );

        CategoryFieldsListResponse result = objectMapper.convertValue(
                response.get("result"),
                CategoryFieldsListResponse.class
        );

        logger.info("获取字段列表成功");
        return result;
    }

    /**
     * 新增文件类别字段
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param tableId     表格ID（可选，不传则创建普通字段）
     * @param fieldConfig 字段配置
     * @return 字段ID
     */
    public String addField(String workspaceId, String categoryId, String tableId, CategoryFieldConfig fieldConfig) {
        logger.info("新增字段: workspaceId={}, categoryId={}, tableId={}", workspaceId, categoryId, tableId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (fieldConfig == null || fieldConfig.getName() == null) {
            throw new ValidationException("字段配置不能为空", "error.validation.field_config_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        if (tableId != null && !tableId.trim().isEmpty()) {
            data.put("table_id", tableId);
        }
        data.put("name", fieldConfig.getName());
        if (fieldConfig.getDescription() != null) {
            data.put("description", fieldConfig.getDescription());
        }
        if (fieldConfig.getPrompt() != null) {
            data.put("prompt", fieldConfig.getPrompt());
        }
        if (fieldConfig.getUsePrompt() != null) {
            data.put("use_prompt", fieldConfig.getUsePrompt());
        }
        if (fieldConfig.getAlias() != null) {
            data.put("alias", fieldConfig.getAlias());
        }
        if (fieldConfig.getIdentity() != null) {
            data.put("identity", fieldConfig.getIdentity());
        }
        if (fieldConfig.getMultiValue() != null) {
            data.put("multi_value", fieldConfig.getMultiValue());
        }
        if (fieldConfig.getDuplicateValueDistinct() != null) {
            data.put("duplicate_value_distinct", fieldConfig.getDuplicateValueDistinct());
        }
        if (fieldConfig.getTransformSettings() != null) {
            data.put("transform_settings", fieldConfig.getTransformSettings());
        }

        JsonNode response = httpClient.post(DocflowConstants.API_PREFIX + "/category/fields/add", data);
        IdResponse result = objectMapper.convertValue(response.get("result"), IdResponse.class);

        logger.info("新增字段成功: fieldId={}", result.getFieldId());
        return result.getFieldId();
    }

    /**
     * 更新文件类别字段
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param fieldId     字段ID
     * @param tableId     表格ID（可选）
     * @param fieldConfig 字段配置
     */
    public void updateField(String workspaceId, String categoryId, String fieldId, String tableId, CategoryFieldConfig fieldConfig) {
        logger.info("更新字段: workspaceId={}, categoryId={}, fieldId={}", workspaceId, categoryId, fieldId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (fieldId == null || fieldId.trim().isEmpty()) {
            throw new ValidationException("字段ID不能为空", "error.validation.field_id_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("field_id", fieldId);
        if (tableId != null && !tableId.trim().isEmpty()) {
            data.put("table_id", tableId);
        }
        if (fieldConfig != null) {
            if (fieldConfig.getName() != null) {
                data.put("name", fieldConfig.getName());
            }
            if (fieldConfig.getDescription() != null) {
                data.put("description", fieldConfig.getDescription());
            }
            if (fieldConfig.getPrompt() != null) {
                data.put("prompt", fieldConfig.getPrompt());
            }
            if (fieldConfig.getUsePrompt() != null) {
                data.put("use_prompt", fieldConfig.getUsePrompt());
            }
            if (fieldConfig.getAlias() != null) {
                data.put("alias", fieldConfig.getAlias());
            }
            if (fieldConfig.getIdentity() != null) {
                data.put("identity", fieldConfig.getIdentity());
            }
            if (fieldConfig.getMultiValue() != null) {
                data.put("multi_value", fieldConfig.getMultiValue());
            }
            if (fieldConfig.getDuplicateValueDistinct() != null) {
                data.put("duplicate_value_distinct", fieldConfig.getDuplicateValueDistinct());
            }
            if (fieldConfig.getTransformSettings() != null) {
                data.put("transform_settings", fieldConfig.getTransformSettings());
            }
        }

        httpClient.post(DocflowConstants.API_PREFIX + "/category/fields/update", data);
        logger.info("更新字段成功");
    }

    /**
     * 删除文件类别字段
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param fieldIds    字段ID列表
     * @param tableId     表格ID（可选）
     */
    public void deleteFields(String workspaceId, String categoryId, List<String> fieldIds, String tableId) {
        logger.info("删除字段: workspaceId={}, categoryId={}, fieldIds={}, tableId={}",
                workspaceId, categoryId, fieldIds, tableId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (fieldIds == null || fieldIds.isEmpty()) {
            throw new ValidationException("字段ID列表不能为空", "error.validation.field_ids_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("field_ids", fieldIds);
        if (tableId != null && !tableId.trim().isEmpty()) {
            data.put("table_id", tableId);
        }

        httpClient.post(DocflowConstants.API_PREFIX + "/category/fields/delete", data);
        logger.info("删除字段成功: count={}", fieldIds.size());
    }

    // ==================== 样本管理 ====================

    /**
     * 上传类别样本
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param file        样本文件
     * @return 样本ID
     */
    public String uploadSample(String workspaceId, String categoryId, File file) {
        logger.info("上传样本: workspaceId={}, categoryId={}, fileName={}", workspaceId, categoryId, file.getName());

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (file == null || !file.exists()) {
            throw new ValidationException("样本文件不存在", "error.validation.file_not_exists");
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("workspace_id", workspaceId)
                .addFormDataPart("category_id", categoryId)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")));

        JsonNode response = httpClient.postMultipart(
                DocflowConstants.API_PREFIX + "/category/sample/upload",
                builder.build()
        );

        IdResponse result = objectMapper.convertValue(response.get("result"), IdResponse.class);

        logger.info("上传样本成功: sampleId={}", result.getSampleId());
        return result.getSampleId();
    }

    /**
     * 获取类别样本列表
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param page        页码
     * @param pageSize    每页数量
     * @return 样本列表
     */
    public CategorySampleListResponse listSamples(String workspaceId, String categoryId, Integer page, Integer pageSize) {
        logger.info("获取样本列表: workspaceId={}, categoryId={}, page={}, pageSize={}",
                workspaceId, categoryId, page, pageSize);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);

        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("category_id", categoryId);
        params.put("page", String.valueOf(page != null ? page : DocflowConstants.DEFAULT_PAGE));
        params.put("page_size", String.valueOf(pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE));

        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/category/sample/list",
                params
        );

        CategorySampleListResponse result = objectMapper.convertValue(
                response.get("result"),
                CategorySampleListResponse.class
        );

        logger.info("获取样本列表成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 获取类别样本列表（简化版）
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @return 样本列表
     */
    public CategorySampleListResponse listSamples(String workspaceId, String categoryId) {
        return listSamples(workspaceId, categoryId, null, null);
    }

    /**
     * 迭代获取所有样本
     * <p>
     * 自动处理分页，返回可迭代对象，支持 for-each 循环和 Stream API
     * </p>
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param pageSize    每页数量（默认20）
     * @param maxPages    最大页数限制（可选，null表示不限制）
     * @return 样本信息的可迭代对象
     */
    public PageIterable<CategorySample> iterSamples(
            String workspaceId,
            String categoryId,
            Integer pageSize,
            Integer maxPages
    ) {
        logger.info("创建样本迭代器: workspaceId={}, categoryId={}, pageSize={}, maxPages={}",
                workspaceId, categoryId, pageSize, maxPages);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);

        final int finalPageSize = pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE;

        // 创建分页数据获取器
        PageIterator.PageFetcher<CategorySample> fetcher = page -> {
            CategorySampleListResponse response = listSamples(workspaceId, categoryId, page, finalPageSize);

            return new PageIterator.PageResult<>(
                    response.getSamples(),
                    response.getTotal() != null ? response.getTotal().intValue() : 0,
                    finalPageSize
            );
        };

        return new PageIterable<>(fetcher, maxPages);
    }

    /**
     * 迭代获取所有样本（简化参数）
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @return 样本信息的可迭代对象
     */
    public PageIterable<CategorySample> iterSamples(String workspaceId, String categoryId) {
        return iterSamples(workspaceId, categoryId, null, null);
    }

    /**
     * 下载类别样本
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param sampleId    样本ID
     * @return 文件数据和文件名
     */
    public HttpClient.DownloadResult downloadSample(String workspaceId, String categoryId, String sampleId) {
        logger.info("下载样本: workspaceId={}, categoryId={}, sampleId={}", workspaceId, categoryId, sampleId);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (sampleId == null || sampleId.trim().isEmpty()) {
            throw new ValidationException("样本ID不能为空", "error.validation.sample_id_required");
        }

        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("category_id", categoryId);
        params.put("sample_id", sampleId);

        HttpClient.DownloadResult result = httpClient.download(
                DocflowConstants.API_PREFIX + "/category/sample/download",
                params
        );

        logger.info("下载样本成功: fileName={}", result.getFileName());
        return result;
    }

    /**
     * 删除类别样本
     *
     * @param workspaceId 工作空间ID
     * @param categoryId  类别ID
     * @param sampleIds   样本ID列表
     */
    public void deleteSamples(String workspaceId, String categoryId, List<String> sampleIds) {
        logger.info("删除样本: workspaceId={}, categoryId={}, sampleIds={}", workspaceId, categoryId, sampleIds);

        validateWorkspaceId(workspaceId);
        validateCategoryId(categoryId);
        if (sampleIds == null || sampleIds.isEmpty()) {
            throw new ValidationException("样本ID列表不能为空", "error.validation.sample_ids_required");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("workspace_id", workspaceId);
        data.put("category_id", categoryId);
        data.put("sample_ids", sampleIds);

        httpClient.post(DocflowConstants.API_PREFIX + "/category/sample/delete", data);
        logger.info("删除样本成功: count={}", sampleIds.size());
    }

    // ==================== 校验方法 ====================

    /**
     * 校验工作空间ID
     */
    private void validateWorkspaceId(String workspaceId) {
        if (workspaceId == null || workspaceId.trim().isEmpty()) {
            throw new ValidationException(
                    "工作空间ID不能为空",
                    "error.validation.workspace_id_required"
            );
        }

        if (!workspaceId.matches("^\\d+$")) {
            throw new ValidationException(
                    "工作空间ID格式无效",
                    "error.validation.workspace_id_invalid"
            );
        }

        if (workspaceId.length() > 19) {
            throw new ValidationException(
                    "工作空间ID格式无效",
                    "error.validation.workspace_id_invalid"
            );
        }
    }

    /**
     * 校验类别ID
     */
    private void validateCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            throw new ValidationException(
                    "类别ID不能为空",
                    "error.validation.category_id_required"
            );
        }
    }
}
