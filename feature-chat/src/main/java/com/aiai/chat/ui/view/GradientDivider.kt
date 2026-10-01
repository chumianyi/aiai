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
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 渐变分割线View
 *
 * 实现从透明到颜色再到透明的水平渐变分割线。
 * 用于会话列表、聊天页面中的视觉分隔。
 *
 * XML属性：
 * - gradientColor: 渐变中间颜色
 * - lineHeight: 分割线高度
 */
class GradientDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientColor: Int = Color.parseColor("#DDDDDD")
    private var lineHeight: Float = dp2px(1f)

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.GradientDivider, defStyleAttr, 0).apply {
            gradientColor = getColor(com.aiai.chat.R.styleable.GradientDivider_gradientColor, gradientColor)
            lineHeight = getDimension(com.aiai.chat.R.styleable.GradientDivider_lineHeight, lineHeight)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = lineHeight.toInt()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = if (widthMode == MeasureSpec.EXACTLY) widthSize else widthSize
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val shader = LinearGradient(
            0f, 0f, width.toFloat(), 0f,
            intArrayOf(
                Color.TRANSPARENT,
                gradientColor,
                gradientColor,
                Color.TRANSPARENT
            ),
            floatArrayOf(0f, 0.2f, 0.8f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    /**
     * 设置渐变颜色
     */
    fun setGradientColor(color: Int) {
        gradientColor = color
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}
