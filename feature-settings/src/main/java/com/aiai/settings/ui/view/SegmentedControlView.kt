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
 * 分段控制器：用于深色模式三态切换（跟随系统/开/关）。
 */
class SegmentedControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val options = listOf("跟随系统", "浅色", "深色")
    private var selected = 0
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFECEFF1.toInt() }
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(13f); color = 0xFF616161.toInt(); textAlign = Paint.Align.CENTER
    }
    private val selectedTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(13f); color = 0xFF212121.toInt(); textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    var onSelect: ((Int) -> Unit)? = null

    fun setSelected(idx: Int) { selected = idx; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(36f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = dp(3f).toFloat()
        val rect = RectF(pad, pad, width - pad, height - pad)
        canvas.drawRoundRect(rect, height / 2, height / 2, bgPaint)
        val segW = (width - pad * 2) / options.size
        val thumb = RectF(pad + selected * segW + 1, pad + 1, pad + (selected + 1) * segW - 1, height - pad - 1)
        canvas.drawRoundRect(thumb, height / 2, height / 2, thumbPaint)
        options.forEachIndexed { i, opt ->
            val cx = pad + segW * i + segW / 2
            val paint = if (i == selected) selectedTextPaint else textPaint
            canvas.drawText(opt, cx, height / 2f + sp(4f), paint)
        }
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        if (event.action == android.view.MotionEvent.ACTION_UP) {
            val segW = width / options.size
            val idx = (event.x / segW).toInt().coerceIn(0, options.size - 1)
            if (idx != selected) { selected = idx; invalidate(); onSelect?.invoke(idx) }
            return true
        }
        return true
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
