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
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout

/**
 * 圆角布局容器
 *
 * 支持设置四个角独立的圆角半径，作为容器包裹子View。
 * 用于聊天气泡、卡片等需要圆角裁剪的场景。
 *
 * XML属性：
 * - cornerRadius: 统一圆角半径
 * - topLeftRadius: 左上圆角
 * - topRightRadius: 右上圆角
 * - bottomLeftRadius: 左下圆角
 * - bottomRightRadius: 右下圆角
 * - clipContent: 是否裁剪子内容
 */
class RoundedCornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var topLeftRadius: Float = dp2px(16f)
    private var topRightRadius: Float = dp2px(16f)
    private var bottomLeftRadius: Float = dp2px(16f)
    private var bottomRightRadius: Float = dp2px(16f)
    private var clipContent: Boolean = true

    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val outlinePath = Path()

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.RoundedCornerLayout, defStyleAttr, 0).apply {
            val defaultRadius = getDimension(com.aiai.chat.R.styleable.RoundedCornerLayout_cornerRadius, -1f)
            if (defaultRadius >= 0) {
                topLeftRadius = defaultRadius
                topRightRadius = defaultRadius
                bottomLeftRadius = defaultRadius
                bottomRightRadius = defaultRadius
            } else {
                topLeftRadius = getDimension(com.aiai.chat.R.styleable.RoundedCornerLayout_topLeftRadius, topLeftRadius)
                topRightRadius = getDimension(com.aiai.chat.R.styleable.RoundedCornerLayout_topRightRadius, topRightRadius)
                bottomLeftRadius = getDimension(com.aiai.chat.R.styleable.RoundedCornerLayout_bottomLeftRadius, bottomLeftRadius)
                bottomRightRadius = getDimension(com.aiai.chat.R.styleable.RoundedCornerLayout_bottomRightRadius, bottomRightRadius)
            }
            clipContent = getBoolean(com.aiai.chat.R.styleable.RoundedCornerLayout_clipContent, clipContent)
            recycle()
        }
        setWillNotDraw(false)
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (clipContent) {
            canvas.save()
            updateOutlinePath()
            canvas.clipPath(outlinePath)
            super.dispatchDraw(canvas)
            canvas.restore()
        } else {
            super.dispatchDraw(canvas)
        }
    }

    private fun updateOutlinePath() {
        outlinePath.reset()
        val width = width.toFloat()
        val height = height.toFloat()

        val radii = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius
        )

        outlinePath.addRoundRect(RectF(0f, 0f, width, height), radii, Path.Direction.CW)
    }

    /**
     * 设置统一圆角
     */
    fun setCornerRadius(radius: Float) {
        topLeftRadius = radius
        topRightRadius = radius
        bottomLeftRadius = radius
        bottomRightRadius = radius
        invalidate()
    }

    /**
     * 设置四角独立圆角
     */
    fun setCornerRadii(
        topLeft: Float,
        topRight: Float,
        bottomLeft: Float,
        bottomRight: Float
    ) {
        topLeftRadius = topLeft
        topRightRadius = topRight
        bottomLeftRadius = bottomLeft
        bottomRightRadius = bottomRight
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}
