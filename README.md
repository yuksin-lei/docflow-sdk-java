# DocFlow Java SDK

DocFlow Java SDK 提供简洁易用的 API 接口，用于文件上传和处理结果获取。

## 特性

- ✅ **完整的 API 封装** - 支持工作空间、类别、字段、表格、样本等所有资源操作
- ✅ **自动分页迭代** - 内置迭代器，自动处理分页查询 ⭐ **NEW**
- ✅ **自动重试机制** - 内置指数退避重试策略，提高请求成功率
- ✅ **国际化支持** - 支持中文和英文错误消息，可动态切换
- ✅ **类型安全** - 使用枚举和强类型模型，避免参数错误
- ✅ **易于使用** - 提供流式 Builder API 和环境变量配置
- ✅ **详细日志** - 集成 SLF4J，支持请求日志和调试信息

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

## 快速开始

### 基本用法

```java
import com.intsig.docflow.client.DocflowClient;
import com.intsig.docflow.model.workspace.*;
import com.intsig.docflow.enums.AuthScope;

// 创建客户端
DocflowClient client = new DocflowClient("your-app-id", "your-secret-code");

// 创建工作空间
WorkspaceCreateResponse workspace = client.workspace().create(
    WorkspaceCreateRequest.builder()
        .enterpriseId(12345L)
        .name("我的工作空间")
        .description("测试工作空间")
        .authScope(AuthScope.PUBLIC)
        .build()
);

System.out.println("工作空间ID: " + workspace.getWorkspaceId());

// 获取工作空间列表
WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);
System.out.println("总共 " + workspaces.getTotal() + " 个工作空间");

// 关闭客户端
client.close();
```

### 从环境变量加载配置

```java
// 设置环境变量
// export DOCFLOW_APP_ID="your-app-id"
// export DOCFLOW_SECRET_CODE="your-secret-code"
// export DOCFLOW_BASE_URL="https://docflow.textin.com"  // 可选

DocflowClient client = DocflowClient.fromEnv();
```

### 自定义配置

```java
import com.intsig.docflow.client.DocflowClient;

DocflowClient client = DocflowClient.builder()
    .appId("your-app-id")
    .secretCode("your-secret-code")
    .baseUrl("https://custom.api.com")
    .timeout(60)                    // 超时时间（秒）
    .maxRetries(5)                  // 最大重试次数
    .retryBackoffFactor(2.0)        // 重试间隔因子
    .language("en_US")              // 语言设置
    .build();
```

### 使用 try-with-resources

```java
try (DocflowClient client = new DocflowClient("your-app-id", "your-secret-code")) {
    // 使用客户端
    WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);
    // 自动关闭客户端
}
```

### 使用分页迭代器 ⭐

自动处理分页，像遍历普通集合一样遍历所有数据：

```java
import com.intsig.docflow.iterator.PageIterable;
import com.intsig.docflow.model.file.FileInfo;

// 遍历所有文件
PageIterable<FileInfo> files = client.file.iter(workspaceId);
for (FileInfo file : files) {
    System.out.println("文件: " + file.getFileName());
}

// 使用 Stream API
long pdfCount = StreamSupport.stream(files.spliterator(), false)
    .filter(f -> f.getFileName().endsWith(".pdf"))
    .count();

// 带条件筛选
PageIterable<FileInfo> filtered = client.file.iter(
    workspaceId,
    "batch-001",  // 批次编号
    "发票",        // 类别
    "success",     // 识别状态
    2              // 核对状态
);

// 限制获取数量
int count = 0;
for (FileInfo file : files) {
    processFile(file);
    if (++count >= 100) break;  // 只处理前100个
}
```

详细说明请参考 [**分页迭代器使用指南**](ITERATOR_GUIDE.md)

---

## 📈 优化建议

参考业界最佳实践（AWS SDK、Stripe SDK 等），我们为项目准备了全面的优化建议：

- 📋 [**优化总览**](OPTIMIZATION_SUMMARY.md) - 快速了解优化建议
- 🗺️ [**优化路线图**](OPTIMIZATION_ROADMAP.md) - 详细的15项优化建议
- 💻 [**实现示例**](OPTIMIZATION_EXAMPLES.md) - 具体的代码实现
- ✅ [**检查清单**](OPTIMIZATION_CHECKLIST.md) - 对照业界标准自查

**核心优化方向**：
- ⚡ 异步/响应式编程支持
- 🔧 完善的拦截器链
- 📊 可观测性增强（Metrics + Tracing）
- 🧪 完整的 CI/CD 流程
- 🎯 智能重试策略
- 📦 批量操作优化

查看 [优化总览](OPTIMIZATION_SUMMARY.md) 了解详情。

## 核心功能

### 工作空间管理

```java
// 创建工作空间
WorkspaceCreateResponse workspace = client.workspace().create(
    WorkspaceCreateRequest.builder()
        .enterpriseId(12345L)
        .name("我的工作空间")
        .authScope(AuthScope.PUBLIC)
        .build()
);

// 获取工作空间列表
WorkspaceListResponse workspaces = client.workspace().list(12345L, 1, 20);

// 获取工作空间详情
WorkspaceInfo workspace = client.workspace().get("123456789");

// 删除工作空间
client.workspace().delete("123456789");

// 批量删除工作空间
client.workspace().delete(Arrays.asList("123456789", "987654321"));
```

### 类别管理

```java
import com.intsig.docflow.enums.EnabledStatus;

// 获取类别列表
CategoryListResponse categories = client.category().list("workspace_id");

// 获取类别列表（带筛选）
CategoryListResponse categories = client.category().list(
    "workspace_id",
    1,                          // 页码
    20,                         // 每页数量
    EnabledStatus.ENABLED       // 启用状态
);
```

## 重试机制

SDK 内置自动重试机制，支持以下场景：

- **网络异常** - 连接超时、连接失败等
- **特定 HTTP 状态码** - 423（资源锁定）、429（请求过多）、500（服务器错误）、503（服务不可用）、504（网关超时）

重试策略：
- 默认最大重试次数：3 次
- 默认重试间隔：指数退避（0s, 2s, 4s, 8s...）
- 可自定义重试参数

```java
DocflowClient client = DocflowClient.builder()
    .appId("your-app-id")
    .secretCode("your-secret-code")
    .maxRetries(5)              // 最大重试 5 次
    .retryBackoffFactor(2.0)    // 重试间隔倍数
    .build();
```

## 国际化支持

SDK 支持中文和英文错误消息，可动态切换：

```java
// 设置语言为英文
client.setLanguage("en_US");

// 设置语言为中文
client.setLanguage("zh_CN");

// 获取当前语言
String language = client.getLanguage();

// 获取所有可用语言
String[] languages = client.getAvailableLanguages();
```

## 异常处理

SDK 定义了以下异常类型：

- **AuthenticationException** - 认证失败（HTTP 401）
- **PermissionDeniedException** - 权限不足（HTTP 403）
- **ResourceNotFoundException** - 资源不存在（HTTP 404）
- **ValidationException** - 参数校验失败
- **ApiException** - API 调用失败
- **NetworkException** - 网络请求失败

```java
import com.intsig.docflow.exception.*;

try {
    WorkspaceInfo workspace = client.workspace().get("invalid_id");
} catch (ValidationException e) {
    System.err.println("参数错误: " + e.getMessage());
} catch (ResourceNotFoundException e) {
    System.err.println("工作空间不存在: " + e.getMessage());
} catch (NetworkException e) {
    System.err.println("网络错误: " + e.getMessage());
} catch (DocflowException e) {
    System.err.println("SDK错误: " + e.getMessage());
}
```

## 日志配置

SDK 使用 SLF4J 记录日志。推荐在项目中添加 Logback 依赖：

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

配置 `logback.xml`：

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

## 依赖要求

- Java 8 或更高版本
- OkHttp 4.12.0
- Jackson 2.15.3
- SLF4J 2.0.9

## 许可证

本项目采用 MIT 许可证。

## 支持

- 文档：https://docs.docflow.textin.com
- 问题反馈：https://github.com/intsig/docflow-sdk-java/issues
