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
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs

/**
 * 侧滑显示操作按钮布局
 *
 * 支持左滑显示右侧操作按钮（删除、置顶等）。
 * 参考RecyclerView侧滑删除实现，使用ViewDragHelper。
 *
 * 子View结构：
 * - 第一个子View: 内容视图
 * - 后续子View: 操作按钮视图（从右侧排列）
 */
class SwipeRevealLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private lateinit var contentView: View
    private var actionViews: MutableList<View> = mutableListOf()

    private val dragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === contentView
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            val maxLeft = -totalActionWidth()
            return left.coerceIn(maxLeft, 0)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val halfWidth = -totalActionWidth() / 2
            if (contentView.left < halfWidth || xvel < -500) {
                dragHelper.smoothSlideViewTo(contentView, -totalActionWidth(), 0)
            } else {
                dragHelper.smoothSlideViewTo(contentView, 0, 0)
            }
            invalidate()
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return totalActionWidth()
        }
    })

    private var isOpen: Boolean = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount < 1) return
        contentView = getChildAt(0)
        actionViews.clear()
        for (i in 1 until childCount) {
            actionViews.add(getChildAt(i))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        contentView.measure(
            MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
        )
        heightSize = contentView.measuredHeight

        for (actionView in actionViews) {
            actionView.measure(
                MeasureSpec.makeMeasureSpec(DEFAULT_ACTION_WIDTH, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        contentView.layout(
            contentView.left, 0,
            contentView.left + measuredWidth, measuredHeight
        )

        var right = measuredWidth
        for (actionView in actionViews) {
            val left = right - actionView.measuredWidth
            actionView.layout(left, 0, right, measuredHeight)
            right = left
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    private fun totalActionWidth(): Int {
        return actionViews.sumOf { it.measuredWidth }
    }

    /**
     * 打开侧滑
     */
    fun open() {
        dragHelper.smoothSlideViewTo(contentView, -totalActionWidth(), 0)
        invalidate()
        isOpen = true
    }

    /**
     * 关闭侧滑
     */
    fun close() {
        dragHelper.smoothSlideViewTo(contentView, 0, 0)
        invalidate()
        isOpen = false
    }

    /**
     * 切换侧滑状态
     */
    fun toggle() {
        if (isOpen) close() else open()
    }

    /**
     * 是否处于打开状态
     */
    fun isOpened(): Boolean = isOpen

    companion object {
        private const val DEFAULT_ACTION_WIDTH = 160
    }
}
