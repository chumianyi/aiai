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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 使用统计柱状图：展示最近 7 天提示词使用次数。
 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var data = listOf<Float>()
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE0E0E0.toInt(); strokeWidth = dp(1f).toFloat()
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(10f); color = 0xFF9E9E9E.toInt(); textAlign = Paint.Align.CENTER
    }

    fun setData(values: List<Float>) { data = values; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(120f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return
        val max = data.maxOrNull() ?: 1f
        val pad = dp(8f).toFloat()
        val barW = (width - pad * 2) / data.size
        // 网格线
        for (i in 0..3) {
            val y = pad + (height - pad * 2) * i / 3
            canvas.drawLine(pad, y, width - pad, y, gridPaint)
        }
        data.forEachIndexed { i, v ->
            val barH = (v / max) * (height - pad * 2)
            val left = pad + i * barW + barW * 0.2f
            val right = pad + (i + 1) * barW - barW * 0.2f
            canvas.drawRect(left, height - pad - barH, right, height - pad, barPaint)
            canvas.drawText(v.toInt().toString(), (left + right) / 2, height - dp(2f), textPaint)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
