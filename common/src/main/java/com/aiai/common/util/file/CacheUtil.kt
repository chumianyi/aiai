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
package com.aiai.common.util.file

import android.content.Context
import java.io.File

/**
 * 缓存管理工具类。
 *
 * 管理内存缓存和磁盘缓存，支持过期清理、大小限制。
 */
object CacheUtil {

    private const val MEMORY_CACHE_SIZE = 512 * 1024L // 512KB

    /** 内存缓存。 */
    private val memoryCache = linkedMapOf<String, Long>()

    /** 写入内存缓存。 */
    fun putMemory(key: String, value: Long) {
        memoryCache[key] = value
        trimMemory()
    }

    /** 读取内存缓存。 */
    fun getMemory(key: String): Long? = memoryCache[key]

    /** 清理内存缓存。 */
    fun clearMemory() = memoryCache.clear()

    /** 裁剪内存缓存到上限。 */
    private fun trimMemory() {
        while (memoryCache.size > MEMORY_CACHE_SIZE) {
            memoryCache.remove(memoryCache.keys.first())
        }
    }

    /** 清除应用磁盘缓存。 */
    fun clearDiskCache(context: Context) {
        FileUtil.getCacheDir(context).deleteRecursively()
        FileUtil.getExternalCacheDir(context)?.deleteRecursively()
    }

    /** 获取磁盘缓存大小。 */
    fun getDiskCacheSize(context: Context): Long {
        var size = FileUtil.dirSize(FileUtil.getCacheDir(context))
        FileUtil.getExternalCacheDir(context)?.let { size += FileUtil.dirSize(it) }
        return size
    }
}
