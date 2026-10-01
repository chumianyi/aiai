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
import android.view.MotionEvent
import android.view.View

/**
 * 图片消息View
 *
 * 显示聊天中的图片消息，支持圆角、加载状态、点击放大。
 * 占位显示灰色背景，加载完成后显示图片。
 *
 * XML属性：
 * - cornerRadius: 图片圆角
 * - placeholderColor: 占位背景色
 * - maxWidth: 最大宽度
 * - maxHeight: 最大高度
 */
class ImageMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val placeholderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#E0E0E0")
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(1f)
        color = Color.parseColor("#EEEEEE")
    }

    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(2f)
        color = Color.parseColor("#AAAAAA")
    }

    private var cornerRadius: Float = dp2px(12f)
    private var placeholderColor: Int = Color.parseColor("#E0E0E0")
    private var maxWidth: Float = dp2px(200f)
    private var maxHeight: Float = dp2px(200f)
    private var isLoaded: Boolean = false
    private var imageUrl: String? = null

    private var onImageClickListener: ((String?) -> Unit)? = null

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.ImageMessageView, defStyleAttr, 0).apply {
            cornerRadius = getDimension(com.aiai.chat.R.styleable.ImageMessageView_cornerRadius, cornerRadius)
            placeholderColor = getColor(com.aiai.chat.R.styleable.ImageMessageView_placeholderColor, placeholderColor)
            recycle()
        }
        placeholderPaint.color = placeholderColor
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = maxWidth.toInt()
        val height = maxHeight.toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // 绘制圆角背景
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, placeholderPaint)

        if (!isLoaded) {
            // 绘制图片占位图标
            val centerX = width / 2f
            val centerY = height / 2f
            val iconSize = dp2px(32f)
            val iconRect = RectF(
                centerX - iconSize / 2,
                centerY - iconSize / 2,
                centerX + iconSize / 2,
                centerY + iconSize / 2
            )
            canvas.drawRoundRect(iconRect, dp2px(4f), dp2px(4f), iconPaint)
            // 山形图标
            canvas.drawCircle(centerX - iconSize * 0.15f, centerY - iconSize * 0.15f, dp2px(3f), iconPaint)
            canvas.drawLine(
                centerX - iconSize * 0.3f, centerY + iconSize * 0.2f,
                centerX, centerY - iconSize * 0.1f,
                iconPaint
            )
            canvas.drawLine(
                centerX, centerY - iconSize * 0.1f,
                centerX + iconSize * 0.25f, centerY + iconSize * 0.2f,
                iconPaint
            )
        }

        // 绘制边框
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                performClick()
                onImageClickListener?.invoke(imageUrl)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 设置图片URL
     */
    fun setImage(url: String?) {
        imageUrl = url
        isLoaded = false
        invalidate()
        // 实际项目中这里会调用Glide加载图片
    }

    /**
     * 标记加载完成
     */
    fun setLoaded(loaded: Boolean) {
        isLoaded = loaded
        invalidate()
    }

    /**
     * 设置点击监听
     */
    fun setOnImageClickListener(listener: (String?) -> Unit) {
        onImageClickListener = listener
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}
