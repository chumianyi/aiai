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

import android.content.Context
import android.util.Log
import java.io.File

/**
 * 缓存管理器。
 *
 * 提供内存缓存、磁盘缓存、缓存清理等功能。
 */
object CacheManager {

    private const val TAG = "CacheManager"
    private const val DEFAULT_MEMORY_CACHE_SIZE = 10 * 1024 * 1024L // 10MB

    private var context: Context? = null
    private val memoryCache = LinkedHashMap<String, Any>(0, 0.75f, true)
    private var maxMemoryCacheSize = DEFAULT_MEMORY_CACHE_SIZE

    /**
     * 初始化。
     *
     * @param context 上下文
     * @param maxMemorySize 最大内存缓存大小
     */
    fun init(context: Context, maxMemorySize: Long = DEFAULT_MEMORY_CACHE_SIZE) {
        this.context = context.applicationContext
        this.maxMemoryCacheSize = maxMemorySize
        Log.d(TAG, "CacheManager initialized, max memory: $maxMemorySize")
    }

    /**
     * 保存到内存缓存。
     *
     * @param key 键
     * @param value 值
     */
    fun put(key: String, value: Any) {
        memoryCache[key] = value
        trimMemoryCache()
    }

    /**
     * 从内存缓存获取。
     *
     * @param key 键
     * @return 值
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? {
        return memoryCache[key] as? T
    }

    /**
     * 移除内存缓存。
     *
     * @param key 键
     */
    fun remove(key: String) {
        memoryCache.remove(key)
    }

    /**
     * 清除内存缓存。
     */
    fun clearMemory() {
        memoryCache.clear()
        Log.d(TAG, "Memory cache cleared")
    }

    /**
     * 裁剪内存缓存。
     */
    private fun trimMemoryCache() {
        while (getCacheSize() > maxMemoryCacheSize && memoryCache.isNotEmpty()) {
            val eldestKey = memoryCache.keys.firstOrNull()
            eldestKey?.let {
                memoryCache.remove(it)
                Log.d(TAG, "Evicted: $it")
            }
        }
    }

    /**
     * 获取缓存大小（估算）。
     */
    private fun getCacheSize(): Long {
        // 简单估算，实际应该测量对象大小
        return memoryCache.size * 1024L
    }

    /**
     * 获取缓存条目数。
     */
    fun size(): Int = memoryCache.size

    /**
     * 清除磁盘缓存。
     */
    fun clearDisk() {
        val ctx = context ?: return
        try {
            ctx.cacheDir.deleteRecursively()
            ctx.cacheDir.mkdirs()
            Log.d(TAG, "Disk cache cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Clear disk cache failed", e)
        }
    }

    /**
     * 获取磁盘缓存大小。
     */
    fun getDiskCacheSize(): Long {
        val ctx = context ?: return 0L
        return getDirSize(ctx.cacheDir)
    }

    /**
     * 递归计算目录大小。
     */
    private fun getDirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getDirSize(file) else file.length()
        }
        return size
    }

    /**
     * 格式化缓存大小。
     */
    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "${bytes}B"
            bytes < 1024 * 1024 -> String.format("%.2fKB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.2fMB", bytes / (1024.0 * 1024.0))
            else -> String.format("%.2fGB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }
}
