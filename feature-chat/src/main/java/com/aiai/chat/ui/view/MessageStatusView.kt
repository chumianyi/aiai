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
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.aiai.chat.R
import com.aiai.chat.data.enums.MessageStatus

/**
 * 消息状态指示器View
 *
 * 显示消息的发送状态：发送中（转圈）、成功（对勾）、失败（感叹号）、已读（双对勾）。
 *
 * XML属性：
 * - statusColor: 状态图标颜色
 * - iconSize: 图标尺寸
 */
class MessageStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(1.5f)
        color = Color.GRAY
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }

    private var statusColor: Int = Color.GRAY
    private var iconSize: Float = dp2px(16f)
    private var currentStatus: MessageStatus = MessageStatus.SENT

    private val checkPath = Path()
    private val circlePath = Path()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MessageStatusView, defStyleAttr, 0).apply {
            statusColor = getColor(R.styleable.MessageStatusView_statusColor, statusColor)
            iconSize = getDimension(R.styleable.MessageStatusView_iconSize, iconSize)
            recycle()
        }
        paint.color = statusColor
        fillPaint.color = statusColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = iconSize.toInt() + dp2px(4f).toInt()
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val halfSize = iconSize / 2f

        when (currentStatus) {
            MessageStatus.SENDING -> drawSending(canvas, centerX, centerY, halfSize)
            MessageStatus.SENT -> drawSingleCheck(canvas, centerX, centerY, halfSize)
            MessageStatus.READ -> drawDoubleCheck(canvas, centerX, centerY, halfSize)
            MessageStatus.FAILED -> drawFailed(canvas, centerX, centerY, halfSize)
            MessageStatus.STREAMING -> drawStreaming(canvas, centerX, centerY, halfSize)
            else -> {}
        }
    }

    private fun drawSending(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        paint.style = Paint.Style.STROKE
        paint.alpha = 180
        canvas.drawCircle(cx, cy, r, paint)
        paint.alpha = 255
        // 小圆弧表示加载中
        val sweepAngle = 120f
        canvas.drawArc(cx - r, cy - r, cx + r, cy + r, -90f, sweepAngle, false, paint)
    }

    private fun drawSingleCheck(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        paint.style = Paint.Style.STROKE
        checkPath.reset()
        checkPath.moveTo(cx - r * 0.5f, cy)
        checkPath.lineTo(cx - r * 0.1f, cy + r * 0.4f)
        checkPath.lineTo(cx + r * 0.5f, cy - r * 0.3f)
        canvas.drawPath(checkPath, paint)
    }

    private fun drawDoubleCheck(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        paint.style = Paint.Style.STROKE
        // 第一个对勾
        checkPath.reset()
        checkPath.moveTo(cx - r * 0.6f, cy)
        checkPath.lineTo(cx - r * 0.2f, cy + r * 0.35f)
        checkPath.lineTo(cx + r * 0.2f, cy - r * 0.25f)
        canvas.drawPath(checkPath, paint)
        // 第二个对勾（偏移）
        checkPath.reset()
        checkPath.moveTo(cx - r * 0.2f, cy + r * 0.1f)
        checkPath.lineTo(cx + r * 0.2f, cy + r * 0.45f)
        checkPath.lineTo(cx + r * 0.6f, cy - r * 0.15f)
        canvas.drawPath(checkPath, paint)
    }

    private fun drawFailed(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        // 感叹号
        canvas.drawLine(cx, cy - r * 0.5f, cx, cy + r * 0.1f, paint)
        fillPaint.color = Color.RED
        canvas.drawCircle(cx, cy + r * 0.4f, dp2px(1f), fillPaint)
        paint.color = statusColor
        fillPaint.color = statusColor
    }

    private fun drawStreaming(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        paint.style = Paint.Style.STROKE
        // 三个点表示流式输出中
        for (i in 0 until 3) {
            val x = cx - r * 0.5f + i * r * 0.5f
            canvas.drawCircle(x, cy, dp2px(1.5f), fillPaint)
        }
    }

    /**
     * 设置消息状态
     */
    fun setStatus(status: MessageStatus) {
        currentStatus = status
        invalidate()
    }

    /**
     * 设置状态颜色
     */
    fun setStatusColor(color: Int) {
        statusColor = color
        paint.color = color
        fillPaint.color = color
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        )
    }
}
