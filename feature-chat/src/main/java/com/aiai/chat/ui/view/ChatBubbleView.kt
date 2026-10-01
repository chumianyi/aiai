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
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.aiai.chat.R

/**
 * 聊天气泡View
 *
 * 实现用户和AI两种样式的聊天气泡，支持圆角、尾巴、背景色自定义。
 * 通过自定义属性设置气泡颜色、圆角大小、尾巴方向等。
 *
 * XML属性：
 * - bubbleColor: 气泡背景色
 * - cornerRadius: 圆角半径
 * - showTail: 是否显示尾巴
 * - tailPosition: 尾巴位置（左/右）
 * - isUserBubble: 是否为用户气泡
 */
class ChatBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val tailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var bubbleColor: Int = Color.parseColor("#E8E8E8")
    private var cornerRadius: Float = dp2px(16f)
    private var showTail: Boolean = true
    private var tailOnLeft: Boolean = true
    private var tailWidth: Float = dp2px(12f)
    private var tailHeight: Float = dp2px(16f)

    private val bubbleRect = RectF()
    private val tailPath = Path()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ChatBubbleView, defStyleAttr, 0).apply {
            bubbleColor = getColor(R.styleable.ChatBubbleView_bubbleColor, bubbleColor)
            cornerRadius = getDimension(R.styleable.ChatBubbleView_cornerRadius, cornerRadius)
            showTail = getBoolean(R.styleable.ChatBubbleView_showTail, showTail)
            tailOnLeft = getBoolean(R.styleable.ChatBubbleView_tailOnLeft, tailOnLeft)
            recycle()
        }
        bubblePaint.color = bubbleColor
        tailPaint.color = bubbleColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = dp2px(200f).toInt()
        val desiredHeight = dp2px(80f).toInt()

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
        val width = width.toFloat()
        val height = height.toFloat()

        val tailOffset = if (showTail) tailWidth else 0f
        val left = if (tailOnLeft) tailOffset else 0f
        val right = if (tailOnLeft) width else width - tailOffset
        val top = 0f
        val bottom = height

        bubbleRect.set(left, top, right, bottom)
        canvas.drawRoundRect(bubbleRect, cornerRadius, cornerRadius, bubblePaint)

        if (showTail) {
            drawTail(canvas, width, height)
        }
    }

    private fun drawTail(canvas: Canvas, width: Float, height: Float) {
        tailPath.reset()
        if (tailOnLeft) {
            tailPath.moveTo(0f, height * 0.3f)
            tailPath.lineTo(tailWidth, height * 0.3f + cornerRadius * 0.5f)
            tailPath.lineTo(0f, height * 0.3f + tailHeight)
            tailPath.close()
        } else {
            tailPath.moveTo(width, height * 0.3f)
            tailPath.lineTo(width - tailWidth, height * 0.3f + cornerRadius * 0.5f)
            tailPath.lineTo(width, height * 0.3f + tailHeight)
            tailPath.close()
        }
        canvas.drawPath(tailPath, tailPaint)
    }

    /**
     * 设置气泡颜色
     */
    fun setBubbleColor(color: Int) {
        bubbleColor = color
        bubblePaint.color = color
        tailPaint.color = color
        invalidate()
    }

    /**
     * 设置圆角半径
     */
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }

    /**
     * 设置尾巴位置
     */
    fun setTailOnLeft(left: Boolean) {
        tailOnLeft = left
        invalidate()
    }

    /**
     * 设置是否显示尾巴
     */
    fun setShowTail(show: Boolean) {
        showTail = show
        requestLayout()
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        )
    }
}
