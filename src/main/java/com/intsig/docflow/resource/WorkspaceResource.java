package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.iterator.PageIterator;
import com.intsig.docflow.model.workspace.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作空间资源操作类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class WorkspaceResource {

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceResource.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WorkspaceResource(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 创建工作空间
     *
     * @param request 创建请求
     * @return 创建响应，包含工作空间ID
     */
    public WorkspaceCreateResponse create(WorkspaceCreateRequest request) {
        logger.info("创建工作空间: name={}", request.getName());

        // 参数校验
        validateCreateRequest(request);

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/workspace/create",
                request
        );

        // 解析响应
        WorkspaceCreateResponse result = objectMapper.convertValue(
                response.get("result"),
                WorkspaceCreateResponse.class
        );

        logger.info("工作空间创建成功: workspaceId={}", result.getWorkspaceId());
        return result;
    }

    /**
     * 获取工作空间列表
     *
     * @param enterpriseId 企业ID
     * @param page         页码（默认1）
     * @param pageSize     每页数量（默认20）
     * @return 工作空间列表
     */
    public WorkspaceListResponse list(Long enterpriseId, Integer page, Integer pageSize) {
        logger.info("获取工作空间列表: enterpriseId={}, page={}, pageSize={}",
                enterpriseId, page, pageSize);

        // 参数校验
        if (enterpriseId == null) {
            throw new ValidationException("企业ID不能为空", "error.workspace.enterprise_id_required");
        }

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("enterprise_id", String.valueOf(enterpriseId));
        params.put("page", String.valueOf(page != null ? page : DocflowConstants.DEFAULT_PAGE));
        params.put("page_size", String.valueOf(pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE));

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/workspace/list",
                params
        );

        // 解析响应
        WorkspaceListResponse result = objectMapper.convertValue(
                response.get("result"),
                WorkspaceListResponse.class
        );

        logger.info("获取工作空间列表成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 获取工作空间详情
     *
     * @param workspaceId 工作空间ID
     * @return 工作空间信息
     */
    public WorkspaceInfo get(String workspaceId) {
        logger.info("获取工作空间详情: workspaceId={}", workspaceId);

        // 参数校验
        validateWorkspaceId(workspaceId);

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/workspace/get",
                params
        );

        // 解析响应
        WorkspaceInfo result = objectMapper.convertValue(
                response.get("result"),
                WorkspaceInfo.class
        );

        logger.info("获取工作空间详情成功: workspaceId={}", workspaceId);
        return result;
    }

    /**
     * 更新工作空间
     *
     * @param workspaceId       工作空间ID
     * @param name              工作空间名称
     * @param authScope         协作范围（0:仅自己可见 1:企业成员可见）
     * @param description       空间描述（可选）
     * @param callbackUrl       回调URL（可选）
     * @param callbackRetryTime 回调重试次数（可选，0-3）
     */
    public void update(String workspaceId, String name, Integer authScope,
                       String description, String callbackUrl, Integer callbackRetryTime) {
        logger.info("更新工作空间: workspaceId={}, name={}", workspaceId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateUpdateRequest(name, authScope, callbackRetryTime);

        // 构建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("workspace_id", workspaceId);
        request.put("name", name);
        request.put("auth_scope", authScope);

        if (description != null) {
            request.put("description", description);
        }
        if (callbackUrl != null) {
            request.put("callback_url", callbackUrl);
        }
        if (callbackRetryTime != null) {
            request.put("callback_retry_time", callbackRetryTime);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/workspace/update",
                request
        );

        logger.info("更新工作空间成功: workspaceId={}", workspaceId);
    }

    /**
     * 更新工作空间（简化版，只更新基本信息）
     *
     * @param workspaceId 工作空间ID
     * @param name        工作空间名称
     * @param authScope   协作范围（0:仅自己可见 1:企业成员可见）
     */
    public void update(String workspaceId, String name, Integer authScope) {
        update(workspaceId, name, authScope, null, null, null);
    }

    /**
     * 删除工作空间
     *
     * @param workspaceIds 工作空间ID列表
     */
    public void delete(List<String> workspaceIds) {
        logger.info("删除工作空间: workspaceIds={}", workspaceIds);

        // 参数校验
        if (workspaceIds == null || workspaceIds.isEmpty()) {
            throw new ValidationException("工作空间ID列表不能为空");
        }

        for (String workspaceId : workspaceIds) {
            validateWorkspaceId(workspaceId);
        }

        // 构建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("workspace_ids", workspaceIds);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/workspace/delete",
                request
        );

        logger.info("删除工作空间成功: workspaceIds={}", workspaceIds);
    }

    /**
     * 删除单个工作空间
     *
     * @param workspaceId 工作空间ID
     */
    public void delete(String workspaceId) {
        List<String> workspaceIds = new ArrayList<>();
        workspaceIds.add(workspaceId);
        delete(workspaceIds);
    }

    /**
     * 迭代获取所有工作空间
     * <p>
     * 自动处理分页，返回可迭代对象，支持 for-each 循环和 Stream API
     * </p>
     *
     * @param enterpriseId 企业ID
     * @param pageSize     每页数量（默认20）
     * @param maxPages     最大页数限制（可选，null表示不限制）
     * @return 工作空间信息的可迭代对象
     */
    public PageIterable<WorkspaceInfo> iter(Long enterpriseId, Integer pageSize, Integer maxPages) {
        logger.info("创建工作空间迭代器: enterpriseId={}, pageSize={}, maxPages={}",
                enterpriseId, pageSize, maxPages);

        // 参数校验
        if (enterpriseId == null) {
            throw new ValidationException("企业ID不能为空", "error.workspace.enterprise_id_required");
        }

        final int finalPageSize = pageSize != null ? pageSize : DocflowConstants.DEFAULT_PAGE_SIZE;

        // 创建分页数据获取器
        PageIterator.PageFetcher<WorkspaceInfo> fetcher = page -> {
            WorkspaceListResponse response = list(enterpriseId, page, finalPageSize);

            return new PageIterator.PageResult<>(
                    response.getWorkspaces(),
                    response.getTotal() != null ? response.getTotal().intValue() : 0,
                    finalPageSize
            );
        };

        return new PageIterable<>(fetcher, maxPages);
    }

    /**
     * 迭代获取所有工作空间（简化参数）
     *
     * @param enterpriseId 企业ID
     * @return 工作空间信息的可迭代对象
     */
    public PageIterable<WorkspaceInfo> iter(Long enterpriseId) {
        return iter(enterpriseId, null, null);
    }

    /**
     * 校验创建请求参数
     */
    private void validateCreateRequest(WorkspaceCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("工作空间名称不能为空", "error.workspace.name_required");
        }

        if (request.getName().length() > 50) {
            throw new ValidationException("工作空间名称最大长度为50字符", "error.workspace.name_too_long");
        }

        if (request.getEnterpriseId() == null) {
            throw new ValidationException("企业ID不能为空", "error.workspace.enterprise_id_required");
        }

        if (request.getAuthScope() == null) {
            throw new ValidationException("协作范围不能为空", "error.workspace.auth_scope_invalid");
        }

        if (request.getAuthScope() != 0 && request.getAuthScope() != 1) {
            throw new ValidationException(
                    "协作范围必须是 0（私有）或 1（公共）",
                    "error.workspace.auth_scope_invalid"
            );
        }
    }

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

        // 校验是否为数字
        if (!workspaceId.matches("^\\d+$")) {
            throw new ValidationException(
                    "工作空间ID格式无效",
                    "error.validation.workspace_id_invalid"
            );
        }

        // 校验长度（最大19位）
        if (workspaceId.length() > 19) {
            throw new ValidationException(
                    "工作空间ID格式无效",
                    "error.validation.workspace_id_invalid"
            );
        }
    }

    /**
     * 校验更新请求参数
     */
    private void validateUpdateRequest(String name, Integer authScope, Integer callbackRetryTime) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("工作空间名称不能为空", "error.workspace.name_required");
        }

        if (name.length() > 50) {
            throw new ValidationException("工作空间名称最大长度为50字符", "error.workspace.name_too_long");
        }

        if (authScope == null) {
            throw new ValidationException("协作范围不能为空", "error.workspace.auth_scope_invalid");
        }

        if (authScope != 0 && authScope != 1) {
            throw new ValidationException(
                    "协作范围必须是 0（仅自己可见）或 1（企业成员可见）",
                    "error.workspace.auth_scope_invalid"
            );
        }

        if (callbackRetryTime != null && (callbackRetryTime < 1 || callbackRetryTime > 3)) {
            throw new ValidationException(
                    "回调重试次数必须在 1-3 之间",
                    "error.workspace.callback_retry_time_invalid"
            );
        }
    }
}
