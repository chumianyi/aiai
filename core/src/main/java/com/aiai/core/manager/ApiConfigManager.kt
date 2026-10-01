/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.core.manager

import android.util.Log

/**
 * API 配置管理器。
 *
 * 提供 API 地址、密钥、超时等配置管理功能。
 */
object ApiConfigManager {

    private const val TAG = "ApiConfigManager"

    /**
     * API 配置数据类。
     *
     * @param baseUrl 基础URL
     * @param apiKey API密钥
     * @param timeout 超时时间（毫秒）
     * @param maxRetries 最大重试次数
     * @param enableLog 是否启用日志
     */
    data class ApiConfig(
        var baseUrl: String = "https://api.openai.com",
        var apiKey: String = "",
        var timeout: Long = 30000L,
        var maxRetries: Int = 3,
        var enableLog: Boolean = true
    )

    private var config = ApiConfig()
    private var onConfigChangedListener: (() -> Unit)? = null

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "ApiConfigManager initialized")
    }

    /**
     * 获取当前配置。
     *
     * @return API 配置
     */
    fun getConfig(): ApiConfig = config.copy()

    /**
     * 更新配置。
     *
     * @param newConfig 新配置
     */
    fun updateConfig(newConfig: ApiConfig) {
        config = newConfig
        Log.d(TAG, "API config updated")
        onConfigChangedListener?.invoke()
    }

    /**
     * 设置基础 URL。
     *
     * @param baseUrl 基础URL
     */
    fun setBaseUrl(baseUrl: String) {
        config.baseUrl = baseUrl
        Log.d(TAG, "Base URL set: $baseUrl")
        onConfigChangedListener?.invoke()
    }

    /**
     * 获取基础 URL。
     *
     * @return 基础URL
     */
    fun getBaseUrl(): String = config.baseUrl

    /**
     * 设置 API 密钥。
     *
     * @param apiKey API密钥
     */
    fun setApiKey(apiKey: String) {
        config.apiKey = apiKey
        Log.d(TAG, "API key set: ${apiKey.take(5)}...")
        onConfigChangedListener?.invoke()
    }

    /**
     * 获取 API 密钥。
     *
     * @return API密钥
     */
    fun getApiKey(): String = config.apiKey

    /**
     * 设置超时时间。
     *
     * @param timeout 超时时间（毫秒）
     */
    fun setTimeout(timeout: Long) {
        config.timeout = timeout
        Log.d(TAG, "Timeout set: $timeout ms")
        onConfigChangedListener?.invoke()
    }

    /**
     * 获取超时时间。
     *
     * @return 超时时间（毫秒）
     */
    fun getTimeout(): Long = config.timeout

    /**
     * 是否有有效的 API 密钥。
     *
     * @return 是否有效
     */
    fun hasValidApiKey(): Boolean {
        return config.apiKey.isNotBlank()
    }

    /**
     * 设置配置变化监听。
     *
     * @param listener 监听器
     */
    fun setOnConfigChangedListener(listener: () -> Unit) {
        onConfigChangedListener = listener
    }

    /**
     * 重置为默认配置。
     */
    fun resetToDefault() {
        config = ApiConfig()
        Log.d(TAG, "API config reset to default")
        onConfigChangedListener?.invoke()
    }
}
