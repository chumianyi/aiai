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
package com.aiai.data.util

import android.content.Context
import android.util.Log
import java.io.File

/**
 * 数据清理器。
 *
 * 提供过期数据清理、缓存清理、空间回收等功能。
 */
class DataCleaner(private val context: Context) {

    companion object {
        private const val TAG = "DataCleaner"
        private const val DEFAULT_CACHE_EXPIRE_DAYS = 7L
    }

    /**
     * 清理过期缓存文件。
     *
     * @param expireDays 过期天数
     * @return 清理的文件数量
     */
    fun cleanExpiredCache(expireDays: Long = DEFAULT_CACHE_EXPIRE_DAYS): Int {
        var cleaned = 0
        val cacheDir = context.cacheDir
        val expireTime = System.currentTimeMillis() - expireDays * 24 * 60 * 60 * 1000

        cacheDir.listFiles()?.forEach { file ->
            if (file.lastModified() < expireTime) {
                if (file.delete()) {
                    cleaned++
                }
            }
        }

        Log.d(TAG, "Cleaned $cleaned expired cache files")
        return cleaned
    }

    /**
     * 清理所有缓存。
     *
     * @return 释放的空间（字节）
     */
    fun clearAllCache(): Long {
        var freedSpace = 0L

        // 清理内部缓存
        freedSpace += deleteDir(context.cacheDir)

        // 清理外部缓存
        context.externalCacheDir?.let {
            freedSpace += deleteDir(it)
        }

        Log.d(TAG, "Cleared all cache, freed: ${freedSpace / 1024 / 1024}MB")
        return freedSpace
    }

    /**
     * 递归删除目录。
     */
    private fun deleteDir(dir: File): Long {
        if (!dir.exists()) return 0L

        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) {
                deleteDir(file)
            } else {
                val fileSize = file.length()
                file.delete()
                fileSize
            }
        }
        dir.delete()
        return size
    }

    /**
     * 获取缓存大小。
     *
     * @return 缓存大小（字节）
     */
    fun getCacheSize(): Long {
        var size = getDirSize(context.cacheDir)
        context.externalCacheDir?.let { size += getDirSize(it) }
        return size
    }

    /**
     * 获取目录大小。
     */
    private fun getDirSize(dir: File): Long {
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) {
                getDirSize(file)
            } else {
                file.length()
            }
        }
        return size
    }

    /**
     * 清理临时文件。
     *
     * @return 清理的文件数
     */
    fun cleanTempFiles(): Int {
        var cleaned = 0
        val tempDir = File(context.cacheDir, "temp")

        if (tempDir.exists()) {
            tempDir.listFiles()?.forEach { file ->
                if (file.delete()) cleaned++
            }
        }

        Log.d(TAG, "Cleaned $cleaned temp files")
        return cleaned
    }
}
