/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import android.os.StatFs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat

/**
 * 缓存管理。
 *
 * 统计并清理三类缓存：
 * - 图片缓存（Glide 默认 cacheDir/image_cache）
 * - 网络缓存（http_cache）
 * - 数据库临时文件
 *
 * 提供 [cacheSizeFlow] 实时显示缓存大小，[clean] 执行清理并回调进度。
 */
class CacheManager(private val context: Context) {

    private val _cacheSize = MutableStateFlow(0L)
    /** 当前缓存总字节数。 */
    val cacheSizeFlow: StateFlow<Long> = _cacheSize.asStateFlow()

    /** 所有需要统计的缓存目录。 */
    private fun cacheDirs(): List<File> {
        val result = mutableListOf<File>()
        context.cacheDir?.let { result += it }
        context.externalCacheDir?.let { result += it }
        File(context.cacheDir, "image_cache").let { if (it.exists()) result += it }
        File(context.cacheDir, "http_cache").let { if (it.exists()) result += it }
        return result
    }

    /** 在 IO 线程重新扫描缓存大小。 */
    suspend fun refresh() = withContext(Dispatchers.IO) {
        var total = 0L
        cacheDirs().forEach { dir -> total += dirSize(dir) }
        _cacheSize.value = total
    }

    /** 递归计算目录大小（字节）。 */
    private fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        if (dir.isFile) return dir.length()
        var size = 0L
        dir.listFiles()?.forEach { size += dirSize(it) }
        return size
    }

    /**
     * 清理全部缓存，返回释放的字节数。
     */
    suspend fun clean(): Long = withContext(Dispatchers.IO) {
        val before = dirSize(context.cacheDir)
        cacheDirs().forEach { dir ->
            dir.listFiles()?.forEach { it.deleteRecursively() }
        }
        val after = dirSize(context.cacheDir)
        _cacheSize.value = after
        (before - after).coerceAtLeast(0L)
    }

    companion object {
        private val sizeFormat = DecimalFormat("0.00")

        /** 字节转可读字符串。 */
        fun humanSize(bytes: Long): String {
            if (bytes <= 0) return "0 B"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            var size = bytes.toDouble()
            var unit = 0
            while (size >= 1024 && unit < units.size - 1) {
                size /= 1024
                unit++
            }
            return "${sizeFormat.format(size)} ${units[unit]}"
        }

        @Volatile
        private var instance: CacheManager? = null

        fun get(context: Context): CacheManager {
            return instance ?: synchronized(this) {
                instance ?: CacheManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
