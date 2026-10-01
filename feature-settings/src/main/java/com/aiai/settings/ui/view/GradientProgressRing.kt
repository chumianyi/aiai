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
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 环形进度条（带渐变色）：用于导出/清理进度。
 */
class GradientProgressRing @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var progress = 0f
    private var max = 100f
    private val stroke = dp(10f).toFloat()

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = stroke
        color = 0xFFE0E0E0.toInt()
    }
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = stroke
        strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER; textSize = sp(18f); color = 0xFF212121.toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = dp(140f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY))
    }

    /** 设置进度并播放动画。 */
    fun setProgressAnimated(target: Float) {
        val anim = android.animation.ValueAnimator.ofFloat(progress, target)
        anim.duration = 400
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { progress = it.animatedValue as Float; invalidate() }
        anim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = stroke / 2 + dp(6f)
        val rect = RectF(pad, pad, width - pad, height - pad)
        canvas.drawArc(rect, 0f, 360f, false, trackPaint)

        // 渐变色弧
        val colors = intArrayOf(0xFF3D5AFE.toInt(), 0xFF00ACC1.toInt(), 0xFF4CAF50.toInt())
        arcPaint.shader = android.graphics.SweepGradient(
            width / 2f, height / 2f, colors, null
        )
        val sweep = 360f * (progress / max)
        canvas.drawArc(rect, -90f, sweep, false, arcPaint)

        val pct = (progress / max * 100).toInt()
        canvas.drawText("$pct%", width / 2f, height / 2f + sp(6f), textPaint)
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
