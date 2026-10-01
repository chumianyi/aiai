/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 缓存清理组件：显示缓存大小文字，清理时旋转动画。
 */
class CacheCleanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var sizeText = "0 B"
    private var angle = 0f
    private var animator: ValueAnimator? = null

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(16f); color = 0xFF212121.toInt(); textAlign = Paint.Align.CENTER
    }
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = dp(3f).toFloat(); color = 0xFF3D5AFE.toInt()
    }

    fun setSize(text: String) { sizeText = text; invalidate() }

    /** 开始清理动画。 */
    fun startClean() {
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1500; repeatCount = ValueAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            addUpdateListener { angle = it.animatedValue as Float; invalidate() }
            start()
        }
    }

    fun stopClean() { animator?.cancel(); angle = 0f; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = dp(120f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f; val cy = height / 2f
        val r = dp(45f).toFloat()
        canvas.drawArc(cx - r, cy - r, cx + r, cy + r, angle, 270f, false, arcPaint)
        canvas.drawText(sizeText, cx, cy, textPaint)
    }

    override fun onDetachedFromWindow() { animator?.cancel(); super.onDetachedFromWindow() }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
