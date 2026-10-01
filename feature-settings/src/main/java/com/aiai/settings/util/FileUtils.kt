/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import java.io.File

/**
 * 文件工具：大小格式化、递归删除、扩展名提取。
 */
object FileUtils {

    /** 字节转可读字符串。 */
    fun humanSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB")
        var size = bytes.toDouble()
        var unit = 0
        while (size >= 1024 && unit < units.size - 1) {
            size /= 1024
            unit++
        }
        return String.format("%.1f %s", size, units[unit])
    }

    /** 递归删除目录或文件。 */
    fun deleteRecursive(file: File): Boolean {
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteRecursive(it) }
        }
        return file.delete()
    }

    /** 递归计算目录大小。 */
    fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0
        if (dir.isFile) return dir.length()
        var size = 0L
        dir.listFiles()?.forEach { size += dirSize(it) }
        return size
    }

    /** 提取文件扩展名（小写，不含点）。 */
    fun extension(fileName: String): String {
        val idx = fileName.lastIndexOf('.')
        return if (idx >= 0) fileName.substring(idx + 1).lowercase() else ""
    }

    /** 文件名（不含扩展名）。 */
    fun baseName(fileName: String): String {
        val idx = fileName.lastIndexOf('.')
        return if (idx >= 0) fileName.substring(0, idx) else fileName
    }

    /** 确保目录存在。 */
    fun ensureDir(dir: File): Boolean {
        return dir.exists() || dir.mkdirs()
    }
}
