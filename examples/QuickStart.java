import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.enums.AuthScope;
import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.model.category.CategoryListResponse;
import com.intsig.docflow.model.workspace.*;

import java.util.Arrays;

/**
 * DocFlow SDK 快速开始示例
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class QuickStart {

    public static void main(String[] args) {
        // 方式1: 直接创建客户端
        try (DocflowClient client = new DocflowClient("your-app-id", "your-secret-code")) {

            // ========== 工作空间管理 ==========

            // 创建工作空间
            WorkspaceCreateResponse createResp = client.workspace().create(
                    WorkspaceCreateRequest.builder()
                            .enterpriseId(12345L)
                            .name("我的工作空间")
                            .description("测试工作空间")
                            .authScope(AuthScope.PUBLIC)
                            .build()
            );
            System.out.println("创建工作空间成功: " + createResp.getWorkspaceId());

            String workspaceId = createResp.getWorkspaceId();

            // 获取工作空间列表
            WorkspaceListResponse listResp = client.workspace().list(12345L, 1, 20);
            System.out.println("工作空间总数: " + listResp.getTotal());
            listResp.getWorkspaces().forEach(ws -> {
                System.out.println("  - " + ws.getName() + " (ID: " + ws.getId() + ")");
            });

            // 获取工作空间详情
            WorkspaceInfo workspace = client.workspace().get(workspaceId);
            System.out.println("工作空间名称: " + workspace.getName());

            // ========== 类别管理 ==========

            // 获取类别列表
            CategoryListResponse categories = client.category().list(workspaceId);
            System.out.println("类别总数: " + categories.getTotal());

            // 获取类别列表（带筛选）
            CategoryListResponse enabledCategories = client.category().list(
                    workspaceId,
                    1,
                    20,
                    EnabledStatus.ENABLED
            );
            System.out.println("已启用的类别数: " + enabledCategories.getTotal());

            // ========== 删除工作空间 ==========

            // 删除单个工作空间
            client.workspace().delete(workspaceId);
            System.out.println("删除工作空间成功");

        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 方式2: 从环境变量加载配置
     */
    public static void fromEnvironment() {
        // 设置环境变量:
        // export DOCFLOW_APP_ID="your-app-id"
        // export DOCFLOW_SECRET_CODE="your-secret-code"
        // export DOCFLOW_BASE_URL="https://docflow.textin.com"  // 可选

        try (DocflowClient client = DocflowClient.fromEnv()) {
            WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);
            System.out.println("工作空间总数: " + workspaces.getTotal());
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
        }
    }

    /**
     * 方式3: 使用自定义配置
     */
    public static void withCustomConfig() {
        try (DocflowClient client = DocflowClient.builder()
                .appId("your-app-id")
                .secretCode("your-secret-code")
                .baseUrl("https://custom.api.com")
                .timeout(60)                    // 超时时间60秒
                .maxRetries(5)                  // 最大重试5次
                .retryBackoffFactor(2.0)        // 重试间隔倍数
                .language("en_US")              // 英文错误消息
                .build()) {

            WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);
            System.out.println("Total workspaces: " + workspaces.getTotal());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * 演示国际化支持
     */
    public static void demonstrateI18n() {
        try (DocflowClient client = new DocflowClient("your-app-id", "your-secret-code")) {

            // 使用中文错误消息
            client.setLanguage("zh_CN");
            System.out.println("当前语言: " + client.getLanguage());

            // 切换到英文
            client.setLanguage("en_US");
            System.out.println("Current language: " + client.getLanguage());

            // 获取所有可用语言
            String[] languages = client.getAvailableLanguages();
            System.out.println("可用语言: " + Arrays.toString(languages));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
