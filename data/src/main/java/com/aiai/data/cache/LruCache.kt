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
package com.aiai.data.cache

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * LRU缓存实现。
 *
 * 基于LinkedHashMap实现LRU淘汰策略。
 *
 * @property maxSize 最大缓存条目数
 */
class LruCache<K, V>(
    private val maxSize: Int = 100,
) {

    companion object {
        private const val TAG = "LruCache"
    }

    private val map = linkedMapOf<K, CacheEntry<V>>()
    private val currentSize = AtomicLong(0)

    private data class CacheEntry<T>(
        val data: T,
        val expireAt: Long,
    )

    /**
     * 获取缓存值。
     *
     * @param key 缓存键
     * @return 缓存值，未命中或过期返回null
     */
    @Synchronized
    fun get(key: K): V? {
        val entry = map.remove(key) ?: return null
        if (System.currentTimeMillis() > entry.expireAt) {
            Log.d(TAG, "Cache expired for key: $key")
            return null
        }
        // 移到末尾（最近使用）
        map[key] = entry
        return entry.data
    }

    /**
     * 存入缓存。
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param ttlMs 存活时间（毫秒）
     */
    @Synchronized
    fun put(key: K, value: V, ttlMs: Long = 5 * 60 * 1000) {
        // 如果已存在，先移除
        map.remove(key)
        // 如果超出大小，移除最旧的
        while (map.size >= maxSize) {
            val oldestKey = map.keys.firstOrNull() ?: break
            map.remove(oldestKey)
            Log.d(TAG, "Evicted oldest key: $oldestKey")
        }
        map[key] = CacheEntry(
            data = value,
            expireAt = System.currentTimeMillis() + ttlMs,
        )
    }

    /**
     * 移除指定键的缓存。
     */
    @Synchronized
    fun remove(key: K) {
        map.remove(key)
    }

    /**
     * 清除所有缓存。
     */
    @Synchronized
    fun clear() {
        map.clear()
    }

    /**
     * 获取当前缓存大小。
     */
    fun size(): Int = map.size

    /**
     * 检查是否包含键。
     */
    @Synchronized
    fun contains(key: K): Boolean {
        return map.containsKey(key)
    }

    /**
     * 获取所有键。
     */
    @Synchronized
    fun keys(): Set<K> = map.keys.toSet()

    /**
     * 清理过期条目。
     */
    @Synchronized
    fun cleanup() {
        val now = System.currentTimeMillis()
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now > entry.value.expireAt) {
                iterator.remove()
            }
        }
    }

    /**
     * 获取缓存统计信息。
     */
    fun getStats(): Map<String, Any> {
        return mapOf(
            "maxSize" to maxSize,
            "currentSize" to size(),
            "utilization" to "${size() * 100 / maxSize}%",
        )
    }
}
