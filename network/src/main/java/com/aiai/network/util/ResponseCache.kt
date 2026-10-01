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
package com.aiai.network.util

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * 响应缓存。
 *
 * 提供 LRU 缓存、过期机制、大小限制等功能。
 *
 * @param maxSize 最大缓存条目数
 * @param defaultTtl 默认过期时间（毫秒）
 */
class ResponseCache(
    private val maxSize: Int = 100,
    private val defaultTtl: Long = 5 * 60 * 1000L
) {

    companion object {
        private const val TAG = "ResponseCache"
    }

    private data class CacheEntry(
        val data: Any,
        val expireAt: Long,
        val createAt: Long = System.currentTimeMillis()
    )

    private val cache = LinkedHashMap<String, CacheEntry>(maxSize, 0.75f, true)

    /**
     * 放入缓存。
     *
     * @param key 缓存键
     * @param data 缓存数据
     * @param ttl 过期时间（毫秒）
     */
    @Synchronized
    fun put(key: String, data: Any, ttl: Long = defaultTtl) {
        val expireAt = System.currentTimeMillis() + ttl
        cache[key] = CacheEntry(data, expireAt)

        if (cache.size > maxSize) {
            // 移除最久未使用的条目
            val oldestKey = cache.keys.first()
            cache.remove(oldestKey)
            Log.d(TAG, "Evicted oldest cache: $oldestKey")
        }
    }

    /**
     * 获取缓存。
     *
     * @param key 缓存键
     * @return 缓存数据，不存在或已过期返回 null
     */
    @Synchronized
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? {
        val entry = cache[key] ?: return null

        return if (System.currentTimeMillis() > entry.expireAt) {
            cache.remove(key)
            Log.d(TAG, "Cache expired: $key")
            null
        } else {
            entry.data as? T
        }
    }

    /**
     * 检查缓存是否存在且未过期。
     *
     * @param key 缓存键
     * @return true 表示存在
     */
    @Synchronized
    fun contains(key: String): Boolean {
        val entry = cache[key] ?: return false
        return System.currentTimeMillis() <= entry.expireAt
    }

    /**
     * 移除指定缓存。
     *
     * @param key 缓存键
     */
    @Synchronized
    fun remove(key: String) {
        cache.remove(key)
    }

    /**
     * 清除所有缓存。
     */
    @Synchronized
    fun clear() {
        cache.clear()
        Log.d(TAG, "Cache cleared")
    }

    /**
     * 获取缓存大小。
     */
    @Synchronized
    fun size(): Int = cache.size

    /**
     * 清理过期缓存。
     */
    @Synchronized
    fun cleanExpired() {
        val now = System.currentTimeMillis()
        val iterator = cache.entries.iterator()
        var cleaned = 0
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now > entry.value.expireAt) {
                iterator.remove()
                cleaned++
            }
        }
        if (cleaned > 0) {
            Log.d(TAG, "Cleaned $cleaned expired entries")
        }
    }

    /**
     * 获取缓存命中率。
     */
    var hitCount: Int = 0
        private set

    var missCount: Int = 0
        private set

    /**
     * 命中率百分比。
     */
    fun hitRate(): Float {
        val total = hitCount + missCount
        return if (total == 0) 0f else hitCount.toFloat() / total * 100
    }
}
