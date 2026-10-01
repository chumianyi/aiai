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
 * 密码强度指示器：分段条形，颜色随强度变化。
 */
class PasswordStrengthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var strength = 0 // 0~4
    private val paints = listOf(
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF44336.toInt() },
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFF9800.toInt() },
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFEB3B.toInt() },
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF8BC34A.toInt() },
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF4CAF50.toInt() }
    )
    private val emptyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFE0E0E0.toInt() }

    fun setStrength(level: Int) { strength = level.coerceIn(0, 4); invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(6f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val segW = width / 4f
        for (i in 0 until 4) {
            val paint = if (i < strength) paints[strength] else emptyPaint
            canvas.drawRoundRect(i * segW + 2, 0, (i + 1) * segW - 2, height.toFloat(), 3f, 3f, paint)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
