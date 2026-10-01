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

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * 文件工具类。
 */
object FileUtil {

    /** 读取文件为字符串。 */
    fun readFile(file: File): String? {
        return try {
            if (!file.exists()) return null
            FileInputStream(file).bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: IOException) {
            null
        }
    }

    /** 写入字符串到文件。 */
    fun writeFile(file: File, content: String): Boolean {
        return try {
            file.parentFile?.mkdirs()
            FileOutputStream(file).bufferedWriter(Charsets.UTF_8).use { it.write(content) }
            true
        } catch (e: IOException) {
            false
        }
    }

    /** 复制文件。 */
    fun copyFile(src: File, dest: File): Boolean {
        return try {
            dest.parentFile?.mkdirs()
            FileInputStream(src).use { input ->
                FileOutputStream(dest).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    /** 删除文件/目录。 */
    fun deleteFile(file: File): Boolean {
        return try {
            if (file.isDirectory) file.listFiles()?.forEach { deleteFile(it) }
            file.delete()
        } catch (e: Exception) {
            false
        }
    }

    /** 获取文件大小格式化。 */
    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB")
        var size = bytes.toDouble()
        var index = 0
        while (size >= 1024 && index < units.size - 1) {
            size /= 1024
            index++
        }
        return String.format("%.1f %s", size, units[index])
    }

    /** 获取目录大小。 */
    fun getDirSize(dir: File): Long {
        if (!dir.exists()) return 0
        var size = 0L
        dir.listFiles()?.forEach { size += if (it.isDirectory) getDirSize(it) else it.length() }
        return size
    }
}
