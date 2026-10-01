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
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * 渐变背景View
 *
 * 支持多色渐变背景，并可添加流动动画效果。
 * 用于App主背景、卡片背景等场景。
 *
 * XML属性：
 * - startColor: 渐变起始色
 * - centerColor: 渐变中间色
 * - endColor: 渐变结束色
 * - orientation: 渐变方向（水平/垂直）
 * - animate: 是否开启动画
 */
class GradientBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startColor: Int = 0xFF667EEA.toInt()
    private var centerColor: Int = 0xFF764BA2.toInt()
    private var endColor: Int = 0xFF6C63FF.toInt()
    private var isHorizontal: Boolean = true
    private var animate: Boolean = false

    private var animator: ValueAnimator? = null
    private var animationOffset = 0f

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.GradientBackgroundView, defStyleAttr, 0).apply {
            startColor = getColor(com.aiai.chat.R.styleable.GradientBackgroundView_startColor, startColor)
            centerColor = getColor(com.aiai.chat.R.styleable.GradientBackgroundView_centerColor, centerColor)
            endColor = getColor(com.aiai.chat.R.styleable.GradientBackgroundView_endColor, endColor)
            isHorizontal = getInt(com.aiai.chat.R.styleable.GradientBackgroundView_orientation, 0) == 0
            animate = getBoolean(com.aiai.chat.R.styleable.GradientBackgroundView_animate, animate)
            recycle()
        }
        if (animate) startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val shader = if (isHorizontal) {
            LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                intArrayOf(startColor, centerColor, endColor),
                floatArrayOf(0f + animationOffset, 0.5f, 1f - animationOffset),
                Shader.TileMode.CLAMP
            )
        } else {
            LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(startColor, centerColor, endColor),
                floatArrayOf(0f + animationOffset, 0.5f, 1f - animationOffset),
                Shader.TileMode.CLAMP
            )
        }
        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 0.2f, 0f).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                animationOffset = animation.animatedValue as Float
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
     * 设置渐变色
     */
    fun setGradientColors(start: Int, center: Int, end: Int) {
        startColor = start
        centerColor = center
        endColor = end
        invalidate()
    }

    /**
     * 设置动画开关
     */
    fun setAnimate(animate: Boolean) {
        this.animate = animate
        if (animate) startAnimation() else animator?.cancel()
    }
}
