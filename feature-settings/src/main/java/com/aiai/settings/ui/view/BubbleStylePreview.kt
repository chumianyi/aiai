/*
 * Copyright (c) 爱Ai (AiAi) Project
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
import android.view.View
import com.aiai.settings.model.BubbleStyle

/**
 * 气泡样式预览：实时绘制对方/我方两种气泡。
 */
class BubbleStylePreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var style = BubbleStyle.ROUNDED

    private val otherPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFECEFF1.toInt() }
    private val myPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sp(13f); color = 0xFF212121.toInt()
    }

    fun setStyle(s: BubbleStyle) { style = s; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(120f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = dp(12f).toFloat()
        val radius = when (style) {
            BubbleStyle.ROUNDED -> dp(12f).toFloat()
            BubbleStyle.SHARP -> 0f
            BubbleStyle.SHADOW -> dp(16f).toFloat()
            BubbleStyle.TRANSPARENT -> dp(8f).toFloat()
            BubbleStyle.MINIMAL -> dp(4f).toFloat()
        }
        if (style == BubbleStyle.TRANSPARENT) {
            otherPaint.alpha = 140; myPaint.alpha = 180
        }

        // 对方气泡（左）
        val other = RectF(pad, pad, width * 0.6f, pad + dp(36f))
        canvas.drawRoundRect(other, radius, radius, otherPaint)
        canvas.drawText("你好，我是 AI 助手", other.left + dp(10f), other.centerY() + sp(4f), textPaint)

        // 我方气泡（右）
        val my = RectF(width * 0.4f, pad + dp(48f), width - pad, pad + dp(48f) + dp(36f))
        val myTextPaint = Paint(textPaint).apply { color = 0xFFFFFFFF.toInt() }
        canvas.drawRoundRect(my, radius, radius, myPaint)
        canvas.drawText("帮我写一段文案", my.left + dp(10f), my.centerY() + sp(4f), myTextPaint)
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
