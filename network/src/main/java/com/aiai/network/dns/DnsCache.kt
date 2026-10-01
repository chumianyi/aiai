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
package com.aiai.network.dns

import java.util.concurrent.ConcurrentHashMap

/**
 * DNS缓存管理。
 *
 * 缓存域名解析结果，减少DNS查询次数。
 * 支持过期时间和最大缓存大小。
 */
class DnsCache {

    companion object {
        private const val DEFAULT_TTL_MS = 5 * 60 * 1000L
        private const val MAX_CACHE_SIZE = 100
    }

    private data class CacheEntry(
        val ips: List<String>,
        val expireAt: Long,
    )

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    /**
     * 获取缓存的DNS记录。
     *
     * @param hostname 域名
     * @return IP列表，如果未命中或过期返回null
     */
    fun get(hostname: String): List<String>? {
        val entry = cache[hostname] ?: return null
        if (System.currentTimeMillis() > entry.expireAt) {
            cache.remove(hostname)
            return null
        }
        return entry.ips
    }

    /**
     * 缓存DNS记录。
     *
     * @param hostname 域名
     * @param ips IP列表
     * @param ttlMs 过期时间（毫秒）
     */
    fun put(hostname: String, ips: List<String>, ttlMs: Long = DEFAULT_TTL_MS) {
        if (cache.size >= MAX_CACHE_SIZE) {
            evictOldest()
        }
        cache[hostname] = CacheEntry(
            ips = ips,
            expireAt = System.currentTimeMillis() + ttlMs,
        )
    }

    /**
     * 清除指定域名缓存。
     *
     * @param hostname 域名
     */
    fun remove(hostname: String) {
        cache.remove(hostname)
    }

    /**
     * 清除所有缓存。
     */
    fun clear() {
        cache.clear()
    }

    /**
     * 获取缓存大小。
     *
     * @return 缓存条目数
     */
    fun size(): Int = cache.size

    /**
     * 淘汰最旧的缓存条目。
     */
    private fun evictOldest() {
        val oldest = cache.entries
            .minByOrNull { it.value.expireAt }
            ?: return
        cache.remove(oldest.key)
    }
}
