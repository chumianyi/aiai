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
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * ImageView 扩展函数集合。
 *
 * 提供圆角、圆形、边框、加载动画、颜色过滤等常用 ImageView 操作。
 */

// region 形状变换

/**
 * 设置为圆形图片。
 *
 * @param borderWidth 边框宽度
 * @param borderColor 边框颜色
 */
fun ImageView.setCircle(borderWidth: Float = 0f, @ColorInt borderColor: Int = Color.TRANSPARENT) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val bitmap = drawableToBitmap(drawable) ?: return@addOnLayoutChangeListener
        val size = minOf(width, height)
        if (size <= 0) return@addOnLayoutChangeListener

        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scale = size.toFloat() / minOf(bitmap.width, bitmap.height)
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        paint.shader = shader

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        if (borderWidth > 0) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                this.color = borderColor
                strokeWidth = borderWidth
            }
            canvas.drawCircle(size / 2f, size / 2f, size / 2f - borderWidth / 2, borderPaint)
        }

        setImageBitmap(output)
    }
}

/**
 * 设置圆角图片。
 *
 * @param radius 圆角半径
 * @param borderWidth 边框宽度
 * @param borderColor 边框颜色
 */
fun ImageView.setRounded(
    radius: Float = 20f,
    borderWidth: Float = 0f,
    @ColorInt borderColor: Int = Color.TRANSPARENT
) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val bitmap = drawableToBitmap(drawable) ?: return@addOnLayoutChangeListener
        val w = width
        val h = height
        if (w <= 0 || h <= 0) return@addOnLayoutChangeListener

        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scale = maxOf(w.toFloat() / bitmap.width, h.toFloat() / bitmap.height)
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        paint.shader = shader

        canvas.drawRoundRect(rect, radius, radius, paint)

        if (borderWidth > 0) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                this.color = borderColor
                strokeWidth = borderWidth
            }
            canvas.drawRoundRect(rect, radius, radius, borderPaint)
        }

        setImageBitmap(output)
    }
}

/**
 * 设置左上角和右上角圆角。
 *
 * @param topRadius 顶部圆角半径
 */
fun ImageView.setTopRounded(topRadius: Float = 20f) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val bitmap = drawableToBitmap(drawable) ?: return@addOnLayoutChangeListener
        val w = width
        val h = height
        if (w <= 0 || h <= 0) return@addOnLayoutChangeListener

        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scale = maxOf(w.toFloat() / bitmap.width, h.toFloat() / bitmap.height)
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        paint.shader = shader

        val path = android.graphics.Path()
        val radii = floatArrayOf(topRadius, topRadius, topRadius, topRadius, 0f, 0f, 0f, 0f)
        path.addRoundRect(rect, radii, android.graphics.Path.Direction.CW)
        canvas.drawPath(path, paint)

        setImageBitmap(output)
    }
}

/**
 * 设置左下角和右下角圆角。
 *
 * @param bottomRadius 底部圆角半径
 */
fun ImageView.setBottomRounded(bottomRadius: Float = 20f) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val bitmap = drawableToBitmap(drawable) ?: return@addOnLayoutChangeListener
        val w = width
        val h = height
        if (w <= 0 || h <= 0) return@addOnLayoutChangeListener

        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scale = maxOf(w.toFloat() / bitmap.width, h.toFloat() / bitmap.height)
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        paint.shader = shader

        val path = android.graphics.Path()
        val radii = floatArrayOf(0f, 0f, 0f, 0f, bottomRadius, bottomRadius, bottomRadius, bottomRadius)
        path.addRoundRect(rect, radii, android.graphics.Path.Direction.CW)
        canvas.drawPath(path, paint)

        setImageBitmap(output)
    }
}

// endregion

// region 边框

/**
 * 设置边框。
 *
 * @param width 边框宽度
 * @param color 边框颜色
 */
fun ImageView.setBorder(width: Float, @ColorInt color: Int) {
    background = android.graphics.drawable.GradientDrawable().apply {
        setStroke(width.toInt(), color)
        cornerRadius = 0f
    }
}

/**
 * 设置圆形边框。
 *
 * @param width 边框宽度
 * @param color 边框颜色
 */
fun ImageView.setCircleBorder(width: Float, @ColorInt color: Int) {
    background = android.graphics.drawable.GradientDrawable().apply {
        shape = android.graphics.drawable.GradientDrawable.OVAL
        setStroke(width.toInt(), color)
    }
}

/**
 * 设置圆角边框。
 *
 * @param width 边框宽度
 * @param color 边框颜色
 * @param radius 圆角半径
 */
fun ImageView.setRoundedBorder(width: Float, @ColorInt color: Int, radius: Float) {
    background = android.graphics.drawable.GradientDrawable().apply {
        setStroke(width.toInt(), color)
        cornerRadius = radius
    }
}

// endregion

// region 颜色过滤

/**
 * 设置颜色过滤。
 *
 * @param color 过滤颜色
 * @param mode 混合模式
 */
fun ImageView.setColorFilterCompat(
    @ColorInt color: Int,
    mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
) {
    colorFilter = PorterDuffColorFilter(color, mode)
}

/**
 * 清除颜色过滤。
 */
fun ImageView.clearColorFilter() {
    colorFilter = null
}

/**
 * 设置灰度效果。
 */
fun ImageView.setGrayscale() {
    val matrix = android.graphics.ColorMatrix()
    matrix.setSaturation(0f)
    colorFilter = android.graphics.ColorMatrixColorFilter(matrix)
}

/**
 * 设置怀旧效果。
 */
fun ImageView.setSepia() {
    val matrix = android.graphics.ColorMatrix()
    matrix.setSaturation(0f)
    val sepia = android.graphics.ColorMatrix(floatArrayOf(
        1f, 0f, 0f, 0f, 20f,
        0f, 1f, 0f, 0f, 20f,
        0f, 0f, 1f, 0f, 20f,
        0f, 0f, 0f, 1f, 0f
    ))
    matrix.postConcat(sepia)
    colorFilter = android.graphics.ColorMatrixColorFilter(matrix)
}

/**
 * 设置反色效果。
 */
fun ImageView.setInvert() {
    val matrix = android.graphics.ColorMatrix(floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    ))
    colorFilter = android.graphics.ColorMatrixColorFilter(matrix)
}

// endregion

// region 加载动画

/**
 * 淡入加载图片。
 *
 * @param drawableRes 图片资源
 * @param duration 动画时长
 */
fun ImageView.setImageResourceFade(
    @DrawableRes drawableRes: Int,
    duration: Long = 300
) {
    setImageResource(drawableRes)
    alpha = 0f
    animate().alpha(1f).setDuration(duration).start()
}

/**
 * 淡入加载 Bitmap。
 *
 * @param bitmap 位图
 * @param duration 动画时长
 */
fun ImageView.setImageBitmapFade(bitmap: Bitmap, duration: Long = 300) {
    setImageBitmap(bitmap)
    alpha = 0f
    animate().alpha(1f).setDuration(duration).start()
}

/**
 * 旋转动画。
 *
 * @param degree 旋转角度
 * @param duration 动画时长
 */
fun ImageView.rotate(degree: Float, duration: Long = 300) {
    animate().rotationBy(degree).setDuration(duration).start()
}

/**
 * 缩放动画。
 *
 * @param scaleX X方向缩放
 * @param scaleY Y方向缩放
 * @param duration 动画时长
 */
fun ImageView.scale(scaleX: Float, scaleY: Float, duration: Long = 300) {
    animate().scaleX(scaleX).scaleY(scaleY).setDuration(duration).start()
}

// endregion

// region 其他

/**
 * Drawable 转 Bitmap。
 */
private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
    if (drawable == null) return null
    if (drawable is BitmapDrawable) return drawable.bitmap

    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * 设置图片资源并保持宽高比。
 *
 * @param resId 图片资源ID
 */
fun ImageView.setImageResourceKeepRatio(@DrawableRes resId: Int) {
    scaleType = ImageView.ScaleType.CENTER_INSIDE
    setImageResource(resId)
}

/**
 * 设置图片为圆形裁剪。
 *
 * @param borderWidth 边框宽度
 * @param borderColor 边框颜色
 */
fun ImageView.setCircleCrop(borderWidth: Float = 0f, @ColorInt borderColor: Int = Color.TRANSPARENT) {
    scaleType = ImageView.ScaleType.CENTER_CROP
    setCircle(borderWidth, borderColor)
}

/**
 * 是否有图片。
 */
fun ImageView.hasImage(): Boolean = drawable != null

/**
 * 清除图片。
 */
fun ImageView.clearImage() {
    setImageDrawable(null)
}

/**
 * 设置图片对比度。
 *
 * @param contrast 对比度（1.0为正常）
 */
fun ImageView.setContrast(contrast: Float) {
    val matrix = android.graphics.ColorMatrix()
    matrix.setScale(contrast, contrast, contrast, 1f)
    colorFilter = android.graphics.ColorMatrixColorFilter(matrix)
}

/**
 * 设置亮度。
 *
 * @param brightness 亮度（0为正常，正值变亮，负值变暗）
 */
fun ImageView.setBrightness(brightness: Float) {
    val matrix = android.graphics.ColorMatrix()
    val value = brightness * 255
    matrix.setScale(1f, 1f, 1f, 1f)
    matrix.postConcat(android.graphics.ColorMatrix(floatArrayOf(
        1f, 0f, 0f, 0f, value,
        0f, 1f, 0f, 0f, value,
        0f, 0f, 1f, 0f, value,
        0f, 0f, 0f, 1f, 0f
    )))
    colorFilter = android.graphics.ColorMatrixColorFilter(matrix)
}

/**
 * 设置圆角半径（统一四角）。
 *
 * @param radius 圆角半径
 */
fun ImageView.setCornerRadius(radius: Float) {
    setRounded(radius)
}

/**
 * 设置单个角的圆角。
 *
 * @param topLeft 左上角半径
 * @param topRight 右上角半径
 * @param bottomLeft 左下角半径
 * @param bottomRight 右下角半径
 */
fun ImageView.setCornerRadii(
    topLeft: Float,
    topRight: Float,
    bottomLeft: Float,
    bottomRight: Float
) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val bitmap = drawableToBitmap(drawable) ?: return@addOnLayoutChangeListener
        val w = width
        val h = height
        if (w <= 0 || h <= 0) return@addOnLayoutChangeListener

        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scale = maxOf(w.toFloat() / bitmap.width, h.toFloat() / bitmap.height)
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        paint.shader = shader

        val path = android.graphics.Path()
        val radii = floatArrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft
        )
        path.addRoundRect(rect, radii, android.graphics.Path.Direction.CW)
        canvas.drawPath(path, paint)

        setImageBitmap(output)
    }
}

// endregion
