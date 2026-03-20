package com.intsig.docflow;

import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.enums.AuthScope;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.model.workspace.WorkspaceCreateRequest;
import com.intsig.docflow.model.workspace.WorkspaceListResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DocflowClient 测试类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class DocflowClientTest {

    @Test
    public void testClientCreation() {
        // 测试正常创建
        DocflowClient client = new DocflowClient("test-app-id", "test-secret");
        assertNotNull(client);
        client.close();
    }

    @Test
    public void testClientCreationWithEmptyAppId() {
        // 测试空 appId
        assertThrows(ValidationException.class, () -> {
            new DocflowClient("", "test-secret");
        });
    }

    @Test
    public void testClientCreationWithEmptySecretCode() {
        // 测试空 secretCode
        assertThrows(ValidationException.class, () -> {
            new DocflowClient("test-app-id", "");
        });
    }

    @Test
    public void testBuilderPattern() {
        // 测试 Builder 模式
        DocflowClient client = DocflowClient.builder()
                .appId("test-app-id")
                .secretCode("test-secret")
                .baseUrl("https://test.example.com")
                .timeout(60)
                .maxRetries(5)
                .language("en_US")
                .build();

        assertNotNull(client);
        assertEquals("en_US", client.getLanguage());
        client.close();
    }

    @Test
    public void testLanguageSetting() {
        DocflowClient client = new DocflowClient("test-app-id", "test-secret");

        // 测试设置语言
        client.setLanguage("en_US");
        assertEquals("en_US", client.getLanguage());

        client.setLanguage("zh_CN");
        assertEquals("zh_CN", client.getLanguage());

        // 测试获取可用语言
        String[] languages = client.getAvailableLanguages();
        assertTrue(languages.length >= 2);

        client.close();
    }

    @Test
    public void testTryWithResources() {
        // 测试 try-with-resources
        try (DocflowClient client = new DocflowClient("test-app-id", "test-secret")) {
            assertNotNull(client);
        }
        // 自动关闭
    }

    @Test
    public void testWorkspaceRequestBuilder() {
        // 测试工作空间请求构建器
        WorkspaceCreateRequest request = WorkspaceCreateRequest.builder()
                .name("测试工作空间")
                .description("这是一个测试工作空间")
                .enterpriseId(12345L)
                .authScope(AuthScope.PUBLIC)
                .build();

        assertEquals("测试工作空间", request.getName());
        assertEquals("这是一个测试工作空间", request.getDescription());
        assertEquals(12345L, request.getEnterpriseId());
        assertEquals(1, request.getAuthScope());
    }
}
