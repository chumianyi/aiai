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
 * 内存缓存。
 *
 * 基于ConcurrentHashMap实现的内存缓存，支持大小限制。
 */
class MemoryCache(
    private val maxSize: Long,
) {

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private var currentSize: Long = 0L

    private data class CacheEntry(
        val value: String,
        val size: Long,
        val expireAt: Long,
    )

    fun get(key: String): String? {
        val entry = cache[key] ?: return null
        if (System.currentTimeMillis() > entry.expireAt) {
            remove(key)
            return null
        }
        return entry.value
    }

    fun put(key: String, value: String, ttlMs: Long = 5 * 60 * 1000L) {
        remove(key)
        val size = value.toByteArray().size.toLong()
        while (currentSize + size > maxSize && cache.isNotEmpty()) {
            evictOldest()
        }
        cache[key] = CacheEntry(value, size, System.currentTimeMillis() + ttlMs)
        currentSize += size
    }

    fun remove(key: String) {
        cache.remove(key)?.let { currentSize -= it.size }
    }

    fun clear() {
        cache.clear()
        currentSize = 0L
    }

    fun size(): Int = cache.size

    private fun evictOldest() {
        val oldest = cache.entries.minByOrNull { it.value.expireAt } ?: return
        remove(oldest.key)
    }
}
