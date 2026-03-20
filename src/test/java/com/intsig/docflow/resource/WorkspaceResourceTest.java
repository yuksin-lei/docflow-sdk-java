package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intsig.docflow.enums.AuthScope;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.model.workspace.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * WorkspaceResource 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class WorkspaceResourceTest {

    private HttpClient httpClient;
    private WorkspaceResource workspaceResource;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        httpClient = mock(HttpClient.class);
        workspaceResource = new WorkspaceResource(httpClient);
        objectMapper = new ObjectMapper();
    }

    // ==================== create() 测试 ====================

    @Test
    public void testCreate_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备请求
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("测试工作空间")
                .description("这是一个测试工作空间")
                .enterpriseId(12345L)
                .authScope((Integer) 1)
                .build();

        // 执行测试
        WorkspaceCreateResponse response = workspaceResource.create(request);

        // 验证结果
        assertNotNull(response);
        assertEquals("1234567890", response.getWorkspaceId());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testCreate_InvalidName_Empty() {
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("")
                .enterpriseId(12345L)
                .authScope(AuthScope.PUBLIC)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_InvalidName_Null() {
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name(null)
                .enterpriseId(12345L)
                .authScope((Integer) 1)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_InvalidName_TooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 51; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name(longName)
                .enterpriseId(12345L)
                .authScope((Integer) 1)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_InvalidEnterpriseId_Null() {
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("测试工作空间")
                .enterpriseId(null)
                .authScope((Integer) 1)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_InvalidAuthScope_Null() {
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("测试工作空间")
                .enterpriseId(12345L)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_InvalidAuthScope_OutOfRange() {
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("测试工作空间")
                .enterpriseId(12345L)
                .authScope(2)
                .build();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });

        request.setAuthScope(-1);
        assertThrows(ValidationException.class, () -> {
            workspaceResource.create(request);
        });
    }

    // ==================== list() 测试 ====================

    @Test
    public void testList_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 10);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        ArrayNode workspacesNode = objectMapper.createArrayNode();
        ObjectNode workspace1 = objectMapper.createObjectNode();
        workspace1.put("workspace_id", "1234567890");
        workspace1.put("name", "测试工作空间1");
        workspacesNode.add(workspace1);
        resultNode.set("workspaces", workspacesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        WorkspaceListResponse response = workspaceResource.list(12345L, 1, 20);

        // 验证结果
        assertNotNull(response);
        assertEquals(10, response.getTotal());
        assertEquals(1, response.getPage());
        assertEquals(20, response.getPageSize());
        assertEquals(1, response.getWorkspaces().size());
        assertEquals("1234567890", response.getWorkspaces().get(0).getId());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testList_WithDefaultPagination() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（使用默认分页参数）
        WorkspaceListResponse response = workspaceResource.list(12345L, null, null);

        // 验证结果
        assertNotNull(response);
        assertEquals(5, response.getTotal());
    }

    @Test
    public void testList_InvalidEnterpriseId_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.list(null, 1, 20);
        });
    }

    // ==================== get() 测试 ====================

    @Test
    public void testGet_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        resultNode.put("name", "测试工作空间");
        resultNode.put("description", "这是一个测试工作空间");
        resultNode.put("auth_scope", 1);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        WorkspaceInfo response = workspaceResource.get("1234567890");

        // 验证结果
        assertNotNull(response);
        assertEquals("1234567890", response.getId());
        assertEquals("测试工作空间", response.getName());
        assertEquals("这是一个测试工作空间", response.getDescription());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testGet_InvalidWorkspaceId_Empty() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.get("");
        });
    }

    @Test
    public void testGet_InvalidWorkspaceId_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.get(null);
        });
    }

    @Test
    public void testGet_InvalidWorkspaceId_NonNumeric() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.get("abc123");
        });
    }

    @Test
    public void testGet_InvalidWorkspaceId_TooLong() {
        String tooLongId = "12345678901234567890"; // 20位，超过19位

        assertThrows(ValidationException.class, () -> {
            workspaceResource.get(tooLongId);
        });
    }

    // ==================== delete() 测试 ====================

    @Test
    public void testDelete_Success_MultipleIds() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        List<String> workspaceIds = Arrays.asList("1234567890", "0987654321");
        assertDoesNotThrow(() -> {
            workspaceResource.delete(workspaceIds);
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDelete_Success_SingleId() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            workspaceResource.delete("1234567890");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDelete_InvalidWorkspaceIds_Empty() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.delete(Arrays.asList());
        });
    }

    @Test
    public void testDelete_InvalidWorkspaceIds_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.delete((List<String>) null);
        });
    }

    @Test
    public void testDelete_InvalidWorkspaceId_EmptyString() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.delete(Arrays.asList("1234567890", ""));
        });
    }

    @Test
    public void testDelete_InvalidWorkspaceId_NonNumeric() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.delete(Arrays.asList("1234567890", "abc"));
        });
    }

    @Test
    public void testDelete_InvalidWorkspaceId_TooLong() {
        String tooLongId = "12345678901234567890"; // 20位

        assertThrows(ValidationException.class, () -> {
            workspaceResource.delete(Arrays.asList(tooLongId));
        });
    }

    // ==================== 边界测试 ====================

    @Test
    public void testCreate_NameWithExactly50Characters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备50个字符的名称
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("a");
        }
        String name50 = sb.toString();

        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name(name50)
                .enterpriseId(12345L)
                .authScope((Integer) 1)
                .build();

        // 执行测试（应该成功）
        assertDoesNotThrow(() -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testGet_WorkspaceIdWith19Characters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890123456789");
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（19位，应该成功）
        WorkspaceInfo info = workspaceResource.get("1234567890123456789");

        // 验证结果
        assertNotNull(info);
        assertEquals("1234567890123456789", info.getId());
    }

    @Test
    public void testCreate_WithMinimalRequiredFields() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 只设置必需字段，不设置可选的 description
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("最小工作空间")
                .enterpriseId(12345L)
                .authScope((Integer) 0)
                .build();

        // 执行测试
        WorkspaceCreateResponse response = workspaceResource.create(request);

        // 验证结果
        assertNotNull(response);
        assertEquals("1234567890", response.getWorkspaceId());
    }

    @Test
    public void testCreate_AuthScope_Private() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // authScope = 0 (私有)
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("私有工作空间")
                .enterpriseId(12345L)
                .authScope((Integer) 0)
                .build();

        // 执行测试
        assertDoesNotThrow(() -> {
            workspaceResource.create(request);
        });
    }

    @Test
    public void testCreate_AuthScope_Public() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("workspace_id", "1234567890");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // authScope = 1 (公共)
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("公共工作空间")
                .enterpriseId(12345L)
                .authScope((Integer) 1)
                .build();

        // 执行测试
        assertDoesNotThrow(() -> {
            workspaceResource.create(request);
        });
    }

    // ==================== update() 测试 ====================

    @Test
    public void testUpdate_Success_FullParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试（包含所有可选参数）
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "更新后的工作空间",
                    1,
                    "更新后的描述",
                    "https://example.com/callback",
                    3
            );
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_Success_BasicParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试（使用简化版方法）
        assertDoesNotThrow(() -> {
            workspaceResource.update("1234567890", "新名称", 0);
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_Success_WithDescription() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试（只带描述）
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间名称",
                    1,
                    "这是更新的描述",
                    null,
                    null
            );
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_Success_WithCallbackUrl() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试（只带回调URL）
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间名称",
                    0,
                    null,
                    "https://example.com/callback",
                    null
            );
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_Success_WithCallbackRetryTime() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试（callbackRetryTime = 1, 边界值）
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间名称",
                    1,
                    null,
                    null,
                    1
            );
        });

        // 执行测试（callbackRetryTime = 3, 边界值）
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间名称",
                    1,
                    null,
                    null,
                    3
            );
        });

        // 验证调用
        verify(httpClient, times(2)).post(anyString(), any());
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update(null, "新名称", 1);
        });
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_Empty() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("", "新名称", 1);
        });
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_NonNumeric() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("abc123", "新名称", 1);
        });
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_TooLong() {
        String tooLongId = "12345678901234567890"; // 20位

        assertThrows(ValidationException.class, () -> {
            workspaceResource.update(tooLongId, "新名称", 1);
        });
    }

    @Test
    public void testUpdate_InvalidName_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", null, 1);
        });
    }

    @Test
    public void testUpdate_InvalidName_Empty() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", "", 1);
        });
    }

    @Test
    public void testUpdate_InvalidName_TooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 51; i++) {
            sb.append("a");
        }
        String longName = sb.toString();

        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", longName, 1);
        });
    }

    @Test
    public void testUpdate_ValidName_Exactly50Characters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备50个字符的名称
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("a");
        }
        String name50 = sb.toString();

        // 执行测试（应该成功）
        assertDoesNotThrow(() -> {
            workspaceResource.update("1234567890", name50, 1);
        });
    }

    @Test
    public void testUpdate_InvalidAuthScope_Null() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", "新名称", null);
        });
    }

    @Test
    public void testUpdate_InvalidAuthScope_OutOfRange() {
        // authScope = 2（超出范围）
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", "新名称", 2);
        });

        // authScope = -1（超出范围）
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update("1234567890", "新名称", -1);
        });
    }

    @Test
    public void testUpdate_ValidAuthScope_Private() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // authScope = 0 (私有)
        assertDoesNotThrow(() -> {
            workspaceResource.update("1234567890", "私有工作空间", 0);
        });
    }

    @Test
    public void testUpdate_ValidAuthScope_Public() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // authScope = 1 (公共)
        assertDoesNotThrow(() -> {
            workspaceResource.update("1234567890", "公共工作空间", 1);
        });
    }

    @Test
    public void testUpdate_InvalidCallbackRetryTime_Zero() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间",
                    1,
                    null,
                    null,
                    0  // 小于1
            );
        });
    }

    @Test
    public void testUpdate_InvalidCallbackRetryTime_TooLarge() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间",
                    1,
                    null,
                    null,
                    4  // 大于3
            );
        });
    }

    @Test
    public void testUpdate_InvalidCallbackRetryTime_Negative() {
        assertThrows(ValidationException.class, () -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间",
                    1,
                    null,
                    null,
                    -1  // 负数
            );
        });
    }

    @Test
    public void testUpdate_NullCallbackRetryTime_ShouldPass() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // callbackRetryTime 为 null 应该通过验证
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间",
                    1,
                    null,
                    null,
                    null
            );
        });
    }

    @Test
    public void testUpdate_AllOptionalParametersNull() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 所有可选参数都为 null
        assertDoesNotThrow(() -> {
            workspaceResource.update(
                    "1234567890",
                    "工作空间",
                    0,
                    null,
                    null,
                    null
            );
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }
}
