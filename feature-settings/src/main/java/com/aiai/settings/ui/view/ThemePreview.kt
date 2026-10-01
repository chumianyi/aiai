/*
 * Copyright (c) 爱Ai (AiAi) Project
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
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.aiai.settings.model.ThemeMode

/**
 * 主题预览：绘制一个手机外框，内部按 浅色/深色 渲染。
 */
class ThemePreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var mode = ThemeMode.SYSTEM

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFBDBDBD.toInt() }
    private val lightBg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF5F6FA.toInt() }
    private val darkBg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF121212.toInt() }
    private val cardLight = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() }
    private val cardDark = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF1E1E1E.toInt() }
    private val accent = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF3D5AFE.toInt() }

    fun setMode(m: ThemeMode) { mode = m; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(160f), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width * 0.4f
        val h = height * 0.9f
        // 两个手机并排：左浅色，右深色
        drawPhone(canvas, dp(12f).toFloat(), height / 2f - h / 2, w, h, dark = false)
        drawPhone(canvas, width - w - dp(12f), height / 2f - h / 2, w, h, dark = true)
    }

    private fun drawPhone(canvas: Canvas, left: Float, top: Float, w: Float, h: Float, dark: Boolean) {
        canvas.drawRoundRect(left, top, left + w, top + h, dp(12f).toFloat(), dp(12f).toFloat(), framePaint)
        val bg = if (dark) darkBg else lightBg
        val card = if (dark) cardDark else cardLight
        canvas.drawRoundRect(left + 4f, top + 4f, left + w - 4f, top + h - 4f, dp(10f).toFloat(), dp(10f).toFloat(), bg)
        // 状态栏
        canvas.drawRect(left + 12f, top + 12f, left + w - 40f, top + 18f, accent)
        // 卡片
        repeat(3) {
            val topCard = top + 30f + it * 40f
            canvas.drawRoundRect(left + 12f, topCard, left + w - 12f, topCard + 30f, dp(6f).toFloat(), dp(6f).toFloat(), card)
        }
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
}
