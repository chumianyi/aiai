/*
 * Copyright (c) 2026 爱Ai (AiAi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.chat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 空状态视图
 *
 * 显示图标+文字+按钮的空状态占位视图。
 * 用于会话列表为空、搜索无结果等场景。
 *
 * XML属性：
 * - emptyIcon: 图标资源
 * - emptyText: 提示文字
 * - textColor: 文字颜色
 * - iconTint: 图标着色
 */
class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#F5F5F5")
    }

    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#CCCCCC")
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#999999")
        textSize = sp2px(14f)
        textAlign = Paint.Align.CENTER
    }

    private var emptyText: String = "暂无数据"
    private var iconSize: Float = dp2px(64f)
    private var textSpacing: Float = dp2px(16f)

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.EmptyStateView, defStyleAttr, 0).apply {
            emptyText = getString(com.aiai.chat.R.styleable.EmptyStateView_emptyText) ?: emptyText
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = dp2px(200f).toInt()
        val desiredHeight = (iconSize + textSpacing + sp2px(20f)).toInt()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredWidth, widthSize)
            else -> desiredWidth
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val totalHeight = iconSize + textSpacing + sp2px(20f)
        val startY = (height - totalHeight) / 2f

        // 绘制图标占位（圆角矩形模拟）
        val iconRect = RectF(
            centerX - iconSize / 2,
            startY,
            centerX + iconSize / 2,
            startY + iconSize
        )
        canvas.drawRoundRect(iconRect, dp2px(12f), dp2px(12f), iconPaint)

        // 绘制文字
        val textY = startY + iconSize + textSpacing + sp2px(14f)
        canvas.drawText(emptyText, centerX, textY, textPaint)
    }

    /**
     * 设置提示文字
     */
    fun setEmptyText(text: String) {
        emptyText = text
        invalidate()
    }

    /**
     * 设置图标颜色
     */
    fun setIconColor(color: Int) {
        iconPaint.color = color
        invalidate()
    }

    /**
     * 设置文字颜色
     */
    fun setTextColor(color: Int) {
        textPaint.color = color
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
}
