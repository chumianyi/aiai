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

import java.util.concurrent.ConcurrentHashMap

/**
 * 缓存管理器。
 *
 * 管理内存缓存和磁盘缓存，支持LRU淘汰、过期、大小限制。
 */
class CacheManager(
    private val maxMemorySize: Long = 10L * 1024L * 1024L,
    private val maxDiskSize: Long = 50L * 1024L * 1024L,
) {

    private val memoryCache = MemoryCache(maxMemorySize)
    private val diskCache = DiskCache(maxDiskSize)

    /**
     * 获取缓存数据。
     */
    fun get(key: String): String? {
        memoryCache.get(key)?.let { return it }
        diskCache.get(key)?.let {
            memoryCache.put(key, it)
            return it
        }
        return null
    }

    /**
     * 存入缓存。
     */
    fun put(key: String, value: String) {
        memoryCache.put(key, value)
        diskCache.put(key, value)
    }

    /**
     * 移除缓存。
     */
    fun remove(key: String) {
        memoryCache.remove(key)
        diskCache.remove(key)
    }

    /**
     * 清除所有缓存。
     */
    fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }

    /**
     * 获取缓存大小。
     */
    fun size(): Int = memoryCache.size() + diskCache.size()
}
