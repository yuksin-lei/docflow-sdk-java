# DocFlow Java SDK 快速入门指南

## 安装

### Maven

```xml
<dependency>
    <groupId>com.intsig.docflow</groupId>
    <artifactId>docflow-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.intsig.docflow:docflow-sdk:1.0.0'
```

## 5 分钟上手

### 1. 创建客户端

```java
import com.intsig.docflow.client.DocflowClient;

// 最简单的方式
DocflowClient client = new DocflowClient("your-app-id", "your-secret-code");

// 使用 try-with-resources 自动关闭
try (DocflowClient client = new DocflowClient("your-app-id", "your-secret-code")) {
    // 使用客户端
}
```

### 2. 创建工作空间

```java
import com.intsig.docflow.model.workspace.*;
import com.intsig.docflow.enums.AuthScope;

WorkspaceCreateResponse workspace = client.workspace().create(
    WorkspaceCreateRequest.builder()
        .enterpriseId(12345L)
        .name("我的工作空间")
        .description("测试工作空间")
        .authScope(AuthScope.PUBLIC)  // 企业成员可见
        .build()
);

System.out.println("工作空间创建成功，ID: " + workspace.getWorkspaceId());
```

### 3. 查询工作空间列表

```java
WorkspaceListResponse workspaces = client.workspace().list(
    12345L,  // 企业ID
    1,       // 页码
    20       // 每页数量
);

System.out.println("总共 " + workspaces.getTotal() + " 个工作空间");
workspaces.getWorkspaces().forEach(ws -> {
    System.out.println("- " + ws.getName() + " (ID: " + ws.getId() + ")");
});
```

### 4. 查询工作空间详情

```java
String workspaceId = "123456789";
WorkspaceInfo workspace = client.workspace().get(workspaceId);

System.out.println("工作空间名称: " + workspace.getName());
System.out.println("工作空间描述: " + workspace.getDescription());
```

### 5. 获取类别列表

```java
import com.intsig.docflow.model.category.*;
import com.intsig.docflow.enums.EnabledStatus;

// 获取所有类别
CategoryListResponse categories = client.category().list("workspace_id");

// 只获取已启用的类别
CategoryListResponse enabledCategories = client.category().list(
    "workspace_id",
    1,                          // 页码
    20,                         // 每页数量
    EnabledStatus.ENABLED       // 只获取已启用的
);

System.out.println("类别总数: " + categories.getTotal());
categories.getCategories().forEach(cat -> {
    System.out.println("- " + cat.getName() + " (ID: " + cat.getId() + ")");
});
```

### 6. 删除工作空间

```java
// 删除单个工作空间
client.workspace().delete("123456789");

// 批量删除工作空间
client.workspace().delete(Arrays.asList(
    "123456789",
    "987654321"
));
```

## 分页迭代器

> 💡 **推荐**: 使用迭代器自动处理分页查询，无需手动管理页码。

SDK 提供了分页迭代器功能，支持 for-each 循环和 Stream API，让您像遍历普通集合一样遍历所有分页数据。

### 基本使用

```java
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.model.file.FileInfo;

// 遍历所有文件
PageIterable<FileInfo> files = client.file.iter(workspaceId);
for (FileInfo file : files) {
    System.out.println("文件: " + file.getFileName());
}
```

### 使用 Stream API

```java
// 过滤和统计
long pdfCount = StreamSupport.stream(files.spliterator(), false)
    .filter(f -> f.getFileName().endsWith(".pdf"))
    .count();
```

### 带条件筛选

```java
// 只遍历符合条件的文件
PageIterable<FileInfo> files = client.file.iter(
    workspaceId,
    "batch-001",      // 批次编号
    "发票",            // 类别
    "success",         // 识别状态
    2                  // 核对状态
);

for (FileInfo file : files) {
    processFile(file);
}
```

### 限制获取数量

```java
// 只获取前3页
PageIterable<FileInfo> files = client.file.iter(
    workspaceId,
    null,    // 默认 pageSize (1000)
    3,       // 最多3页
    null, null, null, null, null, null, null, null, null
);

// 或者提前终止
int count = 0;
for (FileInfo file : files) {
    processFile(file);
    if (++count >= 100) break;  // 只处理前100个
}
```

### 支持的资源

- **文件资源**: `client.file.iter(workspaceId, ...)`
- **工作空间**: `client.workspace.iter(enterpriseId, ...)`
- **类别资源**: `client.category.iter(workspaceId, ...)`
- **类别样本**: `client.category.iterSamples(workspaceId, categoryId, ...)`
- **审核规则库**: `client.review.iterRepos(workspaceId, ...)`

详细说明请参考 [分页迭代器使用指南](ITERATOR_GUIDE.md)。

## 高级功能

### 自定义配置

```java
DocflowClient client = DocflowClient.builder()
    .appId("your-app-id")
    .secretCode("your-secret-code")
    .baseUrl("https://custom.api.com")    // 自定义 API 地址
    .timeout(60)                          // 超时时间 60 秒
    .maxRetries(5)                        // 最大重试 5 次
    .retryBackoffFactor(2.0)              // 重试间隔倍数
    .language("en_US")                    // 英文错误消息
    .build();
```

### 从环境变量加载配置

```bash
# 设置环境变量
export DOCFLOW_APP_ID="your-app-id"
export DOCFLOW_SECRET_CODE="your-secret-code"
export DOCFLOW_BASE_URL="https://docflow.textin.com"  # 可选
```

```java
// 从环境变量创建客户端
DocflowClient client = DocflowClient.fromEnv();
```

### 切换语言

```java
// 设置为英文
client.setLanguage("en_US");

// 设置为中文
client.setLanguage("zh_CN");

// 获取当前语言
String language = client.getLanguage();  // "zh_CN"

// 获取所有可用语言
String[] languages = client.getAvailableLanguages();  // ["zh_CN", "en_US"]
```

### 异常处理

```java
import com.intsig.docflow.exception.*;

try {
    WorkspaceInfo workspace = client.workspace().get("invalid_id");

} catch (ValidationException e) {
    // 参数校验失败
    System.err.println("参数错误: " + e.getMessage());

} catch (ResourceNotFoundException e) {
    // 资源不存在 (HTTP 404)
    System.err.println("工作空间不存在: " + e.getMessage());

} catch (AuthenticationException e) {
    // 认证失败 (HTTP 401)
    System.err.println("认证失败: " + e.getMessage());

} catch (PermissionDeniedException e) {
    // 权限不足 (HTTP 403)
    System.err.println("权限不足: " + e.getMessage());

} catch (NetworkException e) {
    // 网络错误
    System.err.println("网络错误: " + e.getMessage());

} catch (ApiException e) {
    // API 调用失败
    System.err.println("API 错误: " + e.getMessage());
    System.err.println("状态码: " + e.getStatusCode());
    System.err.println("错误码: " + e.getCode());
    System.err.println("追踪ID: " + e.getTraceId());

} catch (DocflowException e) {
    // 其他 SDK 错误
    System.err.println("SDK 错误: " + e.getMessage());
}
```

## 完整示例

```java
import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.enums.AuthScope;
import com.intsig.docflow.enums.EnabledStatus;
import com.intsig.docflow.exception.*;
import com.intsig.docflow.model.category.*;
import com.intsig.docflow.model.workspace.*;

public class DocflowExample {
    public static void main(String[] args) {
        // 创建客户端（使用 try-with-resources 自动关闭）
        try (DocflowClient client = new DocflowClient("your-app-id", "your-secret-code")) {

            // 设置语言为中文
            client.setLanguage("zh_CN");

            // 1. 创建工作空间
            System.out.println("=== 创建工作空间 ===");
            WorkspaceCreateResponse createResp = client.workspace().create(
                WorkspaceCreateRequest.builder()
                    .enterpriseId(12345L)
                    .name("测试工作空间")
                    .description("这是一个测试工作空间")
                    .authScope(AuthScope.PUBLIC)
                    .build()
            );
            String workspaceId = createResp.getWorkspaceId();
            System.out.println("工作空间创建成功: " + workspaceId);

            // 2. 获取工作空间列表
            System.out.println("\n=== 获取工作空间列表 ===");
            WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);
            System.out.println("总共 " + workspaces.getTotal() + " 个工作空间");
            workspaces.getWorkspaces().forEach(ws -> {
                System.out.println("  - " + ws.getName() + " (ID: " + ws.getId() + ")");
            });

            // 3. 获取工作空间详情
            System.out.println("\n=== 获取工作空间详情 ===");
            WorkspaceInfo workspace = client.workspace().get(workspaceId);
            System.out.println("名称: " + workspace.getName());
            System.out.println("描述: " + workspace.getDescription());
            System.out.println("协作范围: " + workspace.getAuthScope());

            // 4. 获取类别列表
            System.out.println("\n=== 获取类别列表 ===");
            CategoryListResponse categories = client.category().list(workspaceId);
            System.out.println("类别总数: " + categories.getTotal());

            // 5. 获取已启用的类别
            System.out.println("\n=== 获取已启用的类别 ===");
            CategoryListResponse enabledCategories = client.category().list(
                workspaceId,
                1,
                20,
                EnabledStatus.ENABLED
            );
            System.out.println("已启用的类别数: " + enabledCategories.getTotal());
            enabledCategories.getCategories().forEach(cat -> {
                System.out.println("  - " + cat.getName() + " (ID: " + cat.getId() + ")");
            });

            // 6. 删除工作空间
            System.out.println("\n=== 删除工作空间 ===");
            client.workspace().delete(workspaceId);
            System.out.println("工作空间删除成功");

        } catch (ValidationException e) {
            System.err.println("参数错误: " + e.getMessage());
        } catch (AuthenticationException e) {
            System.err.println("认证失败: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("资源不存在: " + e.getMessage());
        } catch (NetworkException e) {
            System.err.println("网络错误: " + e.getMessage());
        } catch (DocflowException e) {
            System.err.println("SDK 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## 日志配置

### 添加 Logback 依赖

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

### 配置 logback.xml

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- DocFlow SDK 日志 -->
    <logger name="com.intsig.docflow" level="DEBUG"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

## 重试机制

SDK 内置自动重试机制，无需手动处理：

- **自动重试场景**：
  - 网络异常（连接超时、连接失败）
  - HTTP 状态码：423、429、500、503、504、900

- **默认配置**：
  - 最大重试次数：3 次
  - 重试间隔：0s, 2s, 4s, 8s...（指数退避）

- **自定义配置**：

```java
DocflowClient client = DocflowClient.builder()
    .maxRetries(5)              // 最大重试 5 次
    .retryBackoffFactor(2.0)    // 重试间隔倍数
    .build();
```

## 常见问题

### Q: 如何处理超时？

A: 可以自定义超时时间（默认 30 秒）：

```java
DocflowClient client = DocflowClient.builder()
    .timeout(60)  // 60 秒超时
    .build();
```

### Q: 如何处理并发请求？

A: SDK 内部使用连接池，支持并发请求。建议复用同一个客户端实例：

```java
// 好的做法：复用客户端实例
DocflowClient client = new DocflowClient("app-id", "secret");
// 多个线程可以安全地使用同一个 client 实例

// 不好的做法：每次请求创建新的客户端
// 这会浪费资源并降低性能
```

### Q: 如何调试 API 请求？

A: 启用 DEBUG 日志查看详细的请求信息：

```xml
<logger name="com.intsig.docflow" level="DEBUG"/>
```

## 下一步

- 查看 [完整文档](README.md)
- 查看 [分页迭代器使用指南](ITERATOR_GUIDE.md) ⭐
- 查看 [开发指南](DEVELOPMENT.md)
- 查看 [示例代码](examples/QuickStart.java)
- 查看 [API 文档](https://docs.docflow.textin.com)

## 需要帮助？

- 提交 Issue: https://github.com/intsig/docflow-sdk-java/issues
- 查看文档: https://docs.docflow.textin.com
