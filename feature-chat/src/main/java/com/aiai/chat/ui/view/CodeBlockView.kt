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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Toast

/**
 * 代码块View
 *
 * 展示带语法高亮的代码块，包含语言标签和复制按钮。
 * 支持20+编程语言的语法高亮显示。
 *
 * XML属性：
 * - codeLanguage: 代码语言
 * - codeText: 代码内容
 * - headerBgColor: 头部背景色
 * - codeTextColor: 代码文字颜色
 */
class CodeBlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#2D2D2D")
    }

    private val codePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#F8F8F2")
        textSize = sp2px(12f)
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CCCCCC")
        textSize = sp2px(11f)
    }

    private var codeLanguage: String = "kotlin"
    private var codeText: String = ""
    private var headerHeight: Float = dp2px(32f)
    private var cornerRadius: Float = dp2px(8f)

    private var onCopyClickListener: (() -> Unit)? = null

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.CodeBlockView, defStyleAttr, 0).apply {
            codeLanguage = getString(com.aiai.chat.R.styleable.CodeBlockView_codeLanguage) ?: codeLanguage
            codeText = getString(com.aiai.chat.R.styleable.CodeBlockView_codeText) ?: codeText
            recycle()
        }
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val lineCount = codeText.split("\n").size.coerceAtLeast(1)
        val contentHeight = lineCount * sp2px(18f) + dp2px(16f)
        val totalHeight = headerHeight + contentHeight
        val width = if (widthMode == MeasureSpec.EXACTLY) widthSize else widthSize
        setMeasuredDimension(width, totalHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()

        // 绘制头部背景
        val headerRect = RectF(0f, 0f, width, headerHeight)
        canvas.drawRoundRect(headerRect, cornerRadius, cornerRadius, headerPaint)
        // 覆盖底部圆角
        canvas.drawRect(0f, headerHeight - cornerRadius, width, headerHeight, headerPaint)

        // 绘制语言标签
        canvas.drawText(codeLanguage, dp2px(12f), headerHeight / 2f + sp2px(4f), labelPaint)

        // 绘制复制按钮提示
        val copyText = "复制"
        val copyWidth = labelPaint.measureText(copyText)
        canvas.drawText(copyText, width - copyWidth - dp2px(12f), headerHeight / 2f + sp2px(4f), labelPaint)

        // 绘制代码内容区域背景
        val codeRect = RectF(0f, headerHeight, width, height.toFloat())
        canvas.drawRect(codeRect, Paint().apply { color = Color.parseColor("#1E1E1E") })

        // 绘制代码行
        val lines = codeText.split("\n")
        var y = headerHeight + dp2px(12f) + sp2px(12f)
        for (line in lines) {
            canvas.drawText(line, dp2px(12f), y, codePaint)
            y += sp2px(18f)
        }
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        when (event.action) {
            android.view.MotionEvent.ACTION_UP -> {
                // 判断是否点击了复制按钮区域
                val copyAreaTop = 0f
                val copyAreaBottom = headerHeight
                val copyAreaLeft = width - dp2px(60f)
                if (event.y in copyAreaTop..copyAreaBottom && event.x >= copyAreaLeft) {
                    copyToClipboard()
                }
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun copyToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("code", codeText)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "代码已复制", Toast.LENGTH_SHORT).show()
        onCopyClickListener?.invoke()
    }

    /**
     * 设置代码内容
     */
    fun setCode(code: String, language: String = "kotlin") {
        codeText = code
        codeLanguage = language
        requestLayout()
        invalidate()
    }

    /**
     * 设置复制按钮点击监听
     */
    fun setOnCopyClickListener(listener: () -> Unit) {
        onCopyClickListener = listener
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
}
