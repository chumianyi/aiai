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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.aiai.settings.R

/**
 * 分类标签横向 Tab：文字 + 下划线指示器，支持滑动/点选。
 */
class CategoryTabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val titles = mutableListOf<String>()
    private var selected = 0

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF9E9E9E.toInt(); textSize = sp(14f)
    }
    private val selectedTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3D5AFE.toInt(); textSize = sp(14f); isFakeBoldText = true
    }
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3D5AFE.toInt(); strokeWidth = dp(3f).toFloat()
    }

    private val padding = dp(16f)
    private val tabHeight = dp(40f)

    /** 选中回调。 */
    var onTabSelected: ((index: Int, title: String) -> Unit)? = null

    private val gesture = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val idx = ((e.x - paddingLeft) / (width - paddingLeft - paddingRight) * titles.size).toInt()
            if (idx in titles.indices) {
                setSelection(idx)
                onTabSelected?.invoke(idx, titles[idx])
            }
            return true
        }
    })

    fun setTabs(list: List<String>) {
        titles.clear(); titles.addAll(list)
        selected = 0
        requestLayout(); invalidate()
    }

    fun setSelection(idx: Int) {
        if (idx in titles.indices && idx != selected) {
            selected = idx; invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(tabHeight, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (titles.isEmpty()) return
        val tabWidth = (width - paddingLeft - paddingRight) / titles.size
        titles.forEachIndexed { i, title ->
            val cx = paddingLeft + tabWidth * i + tabWidth / 2f
            val paint = if (i == selected) selectedTextPaint else textPaint
            canvas.drawText(title, cx - paint.measureText(title) / 2, height / 2f, paint)
            if (i == selected) {
                canvas.drawLine(cx - tabWidth / 4, height - dp(4f).toFloat(),
                    cx + tabWidth / 4, height - dp(4f).toFloat(), indicatorPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gesture.onTouchEvent(event)
        return true
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
