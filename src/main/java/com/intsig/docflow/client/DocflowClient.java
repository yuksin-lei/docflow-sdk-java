package com.intsig.docflow.client;

import com.intsig.docflow.config.DocflowConfig;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.exception.ValidationException;
import com.intsig.docflow.http.AuthHandler;
import com.intsig.docflow.http.HttpClient;
import com.intsig.docflow.i18n.I18nManager;
import com.intsig.docflow.resource.CategoryResource;
import com.intsig.docflow.resource.FileResource;
import com.intsig.docflow.resource.ReviewResource;
import com.intsig.docflow.resource.WorkspaceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DocFlow SDK 主客户端
 *
 * <p>提供对工作空间、类别、字段、表格、样本等资源的访问接口</p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 方式1: 直接初始化（使用默认 base_url）
 * DocflowClient client = new DocflowClient("your-app-id", "your-secret-code");
 *
 * // 方式2: 从环境变量加载
 * DocflowClient client = DocflowClient.fromEnv();
 *
 * // 方式3: 使用构建器自定义配置
 * DocflowClient client = DocflowClient.builder()
 *     .appId("your-app-id")
 *     .secretCode("your-secret-code")
 *     .baseUrl("https://custom.api.com")
 *     .timeout(60)
 *     .maxRetries(5)
 *     .language("en_US")
 *     .build();
 *
 * // 使用客户端
 * WorkspaceCreateResponse workspace = client.workspace().create(
 *     WorkspaceCreateRequest.builder()
 *         .enterpriseId(12345L)
 *         .name("我的工作空间")
 *         .authScope(AuthScope.PUBLIC)
 *         .build()
 * );
 *
 * // 关闭客户端
 * client.close();
 * }</pre>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class DocflowClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(DocflowClient.class);

    private final DocflowConfig config;
    private final AuthHandler authHandler;
    private final HttpClient httpClient;

    // 资源操作接口
    private final WorkspaceResource workspaceResource;
    private final CategoryResource categoryResource;
    private final FileResource fileResource;
    private final ReviewResource reviewResource;

    /**
     * 构造 DocFlow 客户端
     *
     * @param appId      应用ID（对应请求头 x-ti-app-id）
     * @param secretCode 密钥（对应请求头 x-ti-secret-code）
     */
    public DocflowClient(String appId, String secretCode) {
        this(appId, secretCode, DocflowConfig.createDefault());
    }

    /**
     * 构造 DocFlow 客户端（自定义配置）
     *
     * @param appId      应用ID
     * @param secretCode 密钥
     * @param config     配置对象
     */
    public DocflowClient(String appId, String secretCode, DocflowConfig config) {
        // 参数校验
        if (appId == null || appId.trim().isEmpty()) {
            throw new ValidationException("应用ID不能为空");
        }
        if (secretCode == null || secretCode.trim().isEmpty()) {
            throw new ValidationException("密钥不能为空");
        }

        this.config = config;

        // 设置语言
        try {
            I18nManager.getInstance().setLanguage(config.getLanguage());
        } catch (IllegalArgumentException e) {
            logger.warn("不支持的语言: {}, 使用默认语言", config.getLanguage());
            I18nManager.getInstance().setLanguage(DocflowConstants.DEFAULT_LANGUAGE);
        }

        // 初始化认证处理器
        this.authHandler = new AuthHandler(appId, secretCode);

        // 初始化 HTTP 客户端
        this.httpClient = new HttpClient(config.getBaseUrl(), authHandler, config);

        // 初始化资源操作接口
        this.workspaceResource = new WorkspaceResource(httpClient);
        this.categoryResource = new CategoryResource(httpClient);
        this.fileResource = new FileResource(httpClient);
        this.reviewResource = new ReviewResource(httpClient);

        logger.info("DocflowClient 初始化完成: baseUrl={}, language={}",
                config.getBaseUrl(), config.getLanguage());
    }

    /**
     * 从环境变量创建客户端实例
     *
     * <p>默认读取以下环境变量：</p>
     * <ul>
     *     <li>DOCFLOW_APP_ID - 应用ID（必需）</li>
     *     <li>DOCFLOW_SECRET_CODE - 密钥（必需）</li>
     *     <li>DOCFLOW_BASE_URL - API地址（可选，默认使用 https://docflow.textin.com）</li>
     * </ul>
     *
     * @return DocflowClient 实例
     * @throws ValidationException 当必需的环境变量未设置时
     */
    public static DocflowClient fromEnv() {
        return fromEnv(
                DocflowConstants.ENV_APP_ID,
                DocflowConstants.ENV_SECRET_CODE,
                DocflowConstants.ENV_BASE_URL
        );
    }

    /**
     * 从环境变量创建客户端实例（自定义环境变量名）
     *
     * @param appIdEnv      应用ID的环境变量名
     * @param secretCodeEnv 密钥的环境变量名
     * @param baseUrlEnv    API地址的环境变量名
     * @return DocflowClient 实例
     * @throws ValidationException 当必需的环境变量未设置时
     */
    public static DocflowClient fromEnv(String appIdEnv, String secretCodeEnv, String baseUrlEnv) {
        String appId = System.getenv(appIdEnv);
        String secretCode = System.getenv(secretCodeEnv);
        String baseUrl = System.getenv(baseUrlEnv);

        if (appId == null || appId.trim().isEmpty()) {
            throw new ValidationException("环境变量 " + appIdEnv + " 未设置");
        }
        if (secretCode == null || secretCode.trim().isEmpty()) {
            throw new ValidationException("环境变量 " + secretCodeEnv + " 未设置");
        }

        DocflowConfig.Builder configBuilder = DocflowConfig.builder();
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            configBuilder.baseUrl(baseUrl);
        }

        return new DocflowClient(appId, secretCode, configBuilder.build());
    }

    /**
     * 创建客户端构建器
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 获取工作空间资源操作接口
     *
     * @return WorkspaceResource
     */
    public WorkspaceResource workspace() {
        return workspaceResource;
    }

    /**
     * 获取类别资源操作接口
     *
     * @return CategoryResource
     */
    public CategoryResource category() {
        return categoryResource;
    }

    /**
     * 获取文件资源操作接口
     *
     * @return FileResource
     */
    public FileResource file() {
        return fileResource;
    }

    /**
     * 获取审核资源操作接口
     *
     * @return ReviewResource
     */
    public ReviewResource review() {
        return reviewResource;
    }

    /**
     * 更新认证凭证
     *
     * @param appId      新的应用ID
     * @param secretCode 新的密钥
     */
    public void setCredentials(String appId, String secretCode) {
        authHandler.setCredentials(appId, secretCode);
    }

    /**
     * 设置错误消息语言
     *
     * @param language 语言代码，如 'zh_CN'（中文）、'en_US'（英文）
     * @throws IllegalArgumentException 当语言不支持时
     */
    public void setLanguage(String language) {
        I18nManager.getInstance().setLanguage(language);
        config.setLanguage(language);
    }

    /**
     * 获取当前语言设置
     *
     * @return 当前语言代码
     */
    public String getLanguage() {
        return I18nManager.getInstance().getLanguage();
    }

    /**
     * 获取所有可用语言
     *
     * @return 可用语言列表
     */
    public String[] getAvailableLanguages() {
        return I18nManager.getInstance().getAvailableLanguages();
    }

    /**
     * 获取配置对象
     *
     * @return DocflowConfig
     */
    public DocflowConfig getConfig() {
        return config;
    }

    /**
     * 关闭客户端，释放资源
     */
    @Override
    public void close() {
        httpClient.close();
        logger.info("DocflowClient 已关闭");
    }

    /**
     * 客户端构建器
     */
    public static class Builder {
        private String appId;
        private String secretCode;
        private final DocflowConfig.Builder configBuilder = DocflowConfig.builder();

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder secretCode(String secretCode) {
            this.secretCode = secretCode;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        public Builder timeout(int timeout) {
            configBuilder.timeout(timeout);
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            configBuilder.maxRetries(maxRetries);
            return this;
        }

        public Builder retryBackoffFactor(double retryBackoffFactor) {
            configBuilder.retryBackoffFactor(retryBackoffFactor);
            return this;
        }

        public Builder language(String language) {
            configBuilder.language(language);
            return this;
        }

        public DocflowClient build() {
            return new DocflowClient(appId, secretCode, configBuilder.build());
        }
    }
}
