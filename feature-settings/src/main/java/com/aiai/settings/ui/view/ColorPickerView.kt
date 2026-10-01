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
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.aiai.settings.R
import com.aiai.settings.model.ColorPalette

/**
 * 颜色选择器：网格色板 + 最近使用颜色，支持触摸选中。
 */
class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val colors = ColorPalette.DEFAULT_COLORS.toMutableList()
    private val recent = mutableListOf<Int>()

    private var selectedColor = ColorPalette.DEFAULT_COLORS[0]
    private var cellSize = dp(40f)
    private var spacing = dp(8f)

    private val cellPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp(2f)
        color = 0xFF212121.toInt()
    }

    /** 选中回调。 */
    var onColorSelected: ((Int) -> Unit)? = null

    private val columns = 6

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.ColorPickerView)
            try {
                selectedColor = ta.getColor(R.styleable.ColorPickerView_cpDefaultColor, selectedColor)
                cellSize = ta.getDimension(R.styleable.ColorPickerView_cpCellSize, cellSize.toFloat()).toInt()
            } finally { ta.recycle() }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val rowCount = (colors.size + columns - 1) / columns
        val width = columns * cellSize + (columns - 1) * spacing + paddingLeft + paddingRight
        val height = rowCount * cellSize + (rowCount - 1) * spacing + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        colors.forEachIndexed { index, color ->
            val col = index % columns
            val row = index / columns
            val left = paddingLeft + col * (cellSize + spacing).toFloat()
            val top = paddingTop + row * (cellSize + spacing).toFloat()
            val rect = Rect(left.toInt(), top.toInt(), (left + cellSize).toInt(), (top + cellSize).toInt())
            cellPaint.color = color
            canvas.drawRoundRect(rect.left.toFloat(), rect.top.toFloat(),
                rect.right.toFloat(), rect.bottom.toFloat(), dp(8f), dp(8f), cellPaint)
            if (color == selectedColor) {
                canvas.drawRoundRect(rect.left.toFloat(), rect.top.toFloat(),
                    rect.right.toFloat(), rect.bottom.toFloat(), dp(8f), dp(8f), borderPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val col = ((event.x - paddingLeft) / (cellSize + spacing)).toInt()
            val row = ((event.y - paddingTop) / (cellSize + spacing)).toInt()
            val index = row * columns + col
            if (index in colors.indices) {
                selectedColor = colors[index]
                onColorSelected?.invoke(selectedColor)
                invalidate()
                return true
            }
        }
        return true
    }

    /** 添加最近使用的颜色。 */
    fun addRecent(color: Int) {
        if (!recent.contains(color)) {
            recent.add(0, color)
            if (recent.size > 6) recent.removeAt(recent.size - 1)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
