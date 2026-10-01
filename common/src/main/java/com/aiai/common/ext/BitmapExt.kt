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
package com.aiai.common.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Bitmap 相关扩展函数集合。
 *
 * 提供缩放、裁剪、圆角、压缩、旋转、保存、滤镜等能力。
 */

// region 缩放 & 裁剪

/** 按比例缩放 Bitmap 到目标宽高。 */
fun Bitmap.scaled(targetWidth: Int, targetHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
}

/** 按比例缩放，保持宽高比，限制最大边长。 */
fun Bitmap.scaledMaxSide(maxSide: Int): Bitmap {
    val ratio = minOf(maxSide.toFloat() / width, maxSide.toFloat() / height)
    if (ratio >= 1f) return this
    return scaled((width * ratio).toInt(), (height * ratio).toInt())
}

/** 裁剪为圆形。 */
fun Bitmap.circle(): Bitmap {
    val size = minOf(width, height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = Rect(0, 0, size, size)
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
}

/** 圆角裁剪。 */
fun Bitmap.rounded(radius: Float): Bitmap {
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
    canvas.drawRoundRect(rect, radius, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return output
}

// endregion

// region 压缩

/** 压缩为 JPEG 字节数组，质量 [quality]（0-100）。 */
fun Bitmap.toJpegBytes(quality: Int = 80): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}

/** 压缩为 PNG 字节数组。 */
fun Bitmap.toPngBytes(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

/** 按最大字节数压缩（迭代降低质量）。 */
fun Bitmap.compressToMaxSize(maxBytes: Long): ByteArray {
    var quality = 100
    var bytes = toJpegBytes(quality)
    while (bytes.size > maxBytes && quality > 10) {
        quality -= 10
        bytes = toJpegBytes(quality)
    }
    return bytes
}

// endregion

// region 旋转 & 滤镜

/** 旋转 Bitmap [degrees] 度。 */
fun Bitmap.rotated(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/** 灰度滤镜。 */
fun Bitmap.grayscale(): Bitmap {
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val colorMatrix = android.graphics.ColorMatrix().apply {
        setSaturation(0f)
    }
    val filter = android.graphics.ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = filter
    canvas.drawBitmap(this, 0f, 0f, paint)
    return output
}

/** 反相滤镜。 */
fun Bitmap.inverted(): Bitmap {
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val colorMatrix = android.graphics.ColorMatrix(floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    ))
    paint.colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return output
}

// endregion

// region 保存

/** 保存 Bitmap 到文件（JPEG）。 */
fun Bitmap.saveToFile(file: File, quality: Int = 90): Boolean {
    return try {
        file.parentFile?.mkdirs()
        FileOutputStream(file).use {
            compress(Bitmap.CompressFormat.JPEG, quality, it)
        }
        true
    } catch (e: Exception) {
        false
    }
}

/** 从文件路径解码 Bitmap（带采样率）。 */
fun File.decodeBitmap(maxSide: Int = 1024): Bitmap? {
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(absolutePath, opts)
    opts.inSampleSize = calculateInSampleSize(opts.outWidth, opts.outHeight, maxSide)
    opts.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(absolutePath, opts)
}

private fun calculateInSampleSize(w: Int, h: Int, maxSide: Int): Int {
    var inSample = 1
    var (newW, newH) = w to h
    while (newW / 2 >= maxSide || newH / 2 >= maxSide) {
        newW /= 2
        newH /= 2
        inSample *= 2
    }
    return inSample
}

// endregion
