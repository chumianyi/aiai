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
import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.File

/**
 * 存储管理器。
 *
 * 提供内部/外部存储、可用空间、挂载状态等功能。
 */
class StorageManager(private val context: Context) {

    companion object {
        private const val TAG = "StorageManager"
        private const val MIN_DISK_SPACE = 10 * 1024 * 1024L // 10MB
    }

    /**
     * 获取内部存储总空间。
     *
     * @return 总空间（字节）
     */
    fun getInternalTotalSpace(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.blockSizeLong * stat.blockCountLong
    }

    /**
     * 获取内部存储可用空间。
     *
     * @return 可用空间（字节）
     */
    fun getInternalAvailableSpace(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.blockSizeLong * stat.availableBlocksLong
    }

    /**
     * 获取内部存储已用空间。
     *
     * @return 已用空间（字节）
     */
    fun getInternalUsedSpace(): Long {
        return getInternalTotalSpace() - getInternalAvailableSpace()
    }

    /**
     * 检查外部存储是否可写。
     *
     * @return true 表示可写
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 检查外部存储是否可读。
     *
     * @return true 表示可读
     */
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }

    /**
     * 获取外部存储总空间。
     *
     * @return 总空间（字节）
     */
    fun getExternalTotalSpace(): Long {
        if (!isExternalStorageReadable()) return 0L
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        return stat.blockSizeLong * stat.blockCountLong
    }

    /**
     * 获取外部存储可用空间。
     *
     * @return 可用空间（字节）
     */
    fun getExternalAvailableSpace(): Long {
        if (!isExternalStorageReadable()) return 0L
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        return stat.blockSizeLong * stat.availableBlocksLong
    }

    /**
     * 检查存储空间是否充足。
     *
     * @return true 表示充足
     */
    fun hasEnoughSpace(): Boolean {
        return getInternalAvailableSpace() > MIN_DISK_SPACE
    }

    /**
     * 获取应用内部缓存目录。
     *
     * @return 缓存目录 File
     */
    fun getInternalCacheDir(): File {
        return context.cacheDir
    }

    /**
     * 获取应用外部缓存目录。
     *
     * @return 缓存目录 File
     */
    fun getExternalCacheDir(): File? {
        return context.externalCacheDir
    }

    /**
     * 清除应用缓存。
     *
     * @return 清除的字节数
     */
    fun clearAppCache(): Long {
        var cleared = 0L
        try {
            cleared += deleteDir(context.cacheDir)
            context.externalCacheDir?.let { cleared += deleteDir(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Clear cache failed", e)
        }
        return cleared
    }

    /**
     * 递归删除目录。
     */
    private fun deleteDir(dir: File?): Long {
        if (dir == null || !dir.exists()) return 0L
        var size = 0L
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                size += deleteDir(file)
            } else {
                size += file.length()
                file.delete()
            }
        }
        dir.delete()
        return size
    }

    /**
     * 获取应用数据大小。
     *
     * @return 数据大小（字节）
     */
    fun getAppDataSize(): Long {
        var size = 0L
        context.filesDir.listFiles()?.forEach {
            size += getFileSize(it)
        }
        size += getDirSize(context.cacheDir)
        return size
    }

    /**
     * 获取文件/目录大小。
     */
    private fun getFileSize(file: File): Long {
        return if (file.isDirectory) {
            getDirSize(file)
        } else {
            file.length()
        }
    }

    /**
     * 获取目录大小。
     */
    private fun getDirSize(dir: File): Long {
        var size = 0L
        dir.listFiles()?.forEach {
            size += getFileSize(it)
        }
        return size
    }

    /**
     * 格式化文件大小。
     *
     * @param bytes 字节数
     * @return 格式化后的大小
     */
    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "${bytes}B"
            bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)}KB"
            bytes < 1024 * 1024 * 1024 -> "${"%.1f".format(bytes / (1024.0 * 1024.0))}MB"
            else -> "${"%.1f".format(bytes / (1024.0 * 1024.0 * 1024.0))}GB"
        }
    }
}
