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
package com.aiai.common.util.storage

import android.content.Context
import java.io.File

/**
 * 缓存管理工具类。
 *
 * 管理磁盘缓存目录，支持按时间过期清理。
 */
object CacheManager {

    private const val CACHE_SUBDIR = "aiai_cache"

    /** 获取缓存目录。 */
    fun getCacheDir(context: Context): File {
        return File(context.cacheDir, CACHE_SUBDIR).apply { mkdirs() }
    }

    /** 清理指定天数前的缓存。 */
    fun cleanOldCache(context: Context, days: Int = 7): Int {
        val dir = getCacheDir(context)
        val cutoff = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000L
        var deleted = 0
        dir.listFiles()?.forEach { file ->
            if (file.lastModified() < cutoff) {
                if (file.delete()) deleted++
            }
        }
        return deleted
    }

    /** 清空所有缓存。 */
    fun clearAll(context: Context) {
        getCacheDir(context).deleteRecursively()
    }
}
