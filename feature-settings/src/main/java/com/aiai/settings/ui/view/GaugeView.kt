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

/**
 * 仪表盘 View：展示 API 健康度（0~100）。
 */
class GaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var value = 0f
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = dp(12f).toFloat(); strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER; textSize = sp(24f); isFakeBoldText = true
    }

    fun setValue(v: Float) { value = v; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val s = dp(160f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = dp(16f).toFloat()
        val rect = RectF(pad, pad, width - pad, height - pad)
        val ratio = value / 100f
        arcPaint.color = when {
            ratio > 0.8f -> 0xFF4CAF50.toInt()
            ratio > 0.5f -> 0xFFFF9800.toInt()
            else -> 0xFFF44336.toInt()
        }
        canvas.drawArc(rect, 180f, 180f, false, arcPaint.apply { alpha = 60 })
        canvas.drawArc(rect, 180f, 180f * ratio, false, arcPaint.apply { alpha = 255 })
        textPaint.color = arcPaint.color
        canvas.drawText("${value.toInt()}", width / 2f, height / 2f + sp(8f), textPaint)
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
