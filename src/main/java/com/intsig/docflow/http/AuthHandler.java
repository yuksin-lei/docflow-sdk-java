package com.intsig.docflow.http;

import com.intsig.docflow.config.DocflowConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证处理器
 *
 * <p>管理 API 认证凭证</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class AuthHandler {

    private String appId;
    private String secretCode;

    /**
     * 构造认证处理器
     *
     * @param appId      应用ID
     * @param secretCode 密钥
     */
    public AuthHandler(String appId, String secretCode) {
        this.appId = appId;
        this.secretCode = secretCode;
    }

    /**
     * 获取认证请求头
     *
     * @return 认证请求头
     */
    public Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(DocflowConstants.HEADER_APP_ID, appId);
        headers.put(DocflowConstants.HEADER_SECRET_CODE, secretCode);
        return headers;
    }

    /**
     * 更新认证凭证
     *
     * @param appId      新的应用ID
     * @param secretCode 新的密钥
     */
    public void setCredentials(String appId, String secretCode) {
        this.appId = appId;
        this.secretCode = secretCode;
    }

    public String getAppId() {
        return appId;
    }

    public String getSecretCode() {
        return secretCode;
    }
}
