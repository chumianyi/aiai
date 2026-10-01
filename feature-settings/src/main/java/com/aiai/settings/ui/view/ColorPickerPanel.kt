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
package com.aiai.settings.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.aiai.settings.R

/**
 * 高级颜色选择器：饱和度/亮度取色面板 + 色相条 + 预览。
 * 支持触摸拖动取色。
 */
class ColorPickerPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var hue = 0f
    private var sat = 1f
    private var bright = 1f

    private val satBrightRect = RectF()
    private val hueRect = RectF()
    private val pad = dp(12f).toFloat()
    private val hueBarHeight = dp(24f).toFloat()
    private val thumbRadius = dp(8f).toFloat()

    private val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = dp(2f).toFloat()
    }

    /** 颜色变化回调。 */
    var onColorChange: ((Int) -> Unit)? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = (w * 0.75f).toInt()
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        satBrightRect.set(pad, pad, w - pad, h - pad - hueBarHeight - pad)
        hueRect.set(pad, h - pad - hueBarHeight, w - pad, h - pad)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制饱和度-亮度面板
        drawSatBright(canvas)
        // 绘制色相条
        drawHueBar(canvas)
        // 绘制 thumb
        val curColor = Color.HSVToColor(floatArrayOf(hue, sat, bright))
        val sx = satBrightRect.left + sat * satBrightRect.width()
        val sy = satBrightRect.top + (1 - bright) * satBrightRect.height()
        canvas.drawCircle(sx, sy, thumbRadius, thumbPaint)
        canvas.drawCircle(sx, sy, thumbRadius - dp(2f), Paint().apply { color = curColor })

        val hx = hueRect.left + (hue / 360f) * hueRect.width()
        canvas.drawCircle(hx, hueRect.centerY(), thumbRadius, thumbPaint)
    }

    private fun drawSatBright(canvas: Canvas) {
        // 横向：白 -> 纯色
        val baseColor = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        val grad = android.graphics.LinearGradient(
            satBrightRect.left, satBrightRect.top,
            satBrightRect.right, satBrightRect.top,
            Color.WHITE, baseColor, android.graphics.Shader.TileMode.CLAMP
        )
        outerPaint.shader = grad
        canvas.drawRect(satBrightRect, outerPaint)
        // 纵向：纯色 -> 黑
        val grad2 = android.graphics.LinearGradient(
            satBrightRect.left, satBrightRect.top,
            satBrightRect.left, satBrightRect.bottom,
            Color.TRANSPARENT, Color.BLACK, android.graphics.Shader.TileMode.CLAMP
        )
        outerPaint.shader = grad2
        canvas.drawRect(satBrightRect, outerPaint)
    }

    private fun drawHueBar(canvas: Canvas) {
        val colors = IntArray(7) { i ->
            Color.HSVToColor(floatArrayOf(i * 60f, 1f, 1f))
        }
        val grad = android.graphics.LinearGradient(
            hueRect.left, hueRect.top, hueRect.right, hueRect.top,
            colors, null, android.graphics.Shader.TileMode.CLAMP
        )
        outerPaint.shader = grad
        outerPaint.style = Paint.Style.FILL
        canvas.drawRoundRect(hueRect, hueBarHeight / 2, hueBarHeight / 2, outerPaint)
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        when (event.action) {
            android.view.MotionEvent.ACTION_DOWN,
            android.view.MotionEvent.ACTION_MOVE -> {
                when {
                    hueRect.contains(event.x, event.y) -> {
                        hue = ((event.x - hueRect.left) / hueRect.width() * 360f).coerceIn(0f, 360f)
                    }
                    satBrightRect.contains(event.x, event.y) -> {
                        sat = ((event.x - satBrightRect.left) / satBrightRect.width()).coerceIn(0f, 1f)
                        bright = 1f - ((event.y - satBrightRect.top) / satBrightRect.height()).coerceIn(0f, 1f)
                    }
                }
                onColorChange?.invoke(Color.HSVToColor(floatArrayOf(hue, sat, bright)))
                invalidate()
                return true
            }
        }
        return true
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
