package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.model.review.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ReviewResource 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class ReviewResourceTest {

    private HttpClient httpClient;
    private ReviewResource reviewResource;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        httpClient = mock(HttpClient.class);
        reviewResource = new ReviewResource(httpClient);
        objectMapper = new ObjectMapper();
    }

    // ==================== createRepo() 测试 ====================

    @Test
    public void testCreateRepo_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("repo_id", "12345");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewRepoCreateResponse response = reviewResource.createRepo(
                "1239840129384",
                "测试规则库"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("12345", response.getRepoId());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testCreateRepo_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.createRepo("", "测试规则库");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRepo(null, "测试规则库");
        });
    }

    @Test
    public void testCreateRepo_InvalidName() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.createRepo("1239840129384", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRepo("1239840129384", null);
        });
    }

    @Test
    public void testCreateRepo_NameTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 31; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRepo("1239840129384", longName);
        });
    }

    // ==================== listRepos() 测试 ====================

    @Test
    public void testListRepos_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        resultNode.put("page", 1);
        resultNode.put("page_size", 10);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewRepoListResponse response = reviewResource.listRepos("1239840129384");

        // 验证结果
        assertNotNull(response);
        assertEquals(5, response.getTotal());
        assertEquals(1, response.getPage());
        assertEquals(10, response.getPageSize());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testListRepos_WithPagination() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 100);
        resultNode.put("page", 2);
        resultNode.put("page_size", 20);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewRepoListResponse response = reviewResource.listRepos("1239840129384", 2, 20);

        // 验证结果
        assertNotNull(response);
        assertEquals(100, response.getTotal());
        assertEquals(2, response.getPage());
        assertEquals(20, response.getPageSize());
    }

    @Test
    public void testListRepos_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.listRepos("", null, null);
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.listRepos(null, null, null);
        });
    }

    // ==================== getRepo() 测试 ====================

    @Test
    public void testGetRepo_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("repo_id", "12345");
        resultNode.put("name", "测试规则库");
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewRepoInfo response = reviewResource.getRepo("1239840129384", "12345");

        // 验证结果
        assertNotNull(response);
        assertEquals("12345", response.getRepoId());
        assertEquals("测试规则库", response.getName());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testGetRepo_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.getRepo("", "12345");
        });
    }

    @Test
    public void testGetRepo_InvalidRepoId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.getRepo("1239840129384", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.getRepo("1239840129384", null);
        });
    }

    @Test
    public void testGetRepo_NonNumericRepoId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.getRepo("1239840129384", "abc");
        });
    }

    // ==================== updateRepo() 测试 ====================

    @Test
    public void testUpdateRepo_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.updateRepo("1239840129384", "12345", "新规则库名称");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdateRepo_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateRepo("", "12345", "新规则库名称");
        });
    }

    @Test
    public void testUpdateRepo_InvalidRepoId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateRepo("1239840129384", "", "新规则库名称");
        });
    }

    @Test
    public void testUpdateRepo_NameTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 31; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        assertThrows(ValidationException.class, () -> {
            reviewResource.updateRepo("1239840129384", "12345", longName);
        });
    }

    // ==================== deleteRepo() 测试 ====================

    @Test
    public void testDeleteRepo_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.deleteRepo("1239840129384", Arrays.asList("12345", "67890"));
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDeleteRepo_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteRepo("", Arrays.asList("12345"));
        });
    }

    @Test
    public void testDeleteRepo_EmptyRepoIds() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteRepo("1239840129384", new ArrayList<>());
        });
    }

    @Test
    public void testDeleteRepo_NullRepoIds() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteRepo("1239840129384", null);
        });
    }

    // ==================== createGroup() 测试 ====================

    @Test
    public void testCreateGroup_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("group_id", "98765");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewGroupCreateResponse response = reviewResource.createGroup(
                "1239840129384",
                "12345",
                "测试规则组"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("98765", response.getGroupId());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testCreateGroup_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("", "12345", "测试规则组");
        });
    }

    @Test
    public void testCreateGroup_InvalidRepoId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("1239840129384", "", "测试规则组");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("1239840129384", "abc", "测试规则组");
        });
    }

    @Test
    public void testCreateGroup_InvalidName() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("1239840129384", "12345", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("1239840129384", "12345", null);
        });
    }

    @Test
    public void testCreateGroup_NameTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 31; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createGroup("1239840129384", "12345", longName);
        });
    }

    // ==================== updateGroup() 测试 ====================

    @Test
    public void testUpdateGroup_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.updateGroup("1239840129384", "98765", "新规则组名称");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdateGroup_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateGroup("", "98765", "新规则组名称");
        });
    }

    @Test
    public void testUpdateGroup_InvalidGroupId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateGroup("1239840129384", "", "新规则组名称");
        });
    }

    // ==================== deleteGroup() 测试 ====================

    @Test
    public void testDeleteGroup_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.deleteGroup("1239840129384", "98765");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDeleteGroup_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteGroup("", "98765");
        });
    }

    @Test
    public void testDeleteGroup_InvalidGroupId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteGroup("1239840129384", "");
        });
    }

    // ==================== createRule() 测试 ====================

    @Test
    public void testCreateRule_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("rule_id", "11111");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewRuleCreateResponse response = reviewResource.createRule(
                "123234280394820",
                "98765",
                "123123",
                "测试规则"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("11111", response.getRuleId());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testCreateRule_WithExtraParams() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("rule_id", "11111");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备规则配置和额外参数
        Map<String, Object> config = new HashMap<>();
        config.put("threshold", 0.8);

        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("prompt", "这是提示词");
        extraParams.put("risk_level", 10);

        // 执行测试
        ReviewRuleCreateResponse response = reviewResource.createRule(
                "123234280394820",
                "98765",
                "123123",
                "测试规则"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("11111", response.getRuleId());
    }

    @Test
    public void testCreateRule_InvalidWorkspaceId() {
        Map<String, Object> config = new HashMap<>();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule(
                    "",
                    "98765",
                    "123123",
                    "测试规则");
        });
    }

    @Test
    public void testCreateRule_InvalidGroupId() {
        Map<String, Object> config = new HashMap<>();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule(
                    "123234280394820",
                    "",
                    "123123",
                    "测试规则");
        });
    }

    @Test
    public void testCreateRule_InvalidName() {
        Map<String, Object> config = new HashMap<>();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule("1239840129384", "98765", "", "测试规则");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule("1239840129384", "98765", null, "测试规则");
        });
    }

    @Test
    public void testCreateRule_InvalidRuleName() {
        Map<String, Object> config = new HashMap<>();

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule("1239840129384", "98765", "1235564", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.createRule("1239840129384", "98765", "1235564", null);
        });
    }
    // ==================== updateRule() 测试 ====================

    @Test
    public void testUpdateRule_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.updateRule("1239840129384", "11111", "新规则名称");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdateRule_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateRule("", "11111", "新规则名称");
        });
    }

    @Test
    public void testUpdateRule_InvalidRuleId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.updateRule("1239840129384", "", "新规则名称");
        });
    }

    // ==================== deleteRule() 测试 ====================

    @Test
    public void testDeleteRule_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.deleteRule("1239840129384", "11111");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDeleteRule_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteRule("", "11111");
        });
    }

    @Test
    public void testDeleteRule_InvalidRuleId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteRule("1239840129384", "");
        });
    }

    // ==================== submitTask() 测试 ====================

    @Test
    public void testSubmitTask_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("task_id", "task123");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewTaskSubmitResponse response = reviewResource.submitTask(
                "1239840129384",
                "审核任务1",
                "12345"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("task123", response.getTaskId());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testSubmitTask_WithAllParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("task_id", "task123");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewTaskSubmitResponse response = reviewResource.submitTask(
                "1239840129384",
                "审核任务1",
                "12345",
                Arrays.asList("11111", "22222"),
                "batch001"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("task123", response.getTaskId());
    }

    @Test
    public void testSubmitTask_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("", "审核任务1", "12345");
        });
    }

    @Test
    public void testSubmitTask_InvalidName() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", "", "12345");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", null, "12345");
        });
    }

    @Test
    public void testSubmitTask_NameTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", longName, "12345");
        });
    }

    @Test
    public void testSubmitTask_InvalidRepoId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", "审核任务1", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", "审核任务1", "abc");
        });
    }

    @Test
    public void testSubmitTask_InvalidExtractTaskId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.submitTask("1239840129384", "审核任务1", "12345",
                    Arrays.asList("abc"), null);
        });
    }

    // ==================== deleteTask() 测试 ====================

    @Test
    public void testDeleteTask_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.deleteTask("1239840129384", Arrays.asList("12345", "67890"));
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDeleteTask_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteTask("", Arrays.asList("12345"));
        });
    }

    @Test
    public void testDeleteTask_EmptyTaskIds() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteTask("1239840129384", new ArrayList<>());
        });
    }

    @Test
    public void testDeleteTask_NullTaskIds() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteTask("1239840129384", null);
        });
    }

    @Test
    public void testDeleteTask_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.deleteTask("1239840129384", Arrays.asList("abc"));
        });
    }

    // ==================== getTaskResult() 测试 ====================

    @Test
    public void testGetTaskResult_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("task_id", "task123");
        resultNode.put("task_name", "审核任务1");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewTaskResultResponse response = reviewResource.getTaskResult(
                "1239840129384",
                "12345"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("task123", response.getTaskId());
        assertEquals("审核任务1", response.getTaskName());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testGetTaskResult_WithTaskDetailUrl() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("task_id", "task123");
        resultNode.put("task_detail_url", "https://example.com/task/123");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        ReviewTaskResultResponse response = reviewResource.getTaskResult(
                "1239840129384",
                "12345",
                true
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("https://example.com/task/123", response.getTaskDetailUrl());
    }

    @Test
    public void testGetTaskResult_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.getTaskResult("", "12345");
        });
    }

    @Test
    public void testGetTaskResult_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.getTaskResult("1239840129384", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.getTaskResult("1239840129384", "abc");
        });
    }

    // ==================== retryTask() 测试 ====================

    @Test
    public void testRetryTask_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.retryTask("1239840129384", "12345");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testRetryTask_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTask("", "12345");
        });
    }

    @Test
    public void testRetryTask_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTask("1239840129384", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTask("1239840129384", "abc");
        });
    }

    // ==================== retryTaskRule() 测试 ====================

    @Test
    public void testRetryTaskRule_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            reviewResource.retryTaskRule("1239840129384", "12345", "11111");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testRetryTaskRule_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTaskRule("", "12345", "11111");
        });
    }

    @Test
    public void testRetryTaskRule_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTaskRule("1239840129384", "", "11111");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTaskRule("1239840129384", "abc", "11111");
        });
    }

    @Test
    public void testRetryTaskRule_InvalidRuleId() {
        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTaskRule("1239840129384", "12345", "");
        });

        assertThrows(ValidationException.class, () -> {
            reviewResource.retryTaskRule("1239840129384", "12345", "abc");
        });
    }
}
