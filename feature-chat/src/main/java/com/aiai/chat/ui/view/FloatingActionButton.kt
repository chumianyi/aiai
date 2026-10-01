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

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator

/**
 * 悬浮操作按钮View
 *
 * 支持展开/收起子菜单按钮，带旋转动画。
 * 点击主按钮时展开子按钮，再次点击收起。
 *
 * XML属性：
 * - fabColor: 主按钮背景色
 * - fabIconColor: 图标颜色
 * - fabSize: 按钮尺寸（dp）
 * - expanded: 是否展开状态
 */
class FloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(2.5f)
        color = Color.WHITE
    }

    private var fabColor: Int = Color.parseColor("#6C63FF")
    private var iconColor: Int = Color.WHITE
    private var fabSize: Float = dp2px(56f)
    private var isExpanded: Boolean = false

    private var rotationAngle: Float = 0f
    private val centerRect = RectF()
    private var onFabClickListener: (() -> Unit)? = null
    private var onExpandListener: ((Boolean) -> Unit)? = null

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.FloatingActionButton, defStyleAttr, 0).apply {
            fabColor = getColor(com.aiai.chat.R.styleable.FloatingActionButton_fabColor, fabColor)
            iconColor = getColor(com.aiai.chat.R.styleable.FloatingActionButton_fabIconColor, iconColor)
            fabSize = getDimension(com.aiai.chat.R.styleable.FloatingActionButton_fabSize, fabSize)
            recycle()
        }
        bgPaint.color = fabColor
        iconPaint.color = iconColor
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = fabSize.toInt()
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = fabSize / 2f

        centerRect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawCircle(centerX, centerY, radius, bgPaint)

        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)
        drawPlusIcon(canvas, centerX, centerY)
        canvas.restore()
    }

    private fun drawPlusIcon(canvas: Canvas, cx: Float, cy: Float) {
        val lineLength = fabSize * 0.25f
        // 横线
        canvas.drawLine(cx - lineLength, cy, cx + lineLength, cy, iconPaint)
        // 竖线
        canvas.drawLine(cx, cy - lineLength, cx, cy + lineLength, iconPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animate().scaleX(0.9f).scaleY(0.9f).duration = 100
                animate().scaleX(0.9f).scaleY(0.9f).duration = 100
                return true
            }
            MotionEvent.ACTION_UP -> {
                animate().scaleX(1f).scaleY(1f).duration = 150
                performClick()
                toggleExpand()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                animate().scaleX(1f).scaleY(1f).duration = 150
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        onFabClickListener?.invoke()
        return true
    }

    /**
     * 切换展开/收起状态
     */
    fun toggleExpand() {
        setExpanded(!isExpanded)
    }

    /**
     * 设置展开状态
     */
    fun setExpanded(expanded: Boolean, animate: Boolean = true) {
        if (isExpanded == expanded) return
        isExpanded = expanded

        val targetRotation = if (expanded) 45f else 0f
        if (animate) {
            val rotationAnim = ObjectAnimator.ofFloat(this, "rotationAngle", rotationAngle, targetRotation)
            rotationAnim.duration = 300
            rotationAnim.interpolator = OvershootInterpolator()
            rotationAnim.start()
        } else {
            rotationAngle = targetRotation
            invalidate()
        }
        onExpandListener?.invoke(expanded)
    }

    /**
     * 设置主按钮点击监听
     */
    fun setOnFabClickListener(listener: () -> Unit) {
        onFabClickListener = listener
    }

    /**
     * 设置展开状态变化监听
     */
    fun setOnExpandListener(listener: (Boolean) -> Unit) {
        onExpandListener = listener
    }

    /**
     * 获取是否展开
     */
    fun isExpanded(): Boolean = isExpanded

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        )
    }
}
