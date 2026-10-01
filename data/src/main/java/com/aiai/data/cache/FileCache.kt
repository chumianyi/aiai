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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 文件缓存。
 *
 * 将缓存数据持久化到文件系统。
 *
 * @param cacheDir 缓存目录
 * @param maxSizeBytes 最大缓存大小（字节）
 */
class FileCache(
    private val cacheDir: File,
    private val maxSizeBytes: Long = 50L * 1024L * 1024L,
) {

    companion object {
        private const val TAG = "FileCache"
    }

    init {
        cacheDir.mkdirs()
    }

    /**
     * 写入缓存文件。
     *
     * @param key 缓存键
     * @param data 数据字节数组
     * @return 是否成功
     */
    fun put(key: String, data: ByteArray): Boolean {
        return try {
            val file = getCacheFile(key)
            FileOutputStream(file).use { it.write(data) }
            Log.d(TAG, "Cached file: ${file.name}, size=${data.size}")
            // 检查是否需要清理
            if (getTotalSize() > maxSizeBytes) {
                evictOldest()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Put cache failed: ${e.message}")
            false
        }
    }

    /**
     * 读取缓存文件。
     *
     * @param key 缓存键
     * @return 数据字节数组，未命中返回null
     */
    fun get(key: String): ByteArray? {
        val file = getCacheFile(key)
        if (!file.exists()) return null
        return try {
            FileInputStream(file).use { it.readBytes() }
        } catch (e: Exception) {
            Log.e(TAG, "Get cache failed: ${e.message}")
            null
        }
    }

    /**
     * 移除缓存。
     */
    fun remove(key: String): Boolean {
        val file = getCacheFile(key)
        return if (file.exists()) file.delete() else true
    }

    /**
     * 清除所有缓存。
     */
    fun clear() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    /**
     * 获取缓存总大小。
     */
    fun getTotalSize(): Long {
        return cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    /**
     * 获取缓存文件数量。
     */
    fun getFileCount(): Int {
        return cacheDir.listFiles()?.size ?: 0
    }

    /**
     * 获取缓存文件。
     */
    private fun getCacheFile(key: String): File {
        val safeName = key.replace("/", "_").replace(":", "_")
        return File(cacheDir, "$safeName.cache")
    }

    /**
     * 淘汰最旧的缓存文件。
     */
    private fun evictOldest() {
        val files = cacheDir.listFiles()?.sortedBy { it.lastModified() } ?: return
        var freed = 0L
        val target = maxSizeBytes / 2
        for (file in files) {
            freed += file.length()
            file.delete()
            if (freed >= target) break
        }
        Log.d(TAG, "Evicted cache, freed ${freed / 1024}KB")
    }
}
