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

import com.aiai.network.interceptor.CacheInterceptor
import com.aiai.network.interceptor.EncryptionInterceptor
import com.aiai.network.interceptor.HeaderInterceptor
import com.aiai.network.interceptor.LoggingInterceptor
import com.aiai.network.interceptor.MockInterceptor
import com.aiai.network.interceptor.NetworkMonitorInterceptor
import com.aiai.network.interceptor.RateLimitInterceptor
import com.aiai.network.interceptor.RetryInterceptor
import com.aiai.network.interceptor.SignatureInterceptor
import com.aiai.network.interceptor.TokenInterceptor
import com.aiai.network.dns.DnsResolver
import com.aiai.network.ssl.SSLManager
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * Retrofit构建器，负责创建和配置Retrofit实例。
 *
 * 支持动态baseUrl切换、多种Converter（Gson/Scalars）、协程适配器，
 * 并配置完整的OkHttp拦截器链、SSL校验、DNS优化等。
 *
 * 使用方式：
 * ```
 * val retrofit = RetrofitClient.getInstance().create()
 * val service = retrofit.create(ChatApiService::class.java)
 * ```
 */
object RetrofitClient {

    private const val CACHE_SIZE = 50L * 1024L * 1024L
    private const val CACHE_DIR_NAME = "http_cache"

    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    private val retrofitRef = AtomicReference<Retrofit?>(null)
    private val okHttpClientRef = AtomicReference<OkHttpClient?>(null)
    private var cacheDir: File? = null

    /**
     * 初始化RetrofitClient，设置缓存目录。
     *
     * @param cacheDir OkHttp缓存目录
     */
    fun init(cacheDir: File) {
        this.cacheDir = File(cacheDir, CACHE_DIR_NAME).apply { mkdirs() }
        retrofitRef.set(null)
        okHttpClientRef.set(null)
    }

    /**
     * 获取或创建OkHttpClient实例。
     *
     * @param config API配置
     * @return 配置好的OkHttpClient
     */
    fun getOkHttpClient(config: ApiConfig = ApiConfig.getInstance()): OkHttpClient {
        return okHttpClientRef.get() ?: buildOkHttpClient(config).also {
            okHttpClientRef.set(it)
        }
    }

    /**
     * 构建OkHttpClient，配置拦截器链和超时。
     *
     * 拦截器链执行顺序：
     * 1. RateLimitInterceptor - 限流
     * 2. NetworkMonitorInterceptor - 网络状态检测
     * 3. HeaderInterceptor - 公共Header
     * 4. TokenInterceptor - Token添加/刷新
     * 5. SignatureInterceptor - 请求签名
     * 6. EncryptionInterceptor - 请求加密
     * 7. LoggingInterceptor - 日志记录
     * 8. CacheInterceptor - 缓存策略
     * 9. RetryInterceptor - 重试
     * 10. MockInterceptor - Mock数据
     *
     * @param config API配置
     * @return OkHttpClient实例
     */
    private fun buildOkHttpClient(config: ApiConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.connectTimeoutMs, TimeUnit.MILLISECONDS)
            .readTimeout(config.readTimeoutMs, TimeUnit.MILLISECONDS)
            .writeTimeout(config.writeTimeoutMs, TimeUnit.MILLISECONDS)
            .callTimeout(config.callTimeoutMs, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .dns(DnsResolver())

        // SSL配置
        val sslManager = SSLManager(config)
        builder.sslSocketFactory(sslManager.sslSocketFactory, sslManager.trustManager)
        builder.hostnameVerifier(sslManager.hostnameVerifier)

        // 缓存
        if (config.enableCache && cacheDir != null) {
            builder.cache(Cache(cacheDir!!, CACHE_SIZE))
        }

        // 应用拦截器（按顺序执行）
        builder.addInterceptor(RateLimitInterceptor(config.maxRequestsPerSecond))
        builder.addInterceptor(NetworkMonitorInterceptor())
        builder.addInterceptor(HeaderInterceptor(config))
        builder.addInterceptor(TokenInterceptor(config))
        builder.addInterceptor(SignatureInterceptor(config))
        builder.addInterceptor(EncryptionInterceptor(config))
        builder.addInterceptor(LoggingInterceptor(config.isDebug))
        builder.addInterceptor(CacheInterceptor(config))
        builder.addInterceptor(RetryInterceptor(config))

        // 网络拦截器
        if (config.isDebug) {
            builder.addNetworkInterceptor(MockInterceptor())
        }

        return builder.build()
    }

    /**
     * 创建Retrofit实例。
     *
     * @param config API配置，默认使用单例配置
     * @return Retrofit实例
     */
    fun create(config: ApiConfig = ApiConfig.getInstance()): Retrofit {
        retrofitRef.get()?.let { cached ->
            if (cached.baseUrl().toString() == config.baseUrl) {
                return cached
            }
        }
        return buildRetrofit(config).also { retrofitRef.set(it) }
    }

    /**
     * 构建Retrofit实例，配置Converter工厂。
     *
     * @param config API配置
     * @return Retrofit实例
     */
    private fun buildRetrofit(config: ApiConfig): Retrofit {
        val okHttpClient = getOkHttpClient(config)
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * 动态更新baseUrl，重建Retrofit实例。
     *
     * @param newBaseUrl 新的基础URL
     */
    fun updateBaseUrl(newBaseUrl: String) {
        ApiConfig.getInstance().baseUrl = newBaseUrl
        retrofitRef.set(null)
        okHttpClientRef.set(null)
    }

    /**
     * 更新API密钥。
     *
     * @param newApiKey 新的API密钥
     */
    fun updateApiKey(newApiKey: String) {
        ApiConfig.getInstance().apiKey = newApiKey
    }

    /**
     * 刷新OkHttpClient，重建拦截器链。
     */
    fun refresh() {
        okHttpClientRef.set(null)
        retrofitRef.set(null)
    }

    /**
     * 获取Gson实例。
     *
     * @return Gson实例
     */
    fun getGson() = gson

    /**
     * 清除缓存。
     */
    fun clearCache() {
        okHttpClientRef.get()?.cache?.evictAll()
    }
}
