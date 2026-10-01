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

import retrofit2.Retrofit
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * 服务创建工厂，负责创建和缓存Retrofit API Service实例。
 *
 * 使用ConcurrentHashMap缓存已创建的Service实例，避免重复创建。
 * 每个Service接口只创建一次，后续调用直接从缓存获取。
 *
 * 使用方式：
 * ```
 * val chatApi = ServiceCreator.create(ChatApiService::class.java)
 * ```
 */
object ServiceCreator {

    /** Service缓存，key为Service接口Class，value为弱引用的Service实例 */
    private val serviceCache = ConcurrentHashMap<Class<*>, WeakReference<Any>>()

    /** Retrofit实例引用 */
    private var retrofit: Retrofit? = null

    /**
     * 初始化ServiceCreator，设置Retrofit实例。
     *
     * @param retrofit Retrofit实例
     */
    fun init(retrofit: Retrofit) {
        this.retrofit = retrofit
        serviceCache.clear()
    }

    /**
     * 创建API Service实例，带缓存。
     *
     * 如果缓存中已有该Service的实例且未被GC回收，直接返回缓存实例。
     * 否则通过Retrofit创建新实例并缓存。
     *
     * @param T Service接口类型
     * @param serviceClass Service接口Class对象
     * @return Service实例
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(serviceClass: Class<T>): T {
        // 先从缓存获取
        serviceCache[serviceClass]?.get()?.let {
            return it as T
        }
        // 创建新实例
        val retrofit = retrofit ?: RetrofitClient.create().also { this.retrofit = it }
        val service = retrofit.create(serviceClass)
        serviceCache[serviceClass] = WeakReference(service)
        return service
    }

    /**
     * 创建API Service实例（Kotlin reified泛型版本）。
     *
     * @param T Service接口类型
     * @return Service实例
     */
    inline fun <reified T : Any> create(): T = create(T::class.java)

    /**
     * 使用指定baseUrl创建临时Service实例，不缓存。
     *
     * 适用于需要临时连接不同服务器的场景。
     *
     * @param T Service接口类型
     * @param baseUrl 临时baseUrl
     * @param serviceClass Service接口Class对象
     * @return Service实例
     */
    fun <T : Any> createWithBaseUrl(serviceClass: Class<T>, baseUrl: String): T {
        val originalConfig = ApiConfig.getInstance()
        val tempConfig = originalConfig.copy(baseUrl = baseUrl)
        val tempRetrofit = RetrofitClient.create(tempConfig)
        return tempRetrofit.create(serviceClass)
    }

    /**
     * 清除指定Service的缓存。
     *
     * @param serviceClass 要清除的Service接口Class
     */
    fun evict(serviceClass: Class<*>) {
        serviceCache.remove(serviceClass)
    }

    /**
     * 清除所有Service缓存。
     */
    fun evictAll() {
        serviceCache.clear()
    }

    /**
     * 获取当前缓存的Service数量。
     *
     * @return 缓存中的Service数量
     */
    fun cachedCount(): Int = serviceCache.size

    /**
     * 检查指定Service是否已缓存。
     *
     * @param serviceClass Service接口Class
     * @return true如果已缓存且实例存活
     */
    fun isCached(serviceClass: Class<*>): Boolean {
        return serviceCache[serviceClass]?.get() != null
    }
}
