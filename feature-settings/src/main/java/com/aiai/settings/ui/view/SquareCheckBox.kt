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
 * 勾选框：方形 checkbox。
 */
class SquareCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var checked = false
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }

    fun setChecked(c: Boolean) { checked = c; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val s = dp(20f)
        super.onMeasure(MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(s, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (checked) {
            canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), dp(4f).toFloat(), dp(4f).toFloat(), paint)
        } else {
            paint.style = Paint.Style.STROKE; paint.strokeWidth = dp(1f).toFloat()
            canvas.drawRoundRect(1f, 1f, width - 1f, height - 1f, dp(4f).toFloat(), dp(4f).toFloat(), paint)
            paint.style = Paint.Style.FILL
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
