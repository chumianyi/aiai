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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * 图片压缩管理器。
 *
 * 提供图片压缩、质量调整、格式转换等功能。
 */
object CompressManager {

    private const val TAG = "CompressManager"
    private const val DEFAULT_QUALITY = 80
    private const val MAX_WIDTH = 1080
    private const val MAX_HEIGHT = 1920

    /**
     * 压缩图片文件。
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param quality 质量（0-100）
     * @return 是否成功
     */
    fun compressImage(
        srcFile: File,
        destFile: File,
        quality: Int = DEFAULT_QUALITY
    ): Boolean {
        return try {
            // 读取原始尺寸
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(srcFile.absolutePath, options)

            // 计算采样率
            options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(srcFile.absolutePath, options)

            // 压缩并保存
            FileOutputStream(destFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }

            bitmap.recycle()
            Log.d(TAG, "Image compressed: ${srcFile.name} -> ${destFile.name}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Compress image failed", e)
            false
        }
    }

    /**
     * 计算采样率。
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                   halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * 压缩 Bitmap 到字节数组。
     *
     * @param bitmap 原始 Bitmap
     * @param quality 质量
     * @return 压缩后的字节数组
     */
    fun compressBitmapToBytes(bitmap: Bitmap, quality: Int = DEFAULT_QUALITY): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * 获取文件大小。
     *
     * @param file 文件
     * @return 文件大小（字节）
     */
    fun getFileSize(file: File): Long {
        return file.length()
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
            bytes < 1024 * 1024 -> String.format("%.2fKB", bytes / 1024.0)
            else -> String.format("%.2fMB", bytes / (1024.0 * 1024.0))
        }
    }
}
