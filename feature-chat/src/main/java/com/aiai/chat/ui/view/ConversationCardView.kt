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
 * 会话卡片View
 *
 * 显示会话列表中的单个会话卡片，包含标题、预览、时间、未读数、置顶标记。
 *
 * XML属性：
 * - cardTitle: 会话标题
 * - cardPreview: 消息预览
 * - cardTime: 时间显示
 * - unreadCount: 未读数
 * - isPinned: 是否置顶
 */
class ConversationCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        textSize = sp2px(15f)
        isFakeBoldText = true
    }

    private val previewPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#999999")
        textSize = sp2px(13f)
    }

    private val timePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#BBBBBB")
        textSize = sp2px(11f)
    }

    private val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#FF3B30")
    }

    private val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = sp2px(10f)
        textAlign = Paint.Align.CENTER
    }

    private var cardTitle: String = "新对话"
    private var cardPreview: String = ""
    private var cardTime: String = ""
    private var unreadCount: Int = 0
    private var isPinned: Boolean = false
    private var cornerRadius: Float = dp2px(12f)

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.ConversationCardView, defStyleAttr, 0).apply {
            cardTitle = getString(com.aiai.chat.R.styleable.ConversationCardView_cardTitle) ?: cardTitle
            cardPreview = getString(com.aiai.chat.R.styleable.ConversationCardView_cardPreview) ?: cardPreview
            cardTime = getString(com.aiai.chat.R.styleable.ConversationCardView_cardTime) ?: cardTime
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val height = dp2px(72f).toInt()
        setMeasuredDimension(widthSize, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // 绘制卡片背景
        val cardRect = RectF(dp2px(12f), dp2px(6f), w - dp2px(12f), h - dp2px(6f))
        canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, bgPaint)

        val padding = dp2px(16f)
        val left = cardRect.left + padding
        val right = cardRect.right - padding

        // 绘制置顶标记
        if (isPinned) {
            val pinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#6C63FF") }
            canvas.drawRect(left, cardRect.top, left + dp2px(3f), cardRect.bottom, pinPaint)
        }

        // 绘制标题
        canvas.drawText(cardTitle, left + dp2px(4f), cardRect.top + dp2px(24f), titlePaint)

        // 绘制预览
        val preview = if (cardPreview.length > 30) cardPreview.take(30) + "..." else cardPreview
        canvas.drawText(preview, left + dp2px(4f), cardRect.top + dp2px(48f), previewPaint)

        // 绘制时间
        val timeWidth = timePaint.measureText(cardTime)
        canvas.drawText(cardTime, right - timeWidth, cardRect.top + dp2px(20f), timePaint)

        // 绘制未读角标
        if (unreadCount > 0) {
            val badgeText = if (unreadCount > 99) "99+" else unreadCount.toString()
            val badgeWidth = badgePaint.measureText(badgeText) + dp2px(12f)
            val badgeHeight = dp2px(18f)
            val badgeRect = RectF(
                right - badgeWidth,
                cardRect.top + dp2px(28f),
                right,
                cardRect.top + dp2px(28f) + badgeHeight
            )
            canvas.drawRoundRect(badgeRect, badgeHeight / 2, badgeHeight / 2, badgePaint)
            canvas.drawText(
                badgeText,
                badgeRect.centerX(),
                badgeRect.centerY() + sp2px(4f),
                badgeTextPaint
            )
        }
    }

    /**
     * 设置卡片数据
     */
    fun setCardData(title: String, preview: String, time: String, unread: Int = 0, pinned: Boolean = false) {
        cardTitle = title
        cardPreview = preview
        cardTime = time
        unreadCount = unread
        isPinned = pinned
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
}
