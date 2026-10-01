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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.aiai.settings.R

/**
 * 滑块组件：标题 + 当前数值 + 轨道 + thumb，支持步进。
 */
class SliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var title = ""
    private var min = 0f
    private var max = 100f
    private var step = 1f
    private var value = 50f
    private var unit = ""

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFE0E0E0.toInt() }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(14f)
        color = 0xFF212121.toInt()
    }
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(14f)
        color = 0xFF3D5AFE.toInt()
        textAlign = Paint.Align.RIGHT
    }

    private val thumbRadius = dp(10f)
    private val trackHeight = dp(4f).toFloat()

    /** 值变化回调。 */
    var onValueChange: ((Float) -> Unit)? = null

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.SliderView)
            try {
                title = ta.getString(R.styleable.SliderView_sliderTitle) ?: ""
                min = ta.getFloat(R.styleable.SliderView_sliderMin, min)
                max = ta.getFloat(R.styleable.SliderView_sliderMax, max)
                step = ta.getFloat(R.styleable.SliderView_sliderStep, step)
                value = ta.getFloat(R.styleable.SliderView_sliderValue, value)
                unit = ta.getString(R.styleable.SliderView_sliderUnit) ?: ""
            } finally { ta.recycle() }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = dp(56f)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = paddingLeft.toFloat() + thumbRadius
        val right = width - paddingRight - thumbRadius
        val cy = height / 2f + dp(6f)

        // 标题
        canvas.drawText(title, paddingLeft.toFloat(), height / 2f - dp(8f), textPaint)
        // 当前值
        canvas.drawText("${value.toInt()}$unit", width - paddingRight.toFloat(), height / 2f - dp(8f), valuePaint)

        // 轨道
        val track = RectF(left, cy - trackHeight / 2, right, cy + trackHeight / 2)
        canvas.drawRoundRect(track, trackHeight, trackHeight, trackPaint)

        // 已选进度
        val ratio = (value - min) / (max - min)
        val progress = left + (right - left) * ratio
        val progressRect = RectF(left, cy - trackHeight / 2, progress, cy + trackHeight / 2)
        canvas.drawRoundRect(progressRect, trackHeight, trackHeight, progressPaint)

        // thumb
        canvas.drawCircle(progress, cy, thumbRadius.toFloat(), thumbPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val left = paddingLeft.toFloat() + thumbRadius
                val right = width - paddingRight - thumbRadius
                val ratio = ((event.x - left) / (right - left)).coerceIn(0f, 1f)
                var v = min + (max - min) * ratio
                // 对齐到 step
                v = (Math.round(v / step) * step)
                value = v.coerceIn(min, max)
                invalidate()
                onValueChange?.invoke(value)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setValue(v: Float) { value = v.coerceIn(min, max); invalidate() }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
