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
package com.aiai.network.util

import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 请求缓存。
 *
 * 缓存网络请求结果，避免重复请求。
 *
 * @param maxAgeMs 缓存最大存活时间
 */
class RequestCache(
    private val maxAgeMs: Long = 5 * 60 * 1000L,
) {

    companion object {
        private const val TAG = "RequestCache"
    }

    private data class CacheEntry(
        val data: String,
        val expireAt: Long,
    )

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val cleanupExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "request-cache-cleanup").apply { isDaemon = true }
    }

    init {
        // 定期清理过期缓存
        cleanupExecutor.scheduleAtFixedRate({
            try {
                cleanup()
            } catch (e: Exception) {
                Log.w(TAG, "Cleanup failed: ${e.message}")
            }
        }, 1, 1, TimeUnit.MINUTES)
    }

    /**
     * 获取缓存。
     *
     * @param key 缓存键（通常是URL）
     * @return 缓存数据，未命中或过期返回null
     */
    fun get(key: String): String? {
        val entry = cache[key] ?: return null
        if (System.currentTimeMillis() > entry.expireAt) {
            cache.remove(key)
            return null
        }
        return entry.data
    }

    /**
     * 存入缓存。
     *
     * @param key 缓存键
     * @param data 缓存数据
     */
    fun put(key: String, data: String) {
        cache[key] = CacheEntry(
            data = data,
            expireAt = System.currentTimeMillis() + maxAgeMs,
        )
    }

    /**
     * 移除缓存。
     *
     * @param key 缓存键
     */
    fun remove(key: String) {
        cache.remove(key)
    }

    /**
     * 清除所有缓存。
     */
    fun clear() {
        cache.clear()
    }

    /**
     * 清理过期缓存。
     */
    private fun cleanup() {
        val now = System.currentTimeMillis()
        val iterator = cache.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now > entry.value.expireAt) {
                iterator.remove()
            }
        }
    }

    /**
     * 获取缓存大小。
     */
    fun size(): Int = cache.size

    /**
     * 关闭缓存，释放资源。
     */
    fun shutdown() {
        cleanupExecutor.shutdown()
        cache.clear()
    }
}
