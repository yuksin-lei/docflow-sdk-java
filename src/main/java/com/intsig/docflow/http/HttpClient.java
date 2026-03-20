package com.intsig.docflow.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intsig.docflow.config.DocflowConfig;
import com.intsig.docflow.config.DocflowConstants;
import com.intsig.docflow.exception.*;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 请求客户端
 *
 * <p>支持重试、超时、连接池等特性</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String baseUrl;
    private final AuthHandler authHandler;
    private final DocflowConfig config;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    /**
     * 构造 HTTP 客户端
     *
     * @param baseUrl     API 基础地址
     * @param authHandler 认证处理器
     * @param config      配置对象
     */
    public HttpClient(String baseUrl, AuthHandler authHandler, DocflowConfig config) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.authHandler = authHandler;
        this.config = config;

        // 配置 OkHttpClient
        ConnectionPool connectionPool = new ConnectionPool(
                DocflowConstants.POOL_MAXSIZE,
                5,
                TimeUnit.MINUTES
        );

        this.client = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .addInterceptor(new RetryInterceptor(config))
                .build();

        // 配置 Jackson ObjectMapper
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        logger.debug("HttpClient 初始化完成: baseUrl={}, maxRetries={}, timeout={}s",
                baseUrl, config.getMaxRetries(), config.getTimeout());
    }

    /**
     * 构建请求头
     *
     * @param customHeaders 自定义请求头
     * @return 完整的请求头
     */
    private Map<String, String> buildHeaders(Map<String, String> customHeaders) {
        Map<String, String> headers = new HashMap<>();

        // 添加 User-Agent
        headers.put(DocflowConstants.HEADER_USER_AGENT, DocflowConstants.USER_AGENT);

        // 添加认证头
        headers.putAll(authHandler.getAuthHeaders());

        // 添加语言头
        if (config.getLanguage() != null) {
            headers.put(DocflowConstants.HEADER_LANGUAGE, config.getLanguage());
        }

        // 添加自定义头
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }

        return headers;
    }

    /**
     * 处理响应
     *
     * @param response HTTP 响应
     * @return 响应数据的 JsonNode
     * @throws DocflowException 各种异常
     */
    private JsonNode handleResponse(Response response) throws DocflowException {
        int statusCode = response.code();

        try {
            // 读取响应体
            String responseBody = response.body() != null ? response.body().string() : "";

            // HTTP 状态码检查
            if (statusCode == 401) {
                throw new AuthenticationException("认证失败，请检查 app_id 和 secret_code");
            } else if (statusCode == 403) {
                throw new PermissionDeniedException("权限不足");
            } else if (statusCode == 404) {
                throw new ResourceNotFoundException("资源不存在");
            } else if (!response.isSuccessful()) {
                // 尝试解析错误响应
                String errorMessage = responseBody;
                String code = null;
                String traceId = null;

                try {
                    JsonNode errorData = objectMapper.readTree(responseBody);
                    // 优先使用 msg 字段，回退到 message
                    if (errorData.has("msg")) {
                        errorMessage = errorData.get("msg").asText();
                    } else if (errorData.has("message")) {
                        errorMessage = errorData.get("message").asText();
                    }

                    if (errorData.has("code")) {
                        code = errorData.get("code").asText();
                    }
                    if (errorData.has("traceId")) {
                        traceId = errorData.get("traceId").asText();
                    }
                } catch (Exception ignored) {
                    // 解析失败，使用原始响应体
                }

                throw new ApiException(statusCode, errorMessage, code, traceId);
            }

            // 解析 JSON 响应
            JsonNode data = objectMapper.readTree(responseBody);

            // 检查业务状态码
            if (data.has("code") && data.get("code").asInt() != 200) {
                String errorMessage = "未知错误";
                if (data.has("msg")) {
                    errorMessage = data.get("msg").asText();
                } else if (data.has("message")) {
                    errorMessage = data.get("message").asText();
                }

                String code = data.has("code") ? data.get("code").asText() : null;
                String traceId = data.has("traceId") ? data.get("traceId").asText() : null;

                throw new ApiException(statusCode, errorMessage, code, traceId);
            }

            return data;

        } catch (IOException e) {
            throw new NetworkException("响应解析失败", e);
        }
    }

    /**
     * 发送 GET 请求
     *
     * @param path   请求路径
     * @param params URL 参数
     * @return 响应数据
     * @throws DocflowException 各种异常
     */
    public JsonNode get(String path, Map<String, String> params) throws DocflowException {
        return request("GET", path, params, null, null, null, null);
    }

    /**
     * 发送 POST 请求（JSON 数据）
     *
     * @param path     请求路径
     * @param jsonData JSON 数据
     * @return 响应数据
     * @throws DocflowException 各种异常
     */
    public JsonNode post(String path, Object jsonData) throws DocflowException {
        return request("POST", path, null, jsonData, null, null, null);
    }

    /**
     * 发送 POST 请求（表单数据）
     *
     * @param path     请求路径
     * @param formData 表单数据
     * @return 响应数据
     * @throws DocflowException 各种异常
     */
    public JsonNode postForm(String path, Map<String, String> formData) throws DocflowException {
        return request("POST", path, null, null, formData, null, null);
    }

    /**
     * 发送 POST 请求（multipart 数据）
     *
     * @param path          请求路径
     * @param multipartBody multipart 请求体
     * @return 响应数据
     * @throws DocflowException 各种异常
     */
    public JsonNode postMultipart(String path, RequestBody multipartBody) throws DocflowException {
        return request("POST", path, null, null, null, multipartBody, null);
    }

    /**
     * 发送 HTTP 请求（通用方法）
     *
     * @param method        HTTP 方法
     * @param path          请求路径
     * @param params        URL 参数
     * @param jsonData      JSON 数据
     * @param formData      表单数据
     * @param multipartBody multipart 请求体
     * @param customHeaders 自定义请求头
     * @return 响应数据
     * @throws DocflowException 各种异常
     */
    public JsonNode request(
            String method,
            String path,
            Map<String, String> params,
            Object jsonData,
            Map<String, String> formData,
            RequestBody multipartBody,
            Map<String, String> customHeaders
    ) throws DocflowException {
        try {
            // 构建 URL
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + path).newBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            String url = urlBuilder.build().toString();

            // 构建请求头
            Map<String, String> headers = buildHeaders(customHeaders);
            Request.Builder requestBuilder = new Request.Builder().url(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }

            // 构建请求体
            RequestBody requestBody = null;
            if (jsonData != null) {
                String jsonString = objectMapper.writeValueAsString(jsonData);
                requestBody = RequestBody.create(jsonString, JSON);
            } else if (formData != null) {
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : formData.entrySet()) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
                requestBody = formBuilder.build();
            } else if (multipartBody != null) {
                requestBody = multipartBody;
            }

            // 设置请求方法和请求体
            if ("GET".equals(method)) {
                requestBuilder.get();
            } else if ("POST".equals(method)) {
                requestBuilder.post(requestBody != null ? requestBody : RequestBody.create("", null));
            } else if ("PUT".equals(method)) {
                requestBuilder.put(requestBody != null ? requestBody : RequestBody.create("", null));
            } else if ("DELETE".equals(method)) {
                if (requestBody != null) {
                    requestBuilder.delete(requestBody);
                } else {
                    requestBuilder.delete();
                }
            }

            Request request = requestBuilder.build();

            logger.debug("发送请求: {} {}", method, url);

            // 执行请求
            long startTime = System.currentTimeMillis();
            Response response = client.newCall(request).execute();
            long elapsed = System.currentTimeMillis() - startTime;

            logger.debug("请求完成: {} {}, status={}, elapsed={}ms",
                    method, url, response.code(), elapsed);

            // 处理响应
            return handleResponse(response);

        } catch (IOException e) {
            logger.error("网络请求失败: {} {}", method, path, e);
            throw new NetworkException("网络请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     *
     * @param path   请求路径
     * @param params URL 参数
     * @return 文件字节数组和文件名
     * @throws DocflowException 各种异常
     */
    public DownloadResult download(String path, Map<String, String> params) throws DocflowException {
        try {
            // 构建 URL
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + path).newBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            String url = urlBuilder.build().toString();

            // 构建请求头
            Map<String, String> headers = buildHeaders(null);
            Request.Builder requestBuilder = new Request.Builder().url(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }

            Request request = requestBuilder.build();

            logger.debug("下载文件: GET {}", url);

            // 执行请求
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), "文件下载失败");
            }

            // 读取文件数据
            byte[] fileData = response.body().bytes();

            // 提取文件名
            String fileName = "download";
            String contentDisposition = response.header("Content-Disposition");
            if (contentDisposition != null && contentDisposition.contains("filename")) {
                // 解析文件名
                String[] parts = contentDisposition.split("filename\\*?=");
                if (parts.length > 1) {
                    fileName = parts[1].trim();
                    // 移除引号
                    fileName = fileName.replaceAll("^\"|\"$", "");
                    // 处理 UTF-8'' 前缀
                    if (fileName.startsWith("UTF-8''")) {
                        fileName = java.net.URLDecoder.decode(fileName.substring(7), "UTF-8");
                    }
                }
            }

            logger.debug("文件下载成功: fileName={}, size={} bytes", fileName, fileData.length);

            return new DownloadResult(fileData, fileName);

        } catch (IOException e) {
            logger.error("文件下载失败: GET {}", path, e);
            throw new NetworkException("文件下载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 关闭客户端，释放资源
     */
    public void close() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        logger.debug("HttpClient 已关闭");
    }

    /**
     * 下载结果
     */
    public static class DownloadResult {
        private final byte[] data;
        private final String fileName;

        public DownloadResult(byte[] data, String fileName) {
            this.data = data;
            this.fileName = fileName;
        }

        public byte[] getData() {
            return data;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
