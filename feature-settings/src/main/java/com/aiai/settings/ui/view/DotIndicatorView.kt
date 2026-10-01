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
import com.aiai.settings.R

/**
 * 引导页指示器：多个圆点，当前页放大变色。
 */
class DotIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var count = 3
    private var position = 0
    private var offset = 0f

    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFBDBDBD.toInt() }
    private val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }

    private val normalR = dp(4f).toFloat()
    private val selectedR = dp(6f).toFloat()
    private val gap = dp(10f).toFloat()

    fun setCount(n: Int) { count = n; requestLayout(); invalidate() }

    /** ViewPager2 页面滚动回调。 */
    fun onPageScrolled(position: Int, positionOffset: Float) {
        this.position = position
        this.offset = positionOffset
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = (count * (selectedR * 2) + (count - 1) * gap).toInt()
        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(dp(16f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val totalW = count * selectedR * 2 + (count - 1) * gap
        var cx = (width - totalW) / 2f + selectedR
        val cy = height / 2f
        for (i in 0 until count) {
            val isCurrent = i == position || i == position + 1
            val r = if (i == position) selectedR else normalR
            val paint = if (i == position) selectedPaint else normalPaint
            canvas.drawCircle(cx, cy, r, paint)
            cx += selectedR * 2 + gap
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
