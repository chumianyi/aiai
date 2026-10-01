/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.util

import java.text.DecimalFormat

/**
 * 文件大小工具类
 *
 * 提供文件大小格式化和解析功能。
 */
object FileSizeUtil {

    private val units = arrayOf("B", "KB", "MB", "GB", "TB")

    /**
     * 格式化文件大小
     */
    fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
        val index = digitGroups.coerceIn(0, units.size - 1)
        val size = bytes / Math.pow(1024.0, index.toDouble())
        val df = DecimalFormat("#.##")
        return "${df.format(size)} ${units[index]}"
    }

    /**
     * 解析文件大小字符串为字节数
     */
    fun parseSize(sizeStr: String): Long {
        val regex = Regex("([\\d.]+)\\s*(B|KB|MB|GB|TB)", RegexOption.IGNORE_CASE)
        val match = regex.find(sizeStr) ?: return 0
        val value = match.groupValues[1].toDoubleOrNull() ?: return 0
        val unit = match.groupValues[2].uppercase()
        val multiplier = when (unit) {
            "KB" -> 1024
            "MB" -> 1024 * 1024
            "GB" -> 1024 * 1024 * 1024
            "TB" -> 1024L * 1024 * 1024 * 1024
            else -> 1
        }
        return (value * multiplier).toLong()
    }

    /**
     * 获取文件扩展名
     */
    fun getExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex >= 0) fileName.substring(dotIndex + 1).lowercase() else ""
    }

    /**
     * 获取文件类型描述
     */
    fun getFileTypeDescription(extension: String): String {
        return when (extension.lowercase()) {
            "txt", "md" -> "文本文件"
            "doc", "docx" -> "Word文档"
            "xls", "xlsx" -> "Excel表格"
            "ppt", "pptx" -> "PPT演示"
            "pdf" -> "PDF文档"
            "jpg", "jpeg", "png", "gif", "webp" -> "图片文件"
            "mp3", "wav", "flac", "m4a" -> "音频文件"
            "mp4", "avi", "mkv", "mov" -> "视频文件"
            "zip", "rar", "7z" -> "压缩包"
            "kt", "java", "py", "js", "ts" -> "代码文件"
            else -> "未知文件"
        }
    }
}
