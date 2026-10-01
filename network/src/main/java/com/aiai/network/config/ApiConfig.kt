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
package com.aiai.network.config

/**
 * API配置管理类，负责维护网络请求的全局配置参数。
 *
 * 该类使用单例模式，集中管理baseUrl、超时时间、重试次数等网络配置。
 * 支持运行时动态修改配置，例如切换环境（开发/测试/生产）或用户自定义API地址。
 *
 * @property baseUrl 基础URL，所有API请求的根地址
 * @property connectTimeoutMs 连接超时时间（毫秒），默认15秒
 * @property readTimeoutMs 读取超时时间（毫秒），默认30秒
 * @property writeTimeoutMs 写入超时时间（毫秒），默认30秒
 * @property retryMaxAttempts 最大重试次数，默认3次
 * @property retryBaseDelayMs 重试基础延迟（毫秒），用于指数退避计算
 * @property callTimeoutMs 整个调用超时时间（毫秒），默认为0表示不限制
 * @property isDebug 是否为调试模式，调试模式下启用详细日志
 * @property apiKey API密钥，用于Authorization头
 * @property appVersion 应用版本号，用于公共Header
 * @property deviceId 设备唯一标识
 * @property language 设备语言
 * @property enableCache 是否启用缓存
 * @property cacheMaxAgeSeconds 缓存最大存活时间（秒）
 * @property enableHttps  是否启用HTTPS
 */
data class ApiConfig(
    var baseUrl: String = "https://api.aiai.com/",
    var connectTimeoutMs: Long = 15_000L,
    var readTimeoutMs: Long = 30_000L,
    var writeTimeoutMs: Long = 30_000L,
    var retryMaxAttempts: Int = 3,
    var retryBaseDelayMs: Long = 1_000L,
    var callTimeoutMs: Long = 0L,
    var isDebug: Boolean = false,
    var apiKey: String = "",
    var appVersion: String = "1.0.0",
    var deviceId: String = "",
    var language: String = "zh-CN",
    var enableCache: Boolean = true,
    var cacheMaxAgeSeconds: Long = 300L,
    var enableHttps: Boolean = true,
    var enableEncryption: Boolean = false,
    var encryptionKey: String = "",
    var encryptionIv: String = "",
    var signEnabled: Boolean = false,
    var signSecret: String = "",
    var tokenRefreshThresholdMs: Long = 5 * 60 * 1000L,
    var maxRequestsPerSecond: Int = 10,
    var webSocketUrl: String = "wss://ws.aiai.com/",
    var pingIntervalMs: Long = 30_000L,
    var maxReconnectAttempts: Int = 10,
    var diskCacheMaxSize: Long = 50L * 1024L * 1024L,
    var memoryCacheMaxSize: Long = 10L * 1024L * 1024L,
) {

    companion object {
        /** 开发环境baseUrl */
        const val BASE_URL_DEV = "https://dev-api.aiai.com/"

        /** 测试环境baseUrl */
        const val BASE_URL_STAGING = "https://staging-api.aiai.com/"

        /** 生产环境baseUrl */
        const val BASE_URL_PROD = "https://api.aiai.com/"

        /** 默认连接超时 */
        const val DEFAULT_CONNECT_TIMEOUT = 15_000L

        /** 默认读取超时 */
        const val DEFAULT_READ_TIMEOUT = 30_000L

        /** 默认写入超时 */
        const val DEFAULT_WRITE_TIMEOUT = 30_000L

        /** 默认重试次数 */
        const val DEFAULT_RETRY_ATTEMPTS = 3

        /** 默认重试基础延迟 */
        const val DEFAULT_RETRY_BASE_DELAY = 1_000L

        /** 缓存最大存活时间（5分钟） */
        const val CACHE_MAX_AGE_FIVE_MINUTES = 300L

        /** 缓存最大存活时间（1小时） */
        const val CACHE_MAX_AGE_ONE_HOUR = 3_600L

        /** 缓存最大存活时间（1天） */
        const val CACHE_MAX_AGE_ONE_DAY = 86_400L

        /** 默认磁盘缓存大小（50MB） */
        const val DEFAULT_DISK_CACHE_SIZE = 50L * 1024L * 1024L

        /** 默认内存缓存大小（10MB） */
        const val DEFAULT_MEMORY_CACHE_SIZE = 10L * 1024L * 1024L

        /** 默认心跳间隔（30秒） */
        const val DEFAULT_PING_INTERVAL = 30_000L

        /** 默认最大重连次数 */
        const val DEFAULT_MAX_RECONNECT = 10

        /** 单例实例 */
        @Volatile
        private var INSTANCE: ApiConfig? = null

        /**
         * 获取ApiConfig单例实例。
         *
         * @return ApiConfig单例对象
         */
        @JvmStatic
        fun getInstance(): ApiConfig {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiConfig().also { INSTANCE = it }
            }
        }

        /**
         * 初始化ApiConfig单例。
         *
         * @param config 自定义配置实例
         */
        fun init(config: ApiConfig) {
            synchronized(this) {
                INSTANCE = config
            }
        }

        /**
         * 重置为默认配置。
         */
        fun reset() {
            synchronized(this) {
                INSTANCE = ApiConfig()
            }
        }
    }

    /**
     * 构建User-Agent字符串。
     *
     * @return 格式化的User-Agent
     */
    fun buildUserAgent(): String {
        return "AiAi/$appVersion (Android; $deviceId; $language)"
    }

    /**
     * 切换到开发环境。
     */
    fun useDevEnvironment() {
        baseUrl = BASE_URL_DEV
        isDebug = true
    }

    /**
     * 切换到测试环境。
     */
    fun useStagingEnvironment() {
        baseUrl = BASE_URL_STAGING
        isDebug = true
    }

    /**
     * 切换到生产环境。
     */
    fun useProdEnvironment() {
        baseUrl = BASE_URL_PROD
        isDebug = false
    }

    /**
     * 验证配置是否有效。
     *
     * @return true如果配置有效
     */
    fun isValid(): Boolean {
        if (baseUrl.isBlank()) return false
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) return false
        if (connectTimeoutMs <= 0) return false
        if (readTimeoutMs <= 0) return false
        if (writeTimeoutMs <= 0) return false
        if (retryMaxAttempts < 0) return false
        return true
    }

    /**
     * 计算指数退避延迟时间。
     *
     * @param attempt 当前重试次数（从1开始）
     * @return 延迟毫秒数
     */
    fun calculateBackoffDelay(attempt: Int): Long {
        require(attempt >= 1) { "attempt must be >= 1" }
        val delay = retryBaseDelayMs * (1L shl (attempt - 1))
        return delay.coerceAtMost(30_000L)
    }

    /**
     * 创建一个新的配置构建器。
     *
     * @return ApiConfigBuilder实例
     */
    fun toBuilder(): ApiConfigBuilder {
        return ApiConfigBuilder(this)
    }

    /**
     * 配置是否需要签名。
     *
     * @return true如果签名已启用且密钥非空
     */
    fun isSignNeeded(): Boolean {
        return signEnabled && signSecret.isNotBlank()
    }

    /**
     * 配置是否需要加密。
     *
     * @return true如果加密已启用且密钥非空
     */
    fun isEncryptionNeeded(): Boolean {
        return enableEncryption && encryptionKey.isNotBlank()
    }

    override fun toString(): String {
        return "ApiConfig(baseUrl='$baseUrl', connectTimeoutMs=$connectTimeoutMs, " +
            "readTimeoutMs=$readTimeoutMs, writeTimeoutMs=$writeTimeoutMs, " +
            "retryMaxAttempts=$retryMaxAttempts, isDebug=$isDebug, appVersion='$appVersion')"
    }
}

/**
 * ApiConfig构建器，支持链式调用。
 *
 * @property config 基础配置
 */
class ApiConfigBuilder(private val config: ApiConfig = ApiConfig()) {

    /** 设置baseUrl */
    fun baseUrl(url: String) = apply { config.baseUrl = url }

    /** 设置连接超时 */
    fun connectTimeout(ms: Long) = apply { config.connectTimeoutMs = ms }

    /** 设置读取超时 */
    fun readTimeout(ms: Long) = apply { config.readTimeoutMs = ms }

    /** 设置写入超时 */
    fun writeTimeout(ms: Long) = apply { config.writeTimeoutMs = ms }

    /** 设置重试次数 */
    fun retryAttempts(count: Int) = apply { config.retryMaxAttempts = count }

    /** 设置重试基础延迟 */
    fun retryBaseDelay(ms: Long) = apply { config.retryBaseDelayMs = ms }

    /** 设置调试模式 */
    fun debug(enabled: Boolean) = apply { config.isDebug = enabled }

    /** 设置API密钥 */
    fun apiKey(key: String) = apply { config.apiKey = key }

    /** 设置应用版本 */
    fun appVersion(version: String) = apply { config.appVersion = version }

    /** 设置设备ID */
    fun deviceId(id: String) = apply { config.deviceId = id }

    /** 设置语言 */
    fun language(lang: String) = apply { config.language = lang }

    /** 启用缓存 */
    fun enableCache(enabled: Boolean) = apply { config.enableCache = enabled }

    /** 设置缓存最大存活时间 */
    fun cacheMaxAge(seconds: Long) = apply { config.cacheMaxAgeSeconds = seconds }

    /** 启用HTTPS */
    fun enableHttps(enabled: Boolean) = apply { config.enableHttps = enabled }

    /** 启用加密 */
    fun enableEncryption(enabled: Boolean, key: String, iv: String) = apply {
        config.enableEncryption = enabled
        config.encryptionKey = key
        config.encryptionIv = iv
    }

    /** 启用签名 */
    fun enableSign(enabled: Boolean, secret: String) = apply {
        config.signEnabled = enabled
        config.signSecret = secret
    }

    /** 设置WebSocket地址 */
    fun webSocketUrl(url: String) = apply { config.webSocketUrl = url }

    /** 设置心跳间隔 */
    fun pingInterval(ms: Long) = apply { config.pingIntervalMs = ms }

    /** 设置最大重连次数 */
    fun maxReconnectAttempts(count: Int) = apply { config.maxReconnectAttempts = count }

    /** 构建ApiConfig */
    fun build(): ApiConfig = config
}
