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
package com.aiai.common.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat

/**
 * 文件工具类。
 *
 * 提供文件操作、目录操作、文件大小格式化等功能。
 */
object FileUtil {

    private val decimalFormat = DecimalFormat("0.00")

    /**
     * 检查文件是否存在。
     *
     * @param path 文件路径
     * @return 是否存在
     */
    fun exists(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            File(path).exists()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检查是否是目录。
     *
     * @param path 文件路径
     * @return 是否是目录
     */
    fun isDirectory(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            File(path).isDirectory
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检查是否是文件。
     *
     * @param path 文件路径
     * @return 是否是文件
     */
    fun isFile(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            File(path).isFile
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 获取文件大小。
     *
     * @param path 文件路径
     * @return 文件大小（字节）
     */
    fun getFileSize(path: String?): Long {
        if (path.isNullOrBlank()) return 0L
        return try {
            File(path).length()
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * 格式化文件大小。
     *
     * @param size 字节数
     * @return 格式化后的文件大小
     */
    fun formatFileSize(size: Long): String {
        return when {
            size < 1024 -> "${size}B"
            size < 1024 * 1024 -> "${decimalFormat.format(size / 1024.0)}KB"
            size < 1024 * 1024 * 1024 -> "${decimalFormat.format(size / (1024.0 * 1024.0))}MB"
            else -> "${decimalFormat.format(size / (1024.0 * 1024.0 * 1024.0))}GB"
        }
    }

    /**
     * 创建目录。
     *
     * @param path 目录路径
     * @return 是否成功
     */
    fun mkdirs(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            File(path).mkdirs()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 删除文件或目录。
     *
     * @param path 文件路径
     * @return 是否成功
     */
    fun delete(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            val file = File(path)
            if (file.isDirectory) {
                deleteDir(file)
            } else {
                file.delete()
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 递归删除目录。
     *
     * @param dir 目录文件
     * @return 是否成功
     */
    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.listFiles()
            children?.forEach { child ->
                if (child.isDirectory) {
                    deleteDir(child)
                } else {
                    child.delete()
                }
            }
        }
        return dir.delete()
    }

    /**
     * 复制文件。
     *
     * @param srcPath 源文件路径
     * @param destPath 目标文件路径
     * @return 是否成功
     */
    fun copyFile(srcPath: String?, destPath: String?): Boolean {
        if (srcPath.isNullOrBlank() || destPath.isNullOrBlank()) return false
        return try {
            val srcFile = File(srcPath)
            if (!srcFile.exists() || !srcFile.isFile) return false

            val destFile = File(destPath)
            destFile.parentFile?.mkdirs()

            FileInputStream(srcFile).use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * 移动文件。
     *
     * @param srcPath 源文件路径
     * @param destPath 目标文件路径
     * @return 是否成功
     */
    fun moveFile(srcPath: String?, destPath: String?): Boolean {
        if (srcPath.isNullOrBlank() || destPath.isNullOrBlank()) return false
        return try {
            val srcFile = File(srcPath)
            if (!srcFile.exists()) return false

            val destFile = File(destPath)
            destFile.parentFile?.mkdirs()

            srcFile.renameTo(destFile)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 重命名文件。
     *
     * @param path 文件路径
     * @param newName 新文件名
     * @return 是否成功
     */
    fun rename(path: String?, newName: String?): Boolean {
        if (path.isNullOrBlank() || newName.isNullOrBlank()) return false
        return try {
            val file = File(path)
            if (!file.exists()) return false
            val newFile = File(file.parent, newName)
            file.renameTo(newFile)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 获取文件名（含扩展名）。
     *
     * @param path 文件路径
     * @return 文件名
     */
    fun getFileName(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            File(path).name
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取文件名（不含扩展名）。
     *
     * @param path 文件路径
     * @return 文件名（不含扩展名）
     */
    fun getFileNameWithoutExtension(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            val name = File(path).name
            val dotIndex = name.lastIndexOf('.')
            if (dotIndex > 0) name.substring(0, dotIndex) else name
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取文件扩展名。
     *
     * @param path 文件路径
     * @return 扩展名
     */
    fun getFileExtension(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            val name = File(path).name
            val dotIndex = name.lastIndexOf('.')
            if (dotIndex > 0) name.substring(dotIndex + 1) else ""
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取目录路径。
     *
     * @param path 文件路径
     * @return 目录路径
     */
    fun getParentDir(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            File(path).parent ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 列出目录下的文件。
     *
     * @param path 目录路径
     * @return 文件列表
     */
    fun listFiles(path: String?): List<String> {
        if (path.isNullOrBlank()) return emptyList()
        return try {
            val dir = File(path)
            if (!dir.isDirectory) return emptyList()
            dir.listFiles()?.map { it.absolutePath } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 获取目录大小。
     *
     * @param path 目录路径
     * @return 目录大小（字节）
     */
    fun getDirSize(path: String?): Long {
        if (path.isNullOrBlank()) return 0L
        return try {
            val dir = File(path)
            if (!dir.isDirectory) return 0L
            var size = 0L
            dir.listFiles()?.forEach { file ->
                size += if (file.isDirectory) getDirSize(file.absolutePath) else file.length()
            }
            size
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * 清空目录。
     *
     * @param path 目录路径
     * @return 是否成功
     */
    fun clearDir(path: String?): Boolean {
        if (path.isNullOrBlank()) return false
        return try {
            val dir = File(path)
            if (!dir.isDirectory) return false
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) deleteDir(file) else file.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 读取文件内容为字符串。
     *
     * @param path 文件路径
     * @return 文件内容
     */
    fun readFile(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            File(path).readText()
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 写入字符串到文件。
     *
     * @param path 文件路径
     * @param content 内容
     * @return 是否成功
     */
    fun writeFile(path: String?, content: String?): Boolean {
        if (path.isNullOrBlank() || content == null) return false
        return try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 追加内容到文件。
     *
     * @param path 文件路径
     * @param content 内容
     * @return 是否成功
     */
    fun appendFile(path: String?, content: String?): Boolean {
        if (path.isNullOrBlank() || content == null) return false
        return try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.appendText(content)
            true
        } catch (e: Exception) {
            false
        }
    }
}
