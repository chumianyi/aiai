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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.os.Environment
import java.io.File
import java.text.DecimalFormat

/**
 * 文件大小工具类。
 */
object FileSizeUtil {

    /** 格式化文件大小。 */
    fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var index = 0
        while (size >= 1024 && index < units.size - 1) {
            size /= 1024
            index++
        }
        return DecimalFormat("#,##0.#").format(size) + " " + units[index]
    }

    /** 获取目录大小。 */
    fun getDirSize(dir: File): Long {
        if (!dir.exists()) return 0
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getDirSize(file) else file.length()
        }
        return size
    }

    /** 清除目录。 */
    fun clearDir(dir: File) {
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach { it.deleteRecursively() }
        }
    }

    /** 获取缓存目录。 */
    fun getCacheDir(context: android.content.Context): File {
        return context.cacheDir
    }

    /** 获取外部缓存目录。 */
    fun getExternalCacheDir(context: android.content.Context): File? {
        return context.externalCacheDir
    }
}
