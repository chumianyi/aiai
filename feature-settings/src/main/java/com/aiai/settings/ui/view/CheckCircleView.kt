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
 * 选中标记：对勾圆圈。
 */
class CheckCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var checked = false
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = dp(2f).toFloat()
    }
    private val checkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = dp(2f).toFloat()
    }

    fun setChecked(c: Boolean) { checked = c; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val s = dp(24f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = width / 2f - dp(2f)
        if (checked) {
            circlePaint.color = 0xFF3D5AFE.toInt()
            canvas.drawCircle(width / 2f, height / 2f, r, circlePaint)
            checkPaint.color = 0xFF3D5AFE.toInt()
            canvas.drawLine(width / 2 - r / 2, height / 2f, width / 2 - 2, height / 2 + r / 3, checkPaint)
            canvas.drawLine(width / 2 - 2, height / 2 + r / 3, width / 2 + r / 2, height / 2 - r / 3, checkPaint)
        } else {
            circlePaint.color = 0xFFBDBDBD.toInt()
            canvas.drawCircle(width / 2f, height / 2f, r, circlePaint)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
