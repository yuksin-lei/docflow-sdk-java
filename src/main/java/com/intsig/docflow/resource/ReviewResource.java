package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.enums.RiskLevel;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.iterator.PageIterator;
import com.intsig.docflow.model.review.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核资源操作类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ReviewResource {

    private static final Logger logger = LoggerFactory.getLogger(ReviewResource.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ReviewResource(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    // ==================== 规则库管理 ====================

    /**
     * 创建审核规则库
     *
     * @param workspaceId 空间ID
     * @param name        规则库名称（最大30字符）
     * @return 创建响应，包含规则库ID
     */
    public ReviewRepoCreateResponse createRepo(String workspaceId, String name) {
        logger.info("创建审核规则库: workspaceId={}, name={}", workspaceId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("规则库名称不能为空");
        }
        if (name.length() > 30) {
            throw new ValidationException("规则库名称不能超过30个字符");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("name", name);

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_repo/create",
                payload
        );

        // 解析响应
        ReviewRepoCreateResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewRepoCreateResponse.class
        );

        logger.info("创建审核规则库成功: repoId={}", result.getRepoId());
        return result;
    }

    /**
     * 获取审核规则库列表
     *
     * @param workspaceId 空间ID
     * @param page        页码（默认1）
     * @param pageSize    每页数量（默认10）
     * @return 规则库列表
     */
    public ReviewRepoListResponse listRepos(String workspaceId, Integer page, Integer pageSize) {
        logger.info("获取审核规则库列表: workspaceId={}, page={}, pageSize={}",
                workspaceId, page, pageSize);

        // 参数校验
        validateWorkspaceId(workspaceId);

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("page", String.valueOf(page != null ? page : DocflowConstants.DEFAULT_PAGE));
        params.put("page_size", String.valueOf(pageSize != null ? pageSize : 10));

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/review/rule_repo/list",
                params
        );

        // 解析响应
        ReviewRepoListResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewRepoListResponse.class
        );

        logger.info("获取审核规则库列表成功: total={}", result.getTotal());
        return result;
    }

    /**
     * 获取审核规则库列表（简化参数）
     */
    public ReviewRepoListResponse listRepos(String workspaceId) {
        return listRepos(workspaceId, null, null);
    }

    /**
     * 迭代获取所有审核规则库
     * <p>
     * 自动处理分页，返回可迭代对象，支持 for-each 循环和 Stream API
     * </p>
     *
     * @param workspaceId 空间ID
     * @param pageSize    每页数量（默认10）
     * @param maxPages    最大页数限制（可选，null表示不限制）
     * @return 审核规则库信息的可迭代对象
     */
    public PageIterable<ReviewRepoInfo> iterRepos(String workspaceId, Integer pageSize, Integer maxPages) {
        logger.info("创建审核规则库迭代器: workspaceId={}, pageSize={}, maxPages={}",
                workspaceId, pageSize, maxPages);

        // 参数校验
        validateWorkspaceId(workspaceId);

        final int finalPageSize = pageSize != null ? pageSize : 10;

        // 创建分页数据获取器
        PageIterator.PageFetcher<ReviewRepoInfo> fetcher = page -> {
            ReviewRepoListResponse response = listRepos(workspaceId, page, finalPageSize);

            return new PageIterator.PageResult<>(
                    response.getRepos(),
                    response.getTotal() != null ? response.getTotal() : 0,
                    finalPageSize
            );
        };

        return new PageIterable<>(fetcher, maxPages);
    }

    /**
     * 迭代获取所有审核规则库（简化参数）
     *
     * @param workspaceId 空间ID
     * @return 审核规则库信息的可迭代对象
     */
    public PageIterable<ReviewRepoInfo> iterRepos(String workspaceId) {
        return iterRepos(workspaceId, null, null);
    }

    /**
     * 获取审核规则库详情
     *
     * @param workspaceId 空间ID
     * @param repoId      规则库ID
     * @return 规则库详情
     */
    public ReviewRepoInfo getRepo(String workspaceId, String repoId) {
        logger.info("获取审核规则库详情: workspaceId={}, repoId={}", workspaceId, repoId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(repoId, "规则库ID");

        // 构建查询参数
        Map<String, String> params = new HashMap<>();
        params.put("workspace_id", workspaceId);
        params.put("repo_id", repoId);

        // 发送请求
        JsonNode response = httpClient.get(
                DocflowConstants.API_PREFIX + "/review/rule_repo/get",
                params
        );

        // 解析响应
        ReviewRepoInfo result = objectMapper.convertValue(
                response.get("result"),
                ReviewRepoInfo.class
        );

        logger.info("获取审核规则库详情成功: repoId={}", repoId);
        return result;
    }

    /**
     * 更新审核规则库
     *
     * @param workspaceId 空间ID
     * @param repoId      规则库ID
     * @param name        新的规则库名称
     */
    public void updateRepo(String workspaceId, String repoId, String name) {
        logger.info("更新审核规则库: workspaceId={}, repoId={}, name={}", workspaceId, repoId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(repoId, "规则库ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("repo_id", repoId);

        if (name != null && !name.trim().isEmpty()) {
            if (name.length() > 30) {
                throw new ValidationException("规则库名称不能超过30个字符");
            }
            payload.put("name", name);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_repo/update",
                payload
        );

        logger.info("更新审核规则库成功: repoId={}", repoId);
    }

    /**
     * 删除审核规则库
     *
     * @param workspaceId 空间ID
     * @param repoIds     规则库ID列表
     */
    public void deleteRepo(String workspaceId, List<String> repoIds) {
        logger.info("删除审核规则库: workspaceId={}, repoIds={}", workspaceId, repoIds);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (repoIds == null || repoIds.isEmpty()) {
            throw new ValidationException("规则库ID列表不能为空");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("repo_ids", repoIds);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_repo/delete",
                payload
        );

        logger.info("删除审核规则库成功: repoIds={}", repoIds);
    }

    // ==================== 规则组管理 ====================

    /**
     * 创建审核规则组
     *
     * @param workspaceId 空间ID
     * @param repoId      规则库ID
     * @param name        规则组名称（最大30字符）
     * @return 创建响应，包含规则组ID
     */
    public ReviewGroupCreateResponse createGroup(String workspaceId, String repoId, String name) {
        logger.info("创建审核规则组: workspaceId={}, repoId={}, name={}", workspaceId, repoId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(repoId, "规则库ID");
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("规则组名称不能为空");
        }
        if (name.length() > 30) {
            throw new ValidationException("规则组名称不能超过30个字符");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("repo_id", Integer.valueOf(repoId));
        payload.put("name", name);

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_group/create",
                payload
        );

        // 解析响应
        ReviewGroupCreateResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewGroupCreateResponse.class
        );

        logger.info("创建审核规则组成功: groupId={}", result.getGroupId());
        return result;
    }

    /**
     * 更新审核规则组
     *
     * @param workspaceId 空间ID
     * @param groupId     规则组ID
     * @param name        新的规则组名称
     */
    public void updateGroup(String workspaceId, String groupId, String name) {
        logger.info("更新审核规则组: workspaceId={}, groupId={}, name={}", workspaceId, groupId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(groupId, "规则组ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("group_id", groupId);

        if (name != null && !name.trim().isEmpty()) {
            if (name.length() > 30) {
                throw new ValidationException("规则组名称不能超过30个字符");
            }
            payload.put("name", name);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_group/update",
                payload
        );

        logger.info("更新审核规则组成功: groupId={}", groupId);
    }

    /**
     * 删除审核规则组
     *
     * @param workspaceId 空间ID
     * @param groupId     规则组ID
     */
    public void deleteGroup(String workspaceId, String groupId) {
        logger.info("删除审核规则组: workspaceId={}, groupId={}", workspaceId, groupId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(groupId, "规则组ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("group_id", groupId);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule_group/delete",
                payload
        );

        logger.info("删除审核规则组成功: groupId={}", groupId);
    }

    // ==================== 规则管理 ====================

    /**
     * 创建审核规则
     *
     * @param workspaceId        空间ID
     * @param repoId             规则库ID
     * @param groupId            规则组ID
     * @param name               规则名称
     * @param prompt             规则提示词（可选）
     * @param categoryIds        分类ID列表（可选）
     * @param riskLevel          风险等级（可选）
     * @param referencedFields   引用字段列表（可选）
     * @return 创建响应，包含规则ID
     */
    public ReviewRuleCreateResponse createRule(
            String workspaceId,
            String repoId,
            String groupId,
            String name,
            String prompt,
            List<String> categoryIds,
            RiskLevel riskLevel,
            List<ReviewRuleReferencedField> referencedFields
    ) {
        logger.info("创建审核规则: workspaceId={}, repoId={}, groupId={}, name={}",
                workspaceId, repoId, groupId, name);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(repoId, "规则库ID");
        validateId(groupId, "规则组ID");
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("规则名称不能为空");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("repo_id", Integer.valueOf(repoId));
        payload.put("group_id", groupId);
        payload.put("name", name);

        if (prompt != null && !prompt.trim().isEmpty()) {
            payload.put("prompt", prompt);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            payload.put("category_ids", categoryIds);
        }
        if (riskLevel != null) {
            payload.put("risk_level", riskLevel.getValue());
        }
        if (referencedFields != null && !referencedFields.isEmpty()) {
            payload.put("referenced_fields", referencedFields);
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule/create",
                payload
        );

        // 解析响应
        ReviewRuleCreateResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewRuleCreateResponse.class
        );

        logger.info("创建审核规则成功: ruleId={}", result.getRuleId());
        return result;
    }

    /**
     * 创建审核规则（简化参数）
     */
    public ReviewRuleCreateResponse createRule(
            String workspaceId,
            String repoId,
            String groupId,
            String name
    ) {
        return createRule(workspaceId, repoId, groupId, name, null, null, null, null);
    }

    /**
     * 更新审核规则
     *
     * @param workspaceId        空间ID
     * @param ruleId             规则ID
     * @param groupId            规则组ID（可选）
     * @param name               规则名称（可选）
     * @param prompt             规则提示词（可选）
     * @param categoryIds        分类ID列表（可选）
     * @param riskLevel          风险等级（可选）
     * @param referencedFields   引用字段列表（可选）
     */
    public void updateRule(
            String workspaceId,
            String ruleId,
            String groupId,
            String name,
            String prompt,
            List<String> categoryIds,
            RiskLevel riskLevel,
            List<ReviewRuleReferencedField> referencedFields
    ) {
        logger.info("更新审核规则: workspaceId={}, ruleId={}", workspaceId, ruleId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(ruleId, "规则ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("rule_id", ruleId);

        if (groupId != null && !groupId.trim().isEmpty()) {
            validateId(groupId, "规则组ID");
            payload.put("group_id", groupId);
        }
        if (name != null && !name.trim().isEmpty()) {
            payload.put("name", name);
        }
        if (prompt != null && !prompt.trim().isEmpty()) {
            payload.put("prompt", prompt);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            payload.put("category_ids", categoryIds);
        }
        if (riskLevel != null) {
            payload.put("risk_level", riskLevel.getValue());
        }
        if (referencedFields != null && !referencedFields.isEmpty()) {
            payload.put("referenced_fields", referencedFields);
        }

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule/update",
                payload
        );

        logger.info("更新审核规则成功: ruleId={}", ruleId);
    }

    /**
     * 更新审核规则（简化参数）
     */
    public void updateRule(String workspaceId, String ruleId, String name) {
        updateRule(workspaceId, ruleId, null, name, null, null, null, null);
    }

    /**
     * 删除审核规则
     *
     * @param workspaceId 空间ID
     * @param ruleId      规则ID
     */
    public void deleteRule(String workspaceId, String ruleId) {
        logger.info("删除审核规则: workspaceId={}, ruleId={}", workspaceId, ruleId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(ruleId, "规则ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("rule_id", ruleId);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/rule/delete",
                payload
        );

        logger.info("删除审核规则成功: ruleId={}", ruleId);
    }

    // ==================== 任务管理 ====================

    /**
     * 新建审核任务
     *
     * @param workspaceId     空间ID
     * @param name            任务名称（最大100字符）
     * @param repoId          审核规则库ID
     * @param extractTaskIds  抽取任务ID列表（可选）
     * @param batchNumber     批次号（可选）
     * @return 审核任务结果（包含task_id）
     */
    public ReviewTaskSubmitResponse submitTask(
            String workspaceId,
            String name,
            String repoId,
            List<String> extractTaskIds,
            String batchNumber
    ) {
        logger.info("新建审核任务: workspaceId={}, name={}, repoId={}", workspaceId, name, repoId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("任务名称不能为空");
        }
        if (name.length() > 100) {
            throw new ValidationException("任务名称不能超过100个字符");
        }
        validateId(repoId, "审核规则库ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("name", name);
        payload.put("repo_id", repoId);

        if (extractTaskIds != null && !extractTaskIds.isEmpty()) {
            // 校验每个抽取任务ID
            for (String taskId : extractTaskIds) {
                validateId(taskId, "抽取任务ID");
            }
            payload.put("extract_task_ids", extractTaskIds);
        }
        if (batchNumber != null) {
            payload.put("batch_number", batchNumber);
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/review/task/submit",
                payload
        );

        // 解析响应
        ReviewTaskSubmitResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewTaskSubmitResponse.class
        );

        logger.info("新建审核任务成功: taskId={}", result.getTaskId());
        return result;
    }

    /**
     * 新建审核任务（简化参数）
     */
    public ReviewTaskSubmitResponse submitTask(String workspaceId, String name, String repoId) {
        return submitTask(workspaceId, name, repoId, null, null);
    }

    /**
     * 删除审核任务
     *
     * @param workspaceId 空间ID
     * @param taskIds     审核任务ID列表
     */
    public void deleteTask(String workspaceId, List<String> taskIds) {
        logger.info("删除审核任务: workspaceId={}, taskIds={}", workspaceId, taskIds);

        // 参数校验
        validateWorkspaceId(workspaceId);
        if (taskIds == null || taskIds.isEmpty()) {
            throw new ValidationException("审核任务ID列表不能为空");
        }

        // 校验每个任务ID
        for (String taskId : taskIds) {
            validateId(taskId, "审核任务ID");
        }

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_ids", taskIds);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/task/delete",
                payload
        );

        logger.info("删除审核任务成功: taskIds={}", taskIds);
    }

    /**
     * 获取审核结果
     *
     * @param workspaceId       空间ID
     * @param taskId            审核任务ID
     * @param withTaskDetailUrl 是否返回审核详情页URL（可选）
     * @return 审核任务结果
     */
    public ReviewTaskResultResponse getTaskResult(
            String workspaceId,
            String taskId,
            Boolean withTaskDetailUrl
    ) {
        logger.info("获取审核结果: workspaceId={}, taskId={}", workspaceId, taskId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(taskId, "审核任务ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);

        if (withTaskDetailUrl != null) {
            payload.put("with_task_detail_url", withTaskDetailUrl);
        }

        // 发送请求
        JsonNode response = httpClient.post(
                DocflowConstants.API_PREFIX + "/review/task/result",
                payload
        );

        // 解析响应
        ReviewTaskResultResponse result = objectMapper.convertValue(
                response.get("result"),
                ReviewTaskResultResponse.class
        );

        logger.info("获取审核结果成功: taskId={}", taskId);
        return result;
    }

    /**
     * 获取审核结果（简化参数）
     */
    public ReviewTaskResultResponse getTaskResult(String workspaceId, String taskId) {
        return getTaskResult(workspaceId, taskId, null);
    }

    /**
     * 重新审核任务
     *
     * @param workspaceId 空间ID
     * @param taskId      审核任务ID
     */
    public void retryTask(String workspaceId, String taskId) {
        logger.info("重新审核任务: workspaceId={}, taskId={}", workspaceId, taskId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(taskId, "审核任务ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/task/retry",
                payload
        );

        logger.info("重新审核任务成功: taskId={}", taskId);
    }

    /**
     * 重新审核任务中的某条规则
     *
     * @param workspaceId 空间ID
     * @param taskId      审核任务ID
     * @param ruleId      审核规则ID
     */
    public void retryTaskRule(String workspaceId, String taskId, String ruleId) {
        logger.info("重新审核任务中的某条规则: workspaceId={}, taskId={}, ruleId={}",
                workspaceId, taskId, ruleId);

        // 参数校验
        validateWorkspaceId(workspaceId);
        validateId(taskId, "审核任务ID");
        validateId(ruleId, "审核规则ID");

        // 构建请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("workspace_id", workspaceId);
        payload.put("task_id", taskId);
        payload.put("rule_id", ruleId);

        // 发送请求
        httpClient.post(
                DocflowConstants.API_PREFIX + "/review/task/rule/retry",
                payload
        );

        logger.info("重新审核任务中的某条规则成功: taskId={}, ruleId={}", taskId, ruleId);
    }

    // ==================== 工具方法 ====================

    /**
     * 校验工作空间ID
     */
    private void validateWorkspaceId(String workspaceId) {
        if (workspaceId == null || workspaceId.trim().isEmpty()) {
            throw new ValidationException("工作空间ID不能为空");
        }
    }

    /**
     * 校验ID（数字字符串）
     */
    private void validateId(String id, String fieldName) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException(fieldName + "不能为空");
        }
        if (!id.matches("^\\d+$")) {
            throw new ValidationException(fieldName + "必须是数字字符串");
        }
    }
}
