/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law on an "AS IS" BASIS,
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
import com.aiai.settings.R

/**
 * 环形进度条：用于导出 / 清理进度。
 */
class ProgressRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var strokeWidth = dp(8f).toFloat()
    private var progressColor = 0xFF3D5AFE.toInt()
    private var trackColor = 0xFFE0E0E0.toInt()
    private var max = 100f
    private var progress = 0f
    private var showText = true

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; this.strokeWidth = this@ProgressRingView.strokeWidth
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; this.strokeWidth = this@ProgressRingView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(16f); textAlign = Paint.Align.CENTER; color = 0xFF212121.toInt()
    }

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.ProgressRingView)
            try {
                strokeWidth = ta.getDimension(R.styleable.ProgressRingView_ringStrokeWidth, strokeWidth)
                progressColor = ta.getColor(R.styleable.ProgressRingView_ringProgressColor, progressColor)
                trackColor = ta.getColor(R.styleable.ProgressRingView_ringTrackColor, trackColor)
                max = ta.getFloat(R.styleable.ProgressRingView_ringMax, max)
                showText = ta.getBoolean(R.styleable.ProgressRingView_ringShowText, true)
            } finally { ta.recycle() }
        }
        trackPaint.color = trackColor
        progressPaint.color = progressColor
    }

    /** 设置进度 0~[max]。 */
    fun setProgress(p: Float) { progress = p.coerceIn(0f, max); invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = dp(120f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = strokeWidth / 2 + dp(4f)
        val rect = RectF(pad, pad, width - pad, height - pad)
        canvas.drawArc(rect, 0f, 360f, false, trackPaint)
        val sweep = 360f * (progress / max)
        canvas.drawArc(rect, -90f, sweep, false, progressPaint)
        if (showText) {
            val pct = (progress / max * 100).toInt()
            canvas.drawText("$pct%", width / 2f, height / 2f + sp(5f), textPaint)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
