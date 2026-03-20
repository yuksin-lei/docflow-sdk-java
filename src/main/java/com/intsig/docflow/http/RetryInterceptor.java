package com.intsig.docflow.http;

import com.intsig.docflow.config.DocflowConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * 重试拦截器
 *
 * <p>实现自动重试逻辑，支持指定状态码和HTTP方法的重试</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class RetryInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);

    private final DocflowConfig config;

    public RetryInterceptor(DocflowConfig config) {
        this.config = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;

        int maxAttempts = config.getMaxRetries() + 1; // 初始请求 + 重试次数
        Set<Integer> retryStatusCodes = config.getRetryStatusCodes();
        Set<String> retryMethods = config.getRetryMethods();
        String method = request.method();

        // 检查是否允许重试该 HTTP 方法
        boolean methodAllowsRetry = retryMethods.contains(method);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // 尝试执行请求
                response = chain.proceed(request);

                // 检查是否需要重试
                int statusCode = response.code();
                boolean shouldRetry = methodAllowsRetry
                        && retryStatusCodes.contains(statusCode)
                        && attempt < maxAttempts;

                if (shouldRetry) {
                    logger.warn("请求失败 [{}], 状态码: {}, 尝试重试 ({}/{})",
                            request.url(), statusCode, attempt, maxAttempts - 1);

                    // 关闭响应体以释放连接
                    if (response.body() != null) {
                        response.body().close();
                    }

                    // 计算重试间隔并等待
                    long waitTime = calculateWaitTime(attempt);
                    Thread.sleep(waitTime);

                    continue;
                }

                // 请求成功或不需要重试
                if (attempt > 1) {
                    logger.info("重试成功 [{}], 尝试次数: {}, 状态码: {}",
                            request.url(), attempt, statusCode);
                }

                return response;

            } catch (IOException e) {
                lastException = e;

                // 对于网络异常，如果允许重试方法且未达到最大重试次数，则重试
                if (methodAllowsRetry && attempt < maxAttempts) {
                    logger.warn("网络请求失败 [{}], 尝试重试 ({}/{}): {}",
                            request.url(), attempt, maxAttempts - 1, e.getMessage());

                    // 计算重试间隔并等待
                    try {
                        long waitTime = calculateWaitTime(attempt);
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("重试被中断", ie);
                    }

                    continue;
                }

                // 达到最大重试次数，抛出异常
                throw e;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("重试被中断", e);
            }
        }

        // 如果所有重试都失败了
        if (lastException != null) {
            logger.error("请求失败 [{}], 已达到最大重试次数 {}", request.url(), config.getMaxRetries());
            throw lastException;
        }

        return response;
    }

    /**
     * 计算等待时间（指数退避）
     *
     * @param attempt 当前尝试次数
     * @return 等待时间（毫秒）
     */
    private long calculateWaitTime(int attempt) {
        // 计算公式: backoff_factor * (2 ** (attempt - 1)) * 1000
        // 例如：backoff_factor=1 时，重试间隔为 0s, 2s, 4s, 8s...
        double waitSeconds = config.getRetryBackoffFactor() * Math.pow(2, attempt - 1);
        return (long) (waitSeconds * 1000);
    }
}
