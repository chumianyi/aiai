/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.network.interceptor

import com.aiai.network.config.ApiConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Token拦截器，自动添加Authorization头并处理Token刷新。
 *
 * 功能：
 * - 自动在请求头中添加Bearer Token
 * - 检测401响应，触发Token刷新流程
 * - Token刷新成功后重试原请求
 * - Token刷新失败时清除本地Token
 *
 * @property config API配置
 */
class TokenInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_BEARER = "Bearer "
        private const val HTTP_UNAUTHORIZED = 401
    }

    @Volatile
    private var accessToken: String = ""

    @Volatile
    private var refreshToken: String = ""

    @Volatile
    private var tokenExpireAt: Long = 0L

    /** Token刷新回调接口 */
    var onTokenRefreshListener: (suspend () -> Boolean)? = null

    /**
     * 设置Token。
     *
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param expireAt 过期时间戳（毫秒）
     */
    fun setToken(accessToken: String, refreshToken: String, expireAt: Long) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.tokenExpireAt = expireAt
    }

    /**
     * 清除Token。
     */
    fun clearToken() {
        accessToken = ""
        refreshToken = ""
        tokenExpireAt = 0L
    }

    /**
     * 检查Token是否即将过期。
     *
     * @return true如果Token即将过期（剩余时间小于刷新阈值）
     */
    private fun isTokenExpiring(): Boolean {
        if (accessToken.isBlank()) return true
        return System.currentTimeMillis() + config.tokenRefreshThresholdMs >= tokenExpireAt
    }

    /**
     * 拦截请求，添加Authorization头。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 构建请求，添加Token头
        val authenticatedRequest = if (accessToken.isNotBlank()) {
            originalRequest.newBuilder()
                .header(HEADER_AUTHORIZATION, HEADER_BEARER + accessToken)
                .build()
        } else {
            originalRequest
        }

        // 执行请求
        var response = chain.proceed(authenticatedRequest)

        // 处理401未授权
        if (response.code == HTTP_UNAUTHORIZED && refreshToken.isNotBlank()) {
            // 关闭旧响应体
            response.close()

            // 尝试刷新Token
            val refreshSuccess = refreshTokenAsync()
            if (refreshSuccess && accessToken.isNotBlank()) {
                // 刷新成功，重试请求
                val newRequest = originalRequest.newBuilder()
                    .header(HEADER_AUTHORIZATION, HEADER_BEARER + accessToken)
                    .build()
                response = chain.proceed(newRequest)
            } else {
                // 刷新失败，清除Token
                clearToken()
            }
        }

        return response
    }

    /**
     * 异步刷新Token。
     *
     * @return true如果刷新成功
     */
    private fun refreshTokenAsync(): Boolean {
        return try {
            onTokenRefreshListener?.invoke() ?: false
        } catch (e: Exception) {
            false
        }
    }
}
