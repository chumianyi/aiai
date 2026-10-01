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
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * 加载动画View（AI风格）
 *
 * 显示流动的渐变波浪加载动画，模拟AI思考中的效果。
 * 使用多层渐变波浪叠加，形成流动感。
 *
 * XML属性：
 * - animationColor: 动画主色
 * - waveCount: 波浪层数
 * - animationSpeed: 动画速度
 */
class LoadingAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animationColor: Int = Color.parseColor("#6C63FF")
    private var waveCount: Int = 3
    private var animationSpeed: Float = 1f

    private var animator: ValueAnimator? = null
    private var wavePhase = 0f
    private var waveAmplitude: Float = dp2px(6f)
    private var waveLength: Float = dp2px(80f)

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.LoadingAnimationView, defStyleAttr, 0).apply {
            animationColor = getColor(com.aiai.chat.R.styleable.LoadingAnimationView_animationColor, animationColor)
            waveCount = getInt(com.aiai.chat.R.styleable.LoadingAnimationView_waveCount, waveCount)
            animationSpeed = getFloat(com.aiai.chat.R.styleable.LoadingAnimationView_animationSpeed, animationSpeed)
            recycle()
        }
        startAnimation()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = dp2px(120f).toInt()
        val desiredHeight = dp2px(40f).toInt()
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

        for (wave in 0 until waveCount) {
            paint.color = animationColor
            paint.alpha = 80 + wave * 40
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = dp2px(2f)

            val phaseOffset = wave * 0.5f
            val amplitude = waveAmplitude * (1f - wave * 0.2f)

            drawWave(canvas, centerY, amplitude, phaseOffset)
        }

        // 中心点
        paint.alpha = 255
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width / 2f, centerY, dp2px(3f), paint)
    }

    private fun drawWave(canvas: Canvas, centerY: Float, amplitude: Float, phaseOffset: Float) {
        val path = android.graphics.Path()
        val step = 4f
        var firstPoint = true

        var x = 0f
        while (x <= width) {
            val y = centerY + kotlin.math.sin((x / waveLength * 2 * Math.PI) + wavePhase + phaseOffset) * amplitude
            if (firstPoint) {
                path.moveTo(x, y.toFloat())
                firstPoint = false
            } else {
                path.lineTo(x, y.toFloat())
            }
            x += step
        }
        canvas.drawPath(path, paint)
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, (2 * Math.PI).toFloat()).apply {
            duration = (2000 / animationSpeed).toLong()
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                wavePhase = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    /**
     * 设置动画颜色
     */
    fun setAnimationColor(color: Int) {
        animationColor = color
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
        }
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        )
    }
}
