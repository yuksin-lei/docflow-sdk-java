package com.intsig.docflow.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * DocFlow SDK 配置类
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class DocflowConfig {

    /**
     * API 基础地址
     */
    private String baseUrl;

    /**
     * 请求超时时间（秒）
     */
    private int timeout;

    /**
     * 最大重试次数
     */
    private int maxRetries;

    /**
     * 重试间隔计算因子
     */
    private double retryBackoffFactor;

    /**
     * 语言设置
     */
    private String language;

    /**
     * 需要重试的 HTTP 状态码
     */
    private Set<Integer> retryStatusCodes;

    /**
     * 允许重试的 HTTP 方法
     */
    private Set<String> retryMethods;

    /**
     * 私有构造函数，使用 Builder 模式
     */
    private DocflowConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.timeout = builder.timeout;
        this.maxRetries = builder.maxRetries;
        this.retryBackoffFactor = builder.retryBackoffFactor;
        this.language = builder.language;
        this.retryStatusCodes = builder.retryStatusCodes;
        this.retryMethods = builder.retryMethods;
    }

    /**
     * 创建默认配置
     *
     * @return 默认配置
     */
    public static DocflowConfig createDefault() {
        return builder().build();
    }

    /**
     * 创建 Builder
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getBaseUrl() {
        return baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public double getRetryBackoffFactor() {
        return retryBackoffFactor;
    }

    public String getLanguage() {
        return language;
    }

    public Set<Integer> getRetryStatusCodes() {
        return retryStatusCodes;
    }

    public Set<String> getRetryMethods() {
        return retryMethods;
    }

    // Setters for dynamic updates
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 配置构建器
     */
    public static class Builder {
        private String baseUrl = DocflowConstants.DEFAULT_BASE_URL;
        private int timeout = DocflowConstants.DEFAULT_TIMEOUT;
        private int maxRetries = DocflowConstants.DEFAULT_MAX_RETRIES;
        private double retryBackoffFactor = DocflowConstants.DEFAULT_RETRY_BACKOFF_FACTOR;
        private String language = DocflowConstants.DEFAULT_LANGUAGE;
        private Set<Integer> retryStatusCodes = new HashSet<>(Arrays.asList(423, 429, 500, 503, 504, 900));
        private Set<String> retryMethods = new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryBackoffFactor(double retryBackoffFactor) {
            this.retryBackoffFactor = retryBackoffFactor;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder retryStatusCodes(Set<Integer> retryStatusCodes) {
            this.retryStatusCodes = retryStatusCodes;
            return this;
        }

        public Builder retryMethods(Set<String> retryMethods) {
            this.retryMethods = retryMethods;
            return this;
        }

        public DocflowConfig build() {
            return new DocflowConfig(this);
        }
    }
}
