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
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 波形可视化 View：绘制声波曲线（语音设置预览用）。
 */
class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bars = FloatArray(40) { 0.2f }
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF00ACC1.toInt() }
    private var animateOffset = 0f

    fun setLevels(levels: FloatArray) {
        for (i in bars.indices) bars[i] = levels[i % levels.size].coerceIn(0.1f, 1f)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(60f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barW = width.toFloat() / bars.size
        bars.forEachIndexed { i, level ->
            val h = level * height
            val left = i * barW
            val paint = if (i < bars.size / 2) progressPaint else barPaint
            canvas.drawRoundRect(left + 1, (height - h) / 2, left + barW - 1, (height + h) / 2,
                barW / 4, barW / 4, paint)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
