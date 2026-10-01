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
package com.aiai.common.util.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * 图片压缩工具类。
 *
 * 支持质量压缩、尺寸压缩、按大小压缩。
 */
object ImageCompressUtil {

    /**
     * 质量压缩：保持尺寸不变，降低 JPEG 质量。
     *
     * @param bitmap 原图
     * @param quality 质量（0-100）
     * @return 压缩后的字节数组
     */
    fun compressByQuality(bitmap: Bitmap, quality: Int = 80): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    /**
     * 尺寸压缩：按采样率缩放。
     *
     * @param src 图片文件
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     */
    fun compressBySize(src: File, maxWidth: Int = 1080, maxHeight: Int = 1920): Bitmap? {
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(src.absolutePath, opts)
        var inSample = 1
        var w = opts.outWidth
        var h = opts.outHeight
        while (w / 2 >= maxWidth || h / 2 >= maxHeight) {
            w /= 2
            h /= 2
            inSample *= 2
        }
        opts.inSampleSize = inSample
        opts.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(src.absolutePath, opts)
    }

    /**
     * 按目标文件大小压缩（迭代降低质量直到小于 maxSize）。
     */
    fun compressToMaxSize(bitmap: Bitmap, maxSize: Long = 1024 * 1024): ByteArray {
        var quality = 100
        var bytes = compressByQuality(bitmap, quality)
        while (bytes.size > maxSize && quality > 10) {
            quality -= 10
            bytes = compressByQuality(bitmap, quality)
        }
        return bytes
    }

    /**
     * 压缩并保存到文件。
     */
    fun compressAndSave(bitmap: Bitmap, dest: File, quality: Int = 80): Boolean {
        return try {
            dest.parentFile?.mkdirs()
            FileOutputStream(dest).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
