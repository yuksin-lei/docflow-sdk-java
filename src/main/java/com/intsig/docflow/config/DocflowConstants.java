package com.intsig.docflow.config;

/**
 * DocFlow SDK 常量定义
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class DocflowConstants {

    /**
     * 默认 API 基础地址
     */
    public static final String DEFAULT_BASE_URL = "https://docflow.textin.com";

    /**
     * API 路径前缀
     */
    public static final String API_PREFIX = "/app-api/sip/platform/v2";

    /**
     * 默认请求超时时间（秒）
     */
    public static final int DEFAULT_TIMEOUT = 30;

    /**
     * 默认最大重试次数
     */
    public static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * 默认重试间隔计算因子
     */
    public static final double DEFAULT_RETRY_BACKOFF_FACTOR = 1.0;

    /**
     * 默认语言
     */
    public static final String DEFAULT_LANGUAGE = "zh_CN";

    /**
     * 支持的语言列表
     */
    public static final String[] SUPPORTED_LANGUAGES = {"zh_CN", "en_US"};

    /**
     * 默认分页页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 连接池大小
     */
    public static final int POOL_CONNECTIONS = 10;

    /**
     * 连接池最大连接数
     */
    public static final int POOL_MAXSIZE = 20;

    /**
     * 请求头：应用ID
     */
    public static final String HEADER_APP_ID = "x-ti-app-id";

    /**
     * 请求头：密钥
     */
    public static final String HEADER_SECRET_CODE = "x-ti-secret-code";

    /**
     * 请求头：语言
     */
    public static final String HEADER_LANGUAGE = "lang";

    /**
     * 请求头：User-Agent
     */
    public static final String HEADER_USER_AGENT = "User-Agent";

    /**
     * User-Agent 值
     */
    public static final String USER_AGENT = "DocflowSDK/1.0.0 Java";

    /**
     * 需要重试的 HTTP 状态码
     */
    public static final int[] DEFAULT_RETRY_STATUS_CODES = {423, 429, 500, 503, 504, 900};

    /**
     * 允许重试的 HTTP 方法
     */
    public static final String[] DEFAULT_RETRY_METHODS = {"GET", "POST", "PUT", "DELETE"};

    /**
     * 环境变量：应用ID
     */
    public static final String ENV_APP_ID = "DOCFLOW_APP_ID";

    /**
     * 环境变量：密钥
     */
    public static final String ENV_SECRET_CODE = "DOCFLOW_SECRET_CODE";

    /**
     * 环境变量：API 基础地址
     */
    public static final String ENV_BASE_URL = "DOCFLOW_BASE_URL";

    private DocflowConstants() {
        // 工具类，禁止实例化
    }
}
