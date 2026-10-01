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
package com.aiai.data.util

import android.util.Log
import java.io.File

/**
 * 文件管理工具。
 *
 * 提供应用文件管理的通用功能。
 */
object FileManager {

    private const val TAG = "FileManager"

    /**
     * 获取文件大小（可读格式）。
     *
     * @param file 文件
     * @return 可读大小字符串
     */
    fun getReadableSize(file: File): String {
        if (!file.exists()) return "0 B"
        return formatSize(file.length())
    }

    /**
     * 格式化字节数。
     */
    fun formatSize(bytes: Long): String {
        if (bytes < 1024) return "$bytes B"
        val kb = bytes / 1024.0
        if (kb < 1024) return String.format("%.1f KB", kb)
        val mb = kb / 1024.0
        if (mb < 1024) return String.format("%.1f MB", mb)
        val gb = mb / 1024.0
        return String.format("%.2f GB", gb)
    }

    /**
     * 递归计算目录大小。
     *
     * @param dir 目录
     * @return 总字节数
     */
    fun getDirSize(dir: File): Long {
        if (!dir.exists() || !dir.isDirectory) return 0L
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getDirSize(file) else file.length()
        }
        return size
    }

    /**
     * 递归删除目录。
     *
     * @param dir 目录
     */
    fun deleteDir(dir: File): Boolean {
        if (dir.exists()) {
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) deleteDir(file)
                else file.delete()
            }
        }
        return dir.delete()
    }

    /**
     * 清理目录中超过指定天数的文件。
     *
     * @param dir 目录
     * @param maxAgeMs 最大存活时间
     */
    fun cleanOldFiles(dir: File, maxAgeMs: Long) {
        if (!dir.exists()) return
        val now = System.currentTimeMillis()
        dir.listFiles()?.forEach { file ->
            if (now - file.lastModified() > maxAgeMs) {
                file.delete()
            }
        }
    }

    /**
     * 确保目录存在。
     *
     * @param dir 目录
     */
    fun ensureDir(dir: File) {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    /**
     * 获取文件扩展名。
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    fun getExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex > 0) fileName.substring(dotIndex + 1).lowercase() else ""
    }

    /**
     * 根据MIME类型判断文件类别。
     *
     * @param mimeType MIME类型
     * @return 文件类别
     */
    fun getFileCategory(mimeType: String): String {
        return when {
            mimeType.startsWith("image/") -> "image"
            mimeType.startsWith("audio/") -> "audio"
            mimeType.startsWith("video/") -> "video"
            mimeType.contains("pdf") -> "pdf"
            mimeType.contains("word") || mimeType.contains("document") -> "document"
            mimeType.contains("sheet") || mimeType.contains("excel") -> "spreadsheet"
            else -> "other"
        }
    }
}
