package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.enums.ExtractModel;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.model.category.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * CategoryResource 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class CategoryResourceTest {

    private HttpClient httpClient;
    private CategoryResource categoryResource;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        httpClient = mock(HttpClient.class);
        categoryResource = new CategoryResource(httpClient);
        objectMapper = new ObjectMapper();
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
        ArrayNode categoriesNode = objectMapper.createArrayNode();
        ObjectNode category1 = objectMapper.createObjectNode();
        category1.put("id", "cat123");
        category1.put("name", "发票");
        categoriesNode.add(category1);
        resultNode.set("categories", categoriesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                "1"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(10, response.getTotal());
        assertEquals(1, response.getPage());
        assertEquals(20, response.getPageSize());
        assertEquals(1, response.getCategories().size());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testList_WithDefaultParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        ArrayNode categoriesNode = objectMapper.createArrayNode();
        resultNode.set("categories", categoriesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（使用默认参数）
        CategoryListResponse response = categoryResource.list("1234567890");

        // 验证结果
        assertNotNull(response);
        assertEquals(5, response.getTotal());
        assertEquals(1, response.getPage());
        assertEquals(20, response.getPageSize());
    }

    @Test
    public void testList_WithEnabledStatusEnum() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 3);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（使用枚举）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                EnabledStatus.ENABLED
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(3, response.getTotal());
    }

    @Test
    public void testList_EnabledStatus_Disabled() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 2);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（禁用状态）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                EnabledStatus.DISABLED
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(2, response.getTotal());
    }

    @Test
    public void testList_EnabledStatus_Other() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 1);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（其他状态，如草稿）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                EnabledStatus.DRAFT
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.getTotal());
    }

    @Test
    public void testList_EnabledStatus_All() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 15);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（所有状态）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                EnabledStatus.ALL
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(15, response.getTotal());
    }

    @Test
    public void testList_WithStringEnabledStatus() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 8);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（使用字符串"0"表示禁用）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                "0"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(8, response.getTotal());
    }

    @Test
    public void testList_WithStringEnabledStatus_All() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 20);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（使用字符串"all"）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                "all"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(20, response.getTotal());
    }

    @Test
    public void testList_WithPagination() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 100);
        resultNode.put("page", 3);
        resultNode.put("page_size", 50);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（自定义分页参数）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                3,
                50,
                (String) null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(100, response.getTotal());
        assertEquals(3, response.getPage());
        assertEquals(50, response.getPageSize());
    }

    // ==================== 参数校验测试 ====================

    @Test
    public void testList_InvalidWorkspaceId_Empty() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.list("", null, null, (String) null);
        });
    }

    @Test
    public void testList_InvalidWorkspaceId_Null() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.list(null, null, null, (String) null);
        });
    }

    @Test
    public void testList_InvalidWorkspaceId_NonNumeric() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.list("abc123", null, null, (String) null);
        });
    }

    @Test
    public void testList_InvalidWorkspaceId_TooLong() {
        String tooLongId = "12345678901234567890"; // 20位，超过19位

        assertThrows(ValidationException.class, () -> {
            categoryResource.list(tooLongId, null, null, (String) null);
        });
    }

    @Test
    public void testList_InvalidWorkspaceId_SpecialCharacters() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.list("123-456", null, null, (String) null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.list("123.456", null, null, (String) null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.list("123 456", null, null, (String) null);
        });
    }

    // ==================== 边界测试 ====================

    @Test
    public void testList_WorkspaceIdWith19Characters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（19位ID，应该成功）
        assertDoesNotThrow(() -> {
            categoryResource.list("1234567890123456789", null, null, (String) null);
        });
    }

    @Test
    public void testList_WithNullEnabledStatus() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 10);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（enabled 为 null，应使用默认值"1"）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                null,
                null,
                (String) null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(10, response.getTotal());
    }

    @Test
    public void testList_MinPage() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 100);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（第1页）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                (String) null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.getPage());
    }

    @Test
    public void testList_MaxPageSize() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 100);
        resultNode.put("page", 1);
        resultNode.put("page_size", 100);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（最大页面大小）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                100,
                (String) null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(100, response.getPageSize());
    }

    @Test
    public void testList_EmptyResult() {
        // 模拟响应（空列表）
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 0);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        ArrayNode categoriesNode = objectMapper.createArrayNode();
        resultNode.set("categories", categoriesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        CategoryListResponse response = categoryResource.list("1234567890");

        // 验证结果
        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertNotNull(response.getCategories());
        assertTrue(response.getCategories().isEmpty());
    }

    // ==================== 测试不同的方法重载 ====================

    @Test
    public void testList_ThreeParameterOverload_WithEnum() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 7);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（三参数重载，使用枚举）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                2,
                10,
                EnabledStatus.ENABLED
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(7, response.getTotal());
    }

    @Test
    public void testList_ThreeParameterOverload_WithString() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 6);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（三参数重载，使用字符串）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                2,
                10,
                "1"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(6, response.getTotal());
    }

    @Test
    public void testList_SingleParameterOverload() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 12);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（单参数重载）
        CategoryListResponse response = categoryResource.list("1234567890");

        // 验证结果
        assertNotNull(response);
        assertEquals(12, response.getTotal());
    }

    @Test
    public void testList_WithNullEnabledStatusEnum() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 9);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试（EnabledStatus 为 null）
        CategoryListResponse response = categoryResource.list(
                "1234567890",
                1,
                20,
                (EnabledStatus) null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(9, response.getTotal());
    }

    // ==================== create() 测试 ====================

    @Test
    public void testCreate_Success(@TempDir Path tempDir) throws IOException {
        // 创建测试文件
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test content".getBytes());

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("category_id", "cat123");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        // 创建字段配置
        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("发票号码");

        // 执行测试
        CategoryCreateResponse response = categoryResource.create(
                "1234567890",
                "发票类别",
                "这是发票分类提示词",
                ExtractModel.MODEL_1,
                Collections.singletonList(sampleFile),
                Collections.singletonList(field)
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("cat123", response.getCategoryId());
        verify(httpClient, times(1)).postMultipart(anyString(), any());
    }

    @Test
    public void testCreate_WithMultipleSampleFiles(@TempDir Path tempDir) throws IOException {
        // 创建多个测试文件
        File file1 = tempDir.resolve("sample1.pdf").toFile();
        File file2 = tempDir.resolve("sample2.pdf").toFile();
        Files.write(file1.toPath(), "content1".getBytes());
        Files.write(file2.toPath(), "content2".getBytes());

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("category_id", "cat456");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        // 创建字段配置
        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("金额");

        // 执行测试
        CategoryCreateResponse response = categoryResource.create(
                "1234567890",
                "合同类别",
                null,
                ExtractModel.MODEL_2,
                Arrays.asList(file1, file2),
                Collections.singletonList(field)
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("cat456", response.getCategoryId());
    }

    @Test
    public void testCreate_WithMultipleFields(@TempDir Path tempDir) throws IOException {
        // 创建测试文件
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("category_id", "cat789");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        // 创建多个字段配置
        CategoryFieldConfig field1 = new CategoryFieldConfig();
        field1.setName("发票号码");
        field1.setIdentity("invoice_number");
        field1.setAlias(Arrays.asList("发票编号", "编号"));

        CategoryFieldConfig field2 = new CategoryFieldConfig();
        field2.setName("发票金额");
        field2.setMultiValue(false);
        field2.setIdentity("invoice_amount");

        CategoryFieldConfig field3 = new CategoryFieldConfig();
        field3.setName("发票日期");
        field3.setIdentity("invoice_date");
        // 添加日期转换配置
        TransformSettings.DatetimeSettings datetimeSettings = TransformSettings.DatetimeSettings.builder()
                .format("yyyy-MM-dd")
                .build();
        TransformSettings transformSettings = TransformSettings.builder()
                .type("datetime")
                .datetimeSettings(datetimeSettings)
                .build();
        field3.setTransformSettings(transformSettings);

        // 执行测试
        CategoryCreateResponse response = categoryResource.create(
                "1234567890",
                "发票",
                "提取发票关键信息",
                ExtractModel.MODEL_1,
                Collections.singletonList(sampleFile),
                Arrays.asList(field1, field2, field3)
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("cat789", response.getCategoryId());
    }

    @Test
    public void testCreate_WithoutCategoryPrompt(@TempDir Path tempDir) throws IOException {
        // 创建测试文件
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("category_id", "cat999");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段名");

        // 执行测试（不传分类提示词）
        CategoryCreateResponse response = categoryResource.create(
                "1234567890",
                "测试类别",
                null,
                ExtractModel.MODEL_2,
                Collections.singletonList(sampleFile),
                Collections.singletonList(field)
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("cat999", response.getCategoryId());
    }

    @Test
    public void testCreate_InvalidWorkspaceId_Null(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    null,
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidWorkspaceId_Empty(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "",
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidName_Null(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    null,
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidName_Empty(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "   ",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidExtractModel_Null(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "类别名",
                    null,
                    null,
                    Collections.singletonList(sampleFile),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidSampleFiles_Null(@TempDir Path tempDir) {
        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    null,
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidSampleFiles_Empty(@TempDir Path tempDir) {
        CategoryFieldConfig field = new CategoryFieldConfig();
        field.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.emptyList(),
                    Collections.singletonList(field)
            );
        });
    }

    @Test
    public void testCreate_InvalidFields_Null(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    null
            );
        });
    }

    @Test
    public void testCreate_InvalidFields_Empty(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        assertThrows(ValidationException.class, () -> {
            categoryResource.create(
                    "1234567890",
                    "类别名",
                    null,
                    ExtractModel.MODEL_1,
                    Collections.singletonList(sampleFile),
                    Collections.emptyList()
            );
        });
    }

    // ==================== update() 测试 ====================

    @Test
    public void testUpdate_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            categoryResource.update(
                    "1234567890",
                    "cat123",
                    "新类别名称",
                    "新提示词",
                    EnabledStatus.ENABLED
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdate_OnlyName() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.update(
                    "1234567890",
                    "cat123",
                    "仅更新名称",
                    null,
                    null
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdate_OnlyCategoryPrompt() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.update(
                    "1234567890",
                    "cat123",
                    null,
                    "仅更新提示词",
                    null
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdate_OnlyEnabled() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.update(
                    "1234567890",
                    "cat123",
                    null,
                    null,
                    EnabledStatus.DISABLED
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdate_AllFields() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.update(
                    "1234567890",
                    "cat123",
                    "完整更新",
                    "完整提示词",
                    EnabledStatus.ENABLED
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_Null() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.update(
                    null,
                    "cat123",
                    "名称",
                    null,
                    null
            );
        });
    }

    @Test
    public void testUpdate_InvalidWorkspaceId_Empty() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.update(
                    "",
                    "cat123",
                    "名称",
                    null,
                    null
            );
        });
    }

    @Test
    public void testUpdate_InvalidCategoryId_Null() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.update(
                    "1234567890",
                    null,
                    "名称",
                    null,
                    null
            );
        });
    }

    @Test
    public void testUpdate_InvalidCategoryId_Empty() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.update(
                    "1234567890",
                    "",
                    "名称",
                    null,
                    null
            );
        });
    }

    // ==================== delete() 测试 ====================

    @Test
    public void testDelete_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.delete(
                    "1234567890",
                    Arrays.asList("cat123", "cat456")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDelete_SingleCategory() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.delete(
                    "1234567890",
                    Collections.singletonList("cat123")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDelete_MultipleCategories() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.delete(
                    "1234567890",
                    Arrays.asList("cat1", "cat2", "cat3", "cat4", "cat5")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDelete_InvalidWorkspaceId_Null() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.delete(
                    null,
                    Collections.singletonList("cat123")
            );
        });
    }

    @Test
    public void testDelete_InvalidWorkspaceId_Empty() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.delete(
                    "",
                    Collections.singletonList("cat123")
            );
        });
    }

    @Test
    public void testDelete_InvalidCategoryIds_Null() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.delete(
                    "1234567890",
                    null
            );
        });
    }

    @Test
    public void testDelete_InvalidCategoryIds_Empty() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.delete(
                    "1234567890",
                    Collections.emptyList()
            );
        });
    }

    // ==================== listTables() 测试 ====================

    @Test
    public void testListTables_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        ArrayNode tablesNode = objectMapper.createArrayNode();

        ObjectNode table1 = objectMapper.createObjectNode();
        table1.put("id", "table1");
        table1.put("name", "表格1");
        tablesNode.add(table1);

        resultNode.set("tables", tablesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryTablesListResponse response = categoryResource.listTables(
                "1234567890",
                "cat123"
        );

        assertNotNull(response);
        assertNotNull(response.getTables());
        assertEquals(1, response.getTables().size());
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testListTables_EmptyResult() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        ArrayNode tablesNode = objectMapper.createArrayNode();
        resultNode.set("tables", tablesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryTablesListResponse response = categoryResource.listTables(
                "1234567890",
                "cat123"
        );

        assertNotNull(response);
        assertNotNull(response.getTables());
        assertTrue(response.getTables().isEmpty());
    }

    @Test
    public void testListTables_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listTables(null, "cat123");
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listTables("", "cat123");
        });
    }

    @Test
    public void testListTables_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listTables("1234567890", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listTables("1234567890", "");
        });
    }

    // ==================== addTable() 测试 ====================

    @Test
    public void testAddTable_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("table_id", "table123");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        String tableId = categoryResource.addTable(
                "1234567890",
                "cat123",
                "明细表",
                "提取明细数据",
                false
        );

        assertNotNull(tableId);
        assertEquals("table123", tableId);
        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testAddTable_WithMultiTableCollection() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("table_id", "table456");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        String tableId = categoryResource.addTable(
                "1234567890",
                "cat123",
                "汇总表",
                null,
                true
        );

        assertEquals("table456", tableId);
    }

    @Test
    public void testAddTable_WithoutOptionalParams() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("table_id", "table789");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        String tableId = categoryResource.addTable(
                "1234567890",
                "cat123",
                "简单表",
                null,
                null
        );

        assertEquals("table789", tableId);
    }

    @Test
    public void testAddTable_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable(null, "cat123", "表名", null, null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable("", "cat123", "表名", null, null);
        });
    }

    @Test
    public void testAddTable_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable("1234567890", null, "表名", null, null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable("1234567890", "", "表名", null, null);
        });
    }

    @Test
    public void testAddTable_InvalidName() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable("1234567890", "cat123", null, null, null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.addTable("1234567890", "cat123", "   ", null, null);
        });
    }

    // ==================== updateTable() 测试 ====================

    @Test
    public void testUpdateTable_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.updateTable(
                    "1234567890",
                    "cat123",
                    "table123",
                    "更新表名",
                    "更新提示词",
                    true
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateTable_OnlyRequiredFields() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.updateTable(
                    "1234567890",
                    "cat123",
                    "table123",
                    null,
                    null,
                    false
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateTable_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable(null, "cat123", "table123", null, null, false);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("", "cat123", "table123", null, null, false);
        });
    }

    @Test
    public void testUpdateTable_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("1234567890", null, "table123", null, null, false);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("1234567890", "", "table123", null, null, false);
        });
    }

    @Test
    public void testUpdateTable_InvalidTableId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("1234567890", "cat123", null, null, null, false);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("1234567890", "cat123", "   ", null, null, false);
        });
    }

    @Test
    public void testUpdateTable_InvalidCollectFromMultiTable() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.updateTable("1234567890", "cat123", "table123", null, null, null);
        });
    }

    // ==================== deleteTables() 测试 ====================

    @Test
    public void testDeleteTables_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteTables(
                    "1234567890",
                    "cat123",
                    Arrays.asList("table1", "table2")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteTables_SingleTable() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteTables(
                    "1234567890",
                    "cat123",
                    Collections.singletonList("table1")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteTables_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables(null, "cat123", Collections.singletonList("table1"));
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables("", "cat123", Collections.singletonList("table1"));
        });
    }

    @Test
    public void testDeleteTables_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables("1234567890", null, Collections.singletonList("table1"));
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables("1234567890", "", Collections.singletonList("table1"));
        });
    }

    @Test
    public void testDeleteTables_InvalidTableIds() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables("1234567890", "cat123", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteTables("1234567890", "cat123", Collections.emptyList());
        });
    }

    // ==================== listFields() 测试 ====================

    @Test
    public void testListFields_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        ArrayNode fieldsNode = objectMapper.createArrayNode();

        ObjectNode field1 = objectMapper.createObjectNode();
        field1.put("id", "field1");
        field1.put("name", "字段1");
        fieldsNode.add(field1);

        resultNode.set("fields", fieldsNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldsListResponse response = categoryResource.listFields(
                "1234567890",
                "123"
        );

        assertNotNull(response);
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testListFields_EmptyResult() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        ArrayNode fieldsNode = objectMapper.createArrayNode();
        resultNode.set("fields", fieldsNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldsListResponse response = categoryResource.listFields(
                "1234567890",
                "cat123"
        );

        assertNotNull(response);
    }

    @Test
    public void testListFields_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listFields(null, "cat123");
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listFields("", "cat123");
        });
    }

    @Test
    public void testListFields_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listFields("1234567890", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listFields("1234567890", "");
        });
    }

    // ==================== addField() 测试 ====================

    @Test
    public void testAddField_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("field_id", "field123");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("发票号码");
        config.setDescription("发票号码字段");
        config.setPrompt("提取发票上的号码");
        config.setUsePrompt(true);
        config.setIdentity("invoice_number");
        config.setMultiValue(false);

        String fieldId = categoryResource.addField(
                "1234567890",
                "cat123",
                null,
                config
        );

        assertNotNull(fieldId);
        assertEquals("field123", fieldId);
        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testAddField_WithTableId() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("field_id", "field456");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("商品名称");
        config.setDescription("商品的名称");
        config.setPrompt("提取商品名称");

        String fieldId = categoryResource.addField(
                "1234567890",
                "cat123",
                "table123",
                config
        );

        assertEquals("field456", fieldId);
    }

    @Test
    public void testAddField_MinimalConfig() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("field_id", "field789");
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段名");

        String fieldId = categoryResource.addField(
                "1234567890",
                "cat123",
                null,
                config
        );

        assertEquals("field789", fieldId);
    }

    @Test
    public void testAddField_InvalidWorkspaceId() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.addField(null, "cat123", null, config);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.addField("", "cat123", null, config);
        });
    }

    @Test
    public void testAddField_InvalidCategoryId() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.addField("1234567890", null, null, config);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.addField("1234567890", "", null, config);
        });
    }

    @Test
    public void testAddField_InvalidFieldConfig() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.addField("1234567890", "cat123", null, null);
        });

        CategoryFieldConfig config = new CategoryFieldConfig();
        assertThrows(ValidationException.class, () -> {
            categoryResource.addField("1234567890", "cat123", null, config);
        });
    }

    // ==================== updateField() 测试 ====================

    @Test
    public void testUpdateField_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("更新字段名");
        config.setDescription("更新描述");

        assertDoesNotThrow(() -> {
            categoryResource.updateField(
                    "1234567890",
                    "cat123",
                    "field123",
                    null,
                    config
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateField_WithTableId() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("表格字段");

        assertDoesNotThrow(() -> {
            categoryResource.updateField(
                    "1234567890",
                    "cat123",
                    "field123",
                    "table123",
                    config
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateField_PartialUpdate() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setPrompt("只更新提示词");

        assertDoesNotThrow(() -> {
            categoryResource.updateField(
                    "1234567890",
                    "cat123",
                    "field123",
                    null,
                    config
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateField_NullConfig() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.updateField(
                    "1234567890",
                    "cat123",
                    "field123",
                    null,
                    null
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testUpdateField_InvalidWorkspaceId() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField(null, "cat123", "field123", null, config);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField("", "cat123", "field123", null, config);
        });
    }

    @Test
    public void testUpdateField_InvalidCategoryId() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField("1234567890", null, "field123", null, config);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField("1234567890", "", "field123", null, config);
        });
    }

    @Test
    public void testUpdateField_InvalidFieldId() {
        CategoryFieldConfig config = new CategoryFieldConfig();
        config.setName("字段");

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField("1234567890", "cat123", null, null, config);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.updateField("1234567890", "cat123", "   ", null, config);
        });
    }

    // ==================== deleteFields() 测试 ====================

    @Test
    public void testDeleteFields_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteFields(
                    "1234567890",
                    "cat123",
                    Arrays.asList("field1", "field2"),
                    null
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteFields_WithTableId() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteFields(
                    "1234567890",
                    "cat123",
                    Collections.singletonList("field1"),
                    "table123"
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteFields_SingleField() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteFields(
                    "1234567890",
                    "cat123",
                    Collections.singletonList("field1"),
                    null
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteFields_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields(null, "cat123", Collections.singletonList("field1"), null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields("", "cat123", Collections.singletonList("field1"), null);
        });
    }

    @Test
    public void testDeleteFields_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields("1234567890", null, Collections.singletonList("field1"), null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields("1234567890", "", Collections.singletonList("field1"), null);
        });
    }

    @Test
    public void testDeleteFields_InvalidFieldIds() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields("1234567890", "cat123", null, null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteFields("1234567890", "cat123", Collections.emptyList(), null);
        });
    }

    // ==================== uploadSample() 测试 ====================

    @Test
    public void testUploadSample_Success(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test content".getBytes());

        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("sample_id", "sample123");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        String sampleId = categoryResource.uploadSample(
                "1234567890",
                "cat123",
                sampleFile
        );

        assertNotNull(sampleId);
        assertEquals("sample123", sampleId);
        verify(httpClient, times(1)).postMultipart(anyString(), any());
    }

    @Test
    public void testUploadSample_DifferentFileType(@TempDir Path tempDir) throws IOException {
        File imageFile = tempDir.resolve("image.jpg").toFile();
        Files.write(imageFile.toPath(), new byte[]{1, 2, 3});

        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("sample_id", "sample456");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any()))
                .thenReturn(responseNode);

        String sampleId = categoryResource.uploadSample(
                "1234567890",
                "cat123",
                imageFile
        );

        assertEquals("sample456", sampleId);
    }

    @Test
    public void testUploadSample_InvalidWorkspaceId(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        assertThrows(ValidationException.class, () -> {
            categoryResource.uploadSample(null, "cat123", sampleFile);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.uploadSample("", "cat123", sampleFile);
        });
    }

    @Test
    public void testUploadSample_InvalidCategoryId(@TempDir Path tempDir) throws IOException {
        File sampleFile = tempDir.resolve("sample.pdf").toFile();
        Files.write(sampleFile.toPath(), "test".getBytes());

        assertThrows(ValidationException.class, () -> {
            categoryResource.uploadSample("1234567890", null, sampleFile);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.uploadSample("1234567890", "", sampleFile);
        });
    }

    @Test
    public void testUploadSample_InvalidFile_NotExists(@TempDir Path tempDir) {
        File nonExistentFile = tempDir.resolve("nonexistent.pdf").toFile();

        assertThrows(ValidationException.class, () -> {
            categoryResource.uploadSample("1234567890", "cat123", nonExistentFile);
        });
    }

    // ==================== listSamples() 测试 ====================

    @Test
    public void testListSamples_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        resultNode.put("page", 1);
        resultNode.put("page_size", 20);
        ArrayNode samplesNode = objectMapper.createArrayNode();

        ObjectNode sample1 = objectMapper.createObjectNode();
        sample1.put("sample_id", "sample1");
        sample1.put("file_name", "file1.pdf");
        samplesNode.add(sample1);

        resultNode.set("samples", samplesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategorySampleListResponse response = categoryResource.listSamples(
                "1234567890",
                "cat123",
                1,
                20
        );

        assertNotNull(response);
        assertEquals(5, response.getTotal());
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testListSamples_WithDefaultParams() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 3);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategorySampleListResponse response = categoryResource.listSamples(
                "1234567890",
                "cat123"
        );

        assertNotNull(response);
        assertEquals(3, response.getTotal());
    }

    @Test
    public void testListSamples_EmptyResult() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 0);
        ArrayNode samplesNode = objectMapper.createArrayNode();
        resultNode.set("samples", samplesNode);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategorySampleListResponse response = categoryResource.listSamples(
                "1234567890",
                "cat123"
        );

        assertNotNull(response);
        assertEquals(0, response.getTotal());
    }

    @Test
    public void testListSamples_WithCustomPagination() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 100);
        resultNode.put("page", 3);
        resultNode.put("page_size", 50);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        CategorySampleListResponse response = categoryResource.listSamples(
                "1234567890",
                "cat123",
                3,
                50
        );

        assertNotNull(response);
        assertEquals(100, response.getTotal());
        assertEquals(3, response.getPage());
        assertEquals(50, response.getPageSize());
    }

    @Test
    public void testListSamples_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listSamples(null, "cat123");
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listSamples("", "cat123");
        });
    }

    @Test
    public void testListSamples_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.listSamples("1234567890", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.listSamples("1234567890", "");
        });
    }

    // ==================== downloadSample() 测试 ====================

    @Test
    public void testDownloadSample_Success() {
        HttpClient.DownloadResult downloadResult = new HttpClient.DownloadResult(
                new byte[]{1, 2, 3},
                "sample.pdf"
        );

        when(httpClient.download(anyString(), anyMap()))
                .thenReturn(downloadResult);

        HttpClient.DownloadResult result = categoryResource.downloadSample(
                "1234567890",
                "cat123",
                "sample123"
        );

        assertNotNull(result);
        assertEquals("sample.pdf", result.getFileName());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getData());
        verify(httpClient, times(1)).download(anyString(), anyMap());
    }

    @Test
    public void testDownloadSample_DifferentFileType() {
        HttpClient.DownloadResult downloadResult = new HttpClient.DownloadResult(
                new byte[]{10, 20, 30},
                "image.jpg"
        );

        when(httpClient.download(anyString(), anyMap()))
                .thenReturn(downloadResult);

        HttpClient.DownloadResult result = categoryResource.downloadSample(
                "1234567890",
                "cat123",
                "sample456"
        );

        assertNotNull(result);
        assertEquals("image.jpg", result.getFileName());
    }

    @Test
    public void testDownloadSample_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample(null, "cat123", "sample123");
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample("", "cat123", "sample123");
        });
    }

    @Test
    public void testDownloadSample_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample("1234567890", null, "sample123");
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample("1234567890", "", "sample123");
        });
    }

    @Test
    public void testDownloadSample_InvalidSampleId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample("1234567890", "cat123", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.downloadSample("1234567890", "cat123", "   ");
        });
    }

    // ==================== deleteSamples() 测试 ====================

    @Test
    public void testDeleteSamples_Success() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteSamples(
                    "1234567890",
                    "cat123",
                    Arrays.asList("sample1", "sample2")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteSamples_SingleSample() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteSamples(
                    "1234567890",
                    "cat123",
                    Collections.singletonList("sample1")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteSamples_MultipleSamples() {
        ObjectNode responseNode = objectMapper.createObjectNode();
        when(httpClient.post(anyString(), anyMap()))
                .thenReturn(responseNode);

        assertDoesNotThrow(() -> {
            categoryResource.deleteSamples(
                    "1234567890",
                    "cat123",
                    Arrays.asList("s1", "s2", "s3", "s4", "s5")
            );
        });

        verify(httpClient, times(1)).post(anyString(), anyMap());
    }

    @Test
    public void testDeleteSamples_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples(null, "cat123", Collections.singletonList("sample1"));
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples("", "cat123", Collections.singletonList("sample1"));
        });
    }

    @Test
    public void testDeleteSamples_InvalidCategoryId() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples("1234567890", null, Collections.singletonList("sample1"));
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples("1234567890", "", Collections.singletonList("sample1"));
        });
    }

    @Test
    public void testDeleteSamples_InvalidSampleIds() {
        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples("1234567890", "cat123", null);
        });

        assertThrows(ValidationException.class, () -> {
            categoryResource.deleteSamples("1234567890", "cat123", Collections.emptyList());
        });
    }
}
