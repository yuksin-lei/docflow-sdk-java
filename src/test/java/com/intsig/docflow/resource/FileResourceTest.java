package com.intsig.docflow.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.model.file.*;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * FileResource 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class FileResourceTest {

    private HttpClient httpClient;
    private FileResource fileResource;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        httpClient = mock(HttpClient.class);
        fileResource = new FileResource(httpClient);
        objectMapper = new ObjectMapper();
    }

    // ==================== upload() 测试 ====================

    @Test
    public void testUpload_Success() throws IOException {
        // 准备测试文件
        File testFile = createTestFile("test.pdf");

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "20240101001");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any(RequestBody.class)))
                .thenReturn(responseNode);

        // 执行测试
        FileUploadResponse response = fileResource.upload(
                "workspace123",
                "invoice",
                testFile
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("20240101001", response.getBatchNumber());

        // 验证调用
        verify(httpClient, times(1)).postMultipart(anyString(), any(RequestBody.class));
    }

    @Test
    public void testUpload_WithAllParameters() throws IOException {
        // 准备测试文件
        File testFile = createTestFile("test.pdf");

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "20240101001");
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any(RequestBody.class)))
                .thenReturn(responseNode);

        // 执行测试
        FileUploadResponse response = fileResource.upload(
                "workspace123",
                "invoice",
                testFile,
                "batch001",
                true,
                true,
                false,
                "recognition",
                true,
                false,
                true,
                2,
                "mode1"
        );

        // 验证结果
        assertNotNull(response);
        assertEquals("20240101001", response.getBatchNumber());
    }

    @Test
    public void testUpload_InvalidWorkspaceId() throws IOException {
        File testFile = createTestFile("test.pdf");

        // 测试空工作空间ID
        assertThrows(ValidationException.class, () -> {
            fileResource.upload("", "invoice", testFile);
        });

        // 测试null工作空间ID
        assertThrows(ValidationException.class, () -> {
            fileResource.upload(null, "invoice", testFile);
        });
    }

    @Test
    public void testUpload_InvalidCategory() throws IOException {
        File testFile = createTestFile("test.pdf");

        // 测试空类别
        assertThrows(ValidationException.class, () -> {
            fileResource.upload("workspace123", "", testFile);
        });

        // 测试null类别
        assertThrows(ValidationException.class, () -> {
            fileResource.upload("workspace123", null, testFile);
        });
    }

    @Test
    public void testUpload_FileNotExists() {
        File nonExistentFile = new File("non_existent_file.pdf");

        // 测试文件不存在
        assertThrows(ValidationException.class, () -> {
            fileResource.upload("workspace123", "invoice", nonExistentFile);
        });
    }

    @Test
    public void testUpload_NullFile() {
        // 测试null文件
        assertThrows(ValidationException.class, () -> {
            fileResource.upload("workspace123", "invoice", null);
        });
    }

    // ==================== uploadSync() 测试 ====================

    @Test
    public void testUploadSync_Success() throws IOException {
        // 准备测试文件
        File testFile = createTestFile("test.pdf");

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 1);
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any(RequestBody.class)))
                .thenReturn(responseNode);

        // 执行测试
        FileFetchResponse response = fileResource.uploadSync(
                "workspace123",
                "invoice",
                testFile
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.getTotal());
    }

    @Test
    public void testUploadSync_WithTaskDetailUrl() throws IOException {
        // 准备测试文件
        File testFile = createTestFile("test.pdf");

        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 1);
        responseNode.set("result", resultNode);

        when(httpClient.postMultipart(anyString(), any(RequestBody.class)))
                .thenReturn(responseNode);

        // 执行测试
        FileFetchResponse response = fileResource.uploadSync(
                "workspace123",
                "invoice",
                testFile,
                "batch001",
                null, null, null, null,
                null, null, null, null, null,
                true
        );

        // 验证结果
        assertNotNull(response);
    }

    @Test
    public void testUploadSync_InvalidWorkspaceId() throws IOException {
        File testFile = createTestFile("test.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadSync("", "invoice", testFile);
        });
    }

    // ==================== fetch() 测试 ====================

    @Test
    public void testFetch_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 10);
        resultNode.put("page", 1);
        resultNode.put("page_size", 1000);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        FileFetchResponse response = fileResource.fetch("workspace123");

        // 验证结果
        assertNotNull(response);
        assertEquals(10, response.getTotal());
        assertEquals(1, response.getPage());
        assertEquals(1000, response.getPageSize());

        // 验证调用
        verify(httpClient, times(1)).get(anyString(), anyMap());
    }

    @Test
    public void testFetch_WithAllParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 5);
        responseNode.set("result", resultNode);

        when(httpClient.get(anyString(), anyMap()))
                .thenReturn(responseNode);

        // 执行测试
        FileFetchResponse response = fileResource.fetch(
                "workspace123",
                2,
                50,
                "batch001",
                "file123",
                "invoice",
                "success",
                2,
                "2024-01-01T00:00:00Z",
                "2024-01-31T23:59:59Z",
                true,
                true
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(5, response.getTotal());
    }

    @Test
    public void testFetch_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.fetch("", null, null, null, null, null,
                    null, null, null, null, null, null);
        });

        assertThrows(ValidationException.class, () -> {
            fileResource.fetch(null, null, null, null, null, null,
                    null, null, null, null, null, null);
        });
    }

    // ==================== update() 测试 ====================

    @Test
    public void testUpdate_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备更新数据
        List<KeyValue> fields = new ArrayList<>();
        fields.add(KeyValue.of("发票代码", "3100231130"));
        fields.add(KeyValue.of("发票号码", "28737000"));

        FileUpdateData data = FileUpdateData.builder()
                .fields(fields)
                .build();

        // 执行测试
        FileUpdateResponse response = fileResource.update(
                "1234567890",
                "9876543210",
                data
        );

        // 验证结果
        assertNotNull(response);

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_Success_WithTableData() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备字段数据
        List<KeyValue> fields = new ArrayList<>();
        fields.add(KeyValue.of("发票代码", "3100231130"));

        // 准备表格数据（二维数组）
        List<List<KeyValue>> items = new ArrayList<>();
        List<KeyValue> row1 = new ArrayList<>();
        row1.add(KeyValue.of("货物劳务名称", "计算机"));
        row1.add(KeyValue.of("规格型号", "DMS-SC68"));
        items.add(row1);

        List<KeyValue> row2 = new ArrayList<>();
        row2.add(KeyValue.of("货物劳务名称", "打印机"));
        row2.add(KeyValue.of("规格型号", "HP-1020"));
        items.add(row2);

        FileUpdateData data = FileUpdateData.builder()
                .fields(fields)
                .items(items)
                .build();

        // 执行测试
        FileUpdateResponse response = fileResource.update(
                "1234567890",
                "9876543210",
                data
        );

        // 验证结果
        assertNotNull(response);

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testUpdate_InvalidWorkspaceId() {
        FileUpdateData data = FileUpdateData.builder()
                .fields(new ArrayList<>())
                .build();

        assertThrows(ValidationException.class, () -> {
            fileResource.update("", "9876543210", data);
        });

        assertThrows(ValidationException.class, () -> {
            fileResource.update(null, "9876543210", data);
        });
    }

    @Test
    public void testUpdate_InvalidFileId() {
        FileUpdateData data = FileUpdateData.builder()
                .fields(new ArrayList<>())
                .build();

        assertThrows(ValidationException.class, () -> {
            fileResource.update("1234567890", "", data);
        });

        assertThrows(ValidationException.class, () -> {
            fileResource.update("1234567890", null, data);
        });
    }

    @Test
    public void testUpdate_NullData() {
        assertThrows(ValidationException.class, () -> {
            fileResource.update("1234567890", "9876543210", null);
        });
    }

    // ==================== batchUpdate() 测试 ====================

    @Test
    public void testBatchUpdate_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备更新列表
        List<FileUpdateRequest> updates = new ArrayList<>();

        // 第一个文件的更新
        FileUpdateData data1 = FileUpdateData.builder()
                .fields(Arrays.asList(KeyValue.of("发票代码", "3100231130")))
                .build();

        FileUpdateRequest update1 = FileUpdateRequest.builder()
                .workspaceId("1234567890")
                .fileId("9876543210")
                .data(data1)
                .build();
        updates.add(update1);

        // 第二个文件的更新
        FileUpdateData data2 = FileUpdateData.builder()
                .fields(Arrays.asList(KeyValue.of("发票代码", "3100231131")))
                .build();

        FileUpdateRequest update2 = FileUpdateRequest.builder()
                .workspaceId("1234567890")
                .fileId("9876543211")
                .data(data2)
                .build();
        updates.add(update2);

        // 执行测试
        FileUpdateResponse response = fileResource.batchUpdate(updates);

        // 验证结果
        assertNotNull(response);

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testBatchUpdate_InvalidWorkspaceId() {
        List<FileUpdateRequest> updates = new ArrayList<>();

        FileUpdateData data = FileUpdateData.builder()
                .fields(new ArrayList<>())
                .build();

        FileUpdateRequest update = FileUpdateRequest.builder()
                .workspaceId("")  // 空的 workspace_id
                .fileId("1234567890")
                .data(data)
                .build();
        updates.add(update);

        assertThrows(ValidationException.class, () -> {
            fileResource.batchUpdate(updates);
        });
    }

    @Test
    public void testBatchUpdate_InvalidFileId() {
        List<FileUpdateRequest> updates = new ArrayList<>();

        FileUpdateData data = FileUpdateData.builder()
                .fields(new ArrayList<>())
                .build();

        FileUpdateRequest update = FileUpdateRequest.builder()
                .workspaceId("1234567890")
                .fileId(null)  // null file_id
                .data(data)
                .build();
        updates.add(update);

        assertThrows(ValidationException.class, () -> {
            fileResource.batchUpdate(updates);
        });
    }

    @Test
    public void testBatchUpdate_NullData() {
        List<FileUpdateRequest> updates = new ArrayList<>();

        FileUpdateRequest update = FileUpdateRequest.builder()
                .workspaceId("1234567890")
                .fileId("9876543210")
                .data(null)  // null data
                .build();
        updates.add(update);

        assertThrows(ValidationException.class, () -> {
            fileResource.batchUpdate(updates);
        });
    }

    @Test
    public void testBatchUpdate_EmptyList() {
        assertThrows(ValidationException.class, () -> {
            fileResource.batchUpdate(new ArrayList<>());
        });
    }

    @Test
    public void testBatchUpdate_NullList() {
        assertThrows(ValidationException.class, () -> {
            fileResource.batchUpdate(null);
        });
    }

    // ==================== delete() 测试 ====================

    @Test
    public void testDelete_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("deleted_count", 5);
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        FileDeleteResponse response = fileResource.delete(
                "workspace123",
                Arrays.asList("batch001", "batch002"),
                null,
                null,
                null,
                null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(5, response.getDeletedCount());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testDelete_WithAllParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("deleted_count", 3);
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        FileDeleteResponse response = fileResource.delete(
                "workspace123",
                Arrays.asList("batch001"),
                Arrays.asList("task123"),
                Arrays.asList("file123"),
                1704067200L,
                1706745599L
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(3, response.getDeletedCount());
    }

    @Test
    public void testDelete_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.delete("", null, null, null, null, null);
        });

        assertThrows(ValidationException.class, () -> {
            fileResource.delete(null, null, null, null, null, null);
        });
    }

    // ==================== extractFields() 测试 ====================

    @Test
    public void testExtractFields_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 1);
        responseNode.set("result", resultNode);

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备字段列表
        List<ExtractFieldRequest> fields = new ArrayList<>();
        fields.add(ExtractFieldRequest.of("发票代码", "发票代码描述"));
        fields.add(ExtractFieldRequest.of("发票号码"));

        // 执行测试
        FileFetchResponse response = fileResource.extractFields(
                "workspace123",
                "task123",
                fields,
                null
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.getTotal());

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testExtractFields_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.extractFields("", "task123", null, null);
        });
    }

    @Test
    public void testExtractFields_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.extractFields("workspace123", "", null, null);
        });

        assertThrows(ValidationException.class, () -> {
            fileResource.extractFields("workspace123", null, null, null);
        });
    }

    // ==================== retry() 测试 ====================

    @Test
    public void testRetry_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.retry("workspace123", "task123");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testRetry_WithParserParams() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备解析参数
        DocumentParserParams parserParams = DocumentParserParams.builder()
                .removeWatermark(1)
                .formulaLevel(0)
                .applyMerge(1)
                .tableTextSplitMode(0)
                .cropDewarp(0)
                .build();

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.retry("workspace123", "task123", parserParams);
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testRetry_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.retry("", "task123");
        });
    }

    @Test
    public void testRetry_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.retry("workspace123", "");
        });
    }

    // ==================== amendCategory() 测试 ====================

    @Test
    public void testAmendCategory_Success() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.amendCategory("workspace123", "task123", "new_category");
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testAmendCategory_WithSplitTasks() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();

        when(httpClient.post(anyString(), any()))
                .thenReturn(responseNode);

        // 准备拆分任务列表
        List<SplitTaskRequest> splitTasks = new ArrayList<>();
        SplitTaskRequest task1 = SplitTaskRequest.builder()
                .category("invoice")
                .pages(Arrays.asList(0, 1))
                .build();
        splitTasks.add(task1);

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.amendCategory("workspace123", "task123", null, splitTasks, null);
        });

        // 验证调用
        verify(httpClient, times(1)).post(anyString(), any());
    }

    @Test
    public void testAmendCategory_InvalidWorkspaceId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.amendCategory("", "task123", "new_category");
        });
    }

    @Test
    public void testAmendCategory_InvalidTaskId() {
        assertThrows(ValidationException.class, () -> {
            fileResource.amendCategory("workspace123", "", "new_category");
        });
    }

    // ==================== uploadByUrls() 测试 ====================

    @Test
    public void testUploadByUrls_Success_FullParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "202412190001");
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        // 准备URL列表
        List<String> urls = Arrays.asList(
                "https://example.com/file1.pdf",
                "https://example.com/file2.pdf"
        );

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.uploadByUrls(
                    "workspace123",
                    "invoice",
                    urls,
                    "batch001",
                    true,
                    false,
                    true,
                    "extract",
                    false,
                    true,
                    false,
                    1,
                    "0"
            );
        });

        // 验证调用
        verify(httpClient, times(1)).request(anyString(), anyString(), anyMap(), any(), any(), any(), any());
    }

    @Test
    public void testUploadByUrls_Success_SimplifiedParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "202412190001");
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        // 准备URL列表
        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });

        // 验证调用
        verify(httpClient, times(1)).request(anyString(), anyString(), anyMap(), any(), any(), any(), any());
    }

    @Test
    public void testUploadByUrls_Success_MultipleUrls() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "202412190001");
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        // 准备10个URL（边界值）
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            urls.add("https://example.com/file" + i + ".pdf");
        }

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });

        // 验证调用
        verify(httpClient, times(1)).request(anyString(), anyString(), anyMap(), any(), any(), any(), any());
    }

    @Test
    public void testUploadByUrls_InvalidWorkspaceId_Empty() {
        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_InvalidCategory_Empty() {
        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "", urls);
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrls_Null() {
        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", null);
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrls_Empty() {
        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", new ArrayList<>());
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrls_TooMany() {
        // 准备11个URL（超过最大值10）
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            urls.add("https://example.com/file" + i + ".pdf");
        }

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrl_EmptyString() {
        List<String> urls = Arrays.asList("https://example.com/file.pdf", "");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrl_NoProtocol() {
        List<String> urls = Arrays.asList("example.com/file.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_InvalidUrl_FtpProtocol() {
        List<String> urls = Arrays.asList("ftp://example.com/file.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_ValidUrl_HttpProtocol() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "202412190001");
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        List<String> urls = Arrays.asList("http://example.com/file.pdf");

        assertDoesNotThrow(() -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadByUrls_ValidUrl_HttpsProtocol() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("batch_number", "202412190001");
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        assertDoesNotThrow(() -> {
            fileResource.uploadByUrls("workspace123", "invoice", urls);
        });
    }

    // ==================== uploadSyncByUrls() 测试 ====================

    @Test
    public void testUploadSyncByUrls_Success_FullParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 2);
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        // 准备URL列表
        List<String> urls = Arrays.asList(
                "https://example.com/file1.pdf",
                "https://example.com/file2.pdf"
        );

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.uploadSyncByUrls(
                    "workspace123",
                    "invoice",
                    urls,
                    "batch001",
                    true,
                    false,
                    true,
                    "extract",
                    false,
                    true,
                    false,
                    1,
                    "0",
                    true
            );
        });

        // 验证调用
        verify(httpClient, times(1)).request(anyString(), anyString(), anyMap(), any(), any(), any(), any());
    }

    @Test
    public void testUploadSyncByUrls_Success_SimplifiedParameters() {
        // 模拟响应
        ObjectNode responseNode = objectMapper.createObjectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("total", 1);
        responseNode.set("result", resultNode);

        when(httpClient.request(anyString(), anyString(), anyMap(), any(), any(), any(), any()))
                .thenReturn(responseNode);

        // 准备URL列表
        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        // 执行测试
        assertDoesNotThrow(() -> {
            fileResource.uploadSyncByUrls("workspace123", "invoice", urls);
        });

        // 验证调用
        verify(httpClient, times(1)).request(anyString(), anyString(), anyMap(), any(), any(), any(), any());
    }

    @Test
    public void testUploadSyncByUrls_InvalidWorkspaceId() {
        List<String> urls = Arrays.asList("https://example.com/file.pdf");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadSyncByUrls("", "invoice", urls);
        });
    }

    @Test
    public void testUploadSyncByUrls_InvalidUrls_TooMany() {
        // 准备11个URL（超过最大值10）
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            urls.add("https://example.com/file" + i + ".pdf");
        }

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadSyncByUrls("workspace123", "invoice", urls);
        });
    }

    @Test
    public void testUploadSyncByUrls_InvalidUrl_Format() {
        List<String> urls = Arrays.asList("not-a-valid-url");

        assertThrows(ValidationException.class, () -> {
            fileResource.uploadSyncByUrls("workspace123", "invoice", urls);
        });
    }

    // ==================== 工具方法 ====================

    private File createTestFile(String fileName) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, "test content".getBytes());
        return filePath.toFile();
    }
}
