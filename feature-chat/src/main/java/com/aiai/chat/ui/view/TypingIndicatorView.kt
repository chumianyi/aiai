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

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.aiai.chat.R

/**
 * 打字指示器View
 *
 * 显示三个跳动的圆点动画，用于AI正在输入的状态提示。
 * 圆点依次上下跳动，形成流畅的波浪效果。
 *
 * XML属性：
 * - dotColor: 圆点颜色
 * - dotRadius: 圆点半径
 * - dotSpacing: 圆点间距
 * - animationDuration: 动画周期（毫秒）
 */
class TypingIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var dotColor: Int = 0xFF999999.toInt()
    private var dotRadius: Float = dp2px(4f)
    private var dotSpacing: Float = dp2px(8f)
    private var animationDuration: Long = 600L

    private var dotCount = 3
    private var animator: ValueAnimator? = null
    private var animationProgress = 0f

    private val dotOffsets = FloatArray(dotCount)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TypingIndicatorView, defStyleAttr, 0).apply {
            dotColor = getColor(R.styleable.TypingIndicatorView_dotColor, dotColor)
            dotRadius = getDimension(R.styleable.TypingIndicatorView_dotRadius, dotRadius)
            dotSpacing = getDimension(R.styleable.TypingIndicatorView_dotSpacing, dotSpacing)
            animationDuration = getInteger(R.styleable.TypingIndicatorView_animationDuration, 600).toLong()
            recycle()
        }
        paint.color = dotColor
        startAnimation()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = ((dotCount - 1) * dotSpacing + dotCount * dotRadius * 2).toInt()
        val desiredHeight = (dotRadius * 4).toInt()

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
        val centerY = height / 2f
        val totalWidth = dotCount * dotRadius * 2 + (dotCount - 1) * dotSpacing
        val startX = (width - totalWidth) / 2f + dotRadius

        for (i in 0 until dotCount) {
            val x = startX + i * (dotRadius * 2 + dotSpacing)
            val offset = dotOffsets[i]
            val alpha = (0.4f + 0.6f * (1f - kotlin.math.abs(offset))).coerceIn(0.2f, 1f)
            paint.alpha = (alpha * 255).toInt()
            canvas.drawCircle(x, centerY + offset * dotRadius, dotRadius, paint)
        }
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationDuration * 2
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                animationProgress = animation.animatedValue as Float
                updateDotOffsets()
                invalidate()
            }
            start()
        }
    }

    private fun updateDotOffsets() {
        for (i in 0 until dotCount) {
            val phase = (animationProgress * 2f + i * 0.33f) % 2f
            dotOffsets[i] = -kotlin.math.sin(phase * Math.PI).toFloat()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    /**
     * 设置圆点颜色
     */
    fun setDotColor(color: Int) {
        dotColor = color
        paint.color = color
        invalidate()
    }

    /**
     * 开始/停止动画
     */
    fun setAnimating(animating: Boolean) {
        if (animating) {
            startAnimation()
        } else {
            animator?.cancel()
            dotOffsets.fill(0f)
            invalidate()
        }
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        )
    }
}
