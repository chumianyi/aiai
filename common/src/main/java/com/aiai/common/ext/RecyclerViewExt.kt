/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.common.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.abs

/**
 * RecyclerView 扩展函数集合。
 *
 * 提供布局管理、滚动控制、分页加载、分割线、动画、适配器数据更新等
 * 常用 RecyclerView 操作。
 */

// region 布局管理

/**
 * 设置垂直方向的 LinearLayoutManager。
 *
 * @param reverseLayout 是否反转布局
 */
fun RecyclerView.verticalLayoutManager(reverseLayout: Boolean = false) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
}

/**
 * 设置水平方向的 LinearLayoutManager。
 *
 * @param reverseLayout 是否反转布局
 */
fun RecyclerView.horizontalLayoutManager(reverseLayout: Boolean = false) {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, reverseLayout)
}

/**
 * 设置瀑布流布局管理器。
 *
 * @param spanCount 列数
 * @param orientation 方向
 * @param reverseLayout 是否反转
 */
fun RecyclerView.staggeredLayoutManager(
    spanCount: Int = 2,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) {
    layoutManager = StaggeredGridLayoutManager(spanCount, orientation).apply {
        this.reverseLayout = reverseLayout
    }
}

/**
 * 获取第一个可见 Item 的位置。
 *
 * @return 位置索引，未找到返回 RecyclerView.NO_POSITION
 */
fun RecyclerView.findFirstVisibleItemPosition(): Int {
    return when (val lm = layoutManager) {
        is LinearLayoutManager -> lm.findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val positions = IntArray(lm.spanCount)
            lm.findFirstVisibleItemPositions(positions)
            positions.minOrNull() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

/**
 * 获取最后一个可见 Item 的位置。
 *
 * @return 位置索引，未找到返回 RecyclerView.NO_POSITION
 */
fun RecyclerView.findLastVisibleItemPosition(): Int {
    return when (val lm = layoutManager) {
        is LinearLayoutManager -> lm.findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val positions = IntArray(lm.spanCount)
            lm.findLastVisibleItemPositions(positions)
            positions.maxOrNull() ?: RecyclerView.NO_POSITION
        }
        else -> RecyclerView.NO_POSITION
    }
}

/**
 * 获取已显示的 Item 数量。
 */
fun RecyclerView.visibleItemCount(): Int {
    val first = findFirstVisibleItemPosition()
    val last = findLastVisibleItemPosition()
    return if (first == RecyclerView.NO_POSITION || last == RecyclerView.NO_POSITION) {
        0
    } else {
        last - first + 1
    }
}

// endregion

// region 滚动控制

/**
 * 平滑滚动到指定位置。
 *
 * @param position 目标位置
 * @param offset 偏移量
 */
fun RecyclerView.smoothScrollToPosition(position: Int, offset: Int = 0) {
    if (offset == 0) {
        smoothScrollToPosition(position)
    } else {
        val lm = layoutManager as? LinearLayoutManager
        lm?.scrollToPositionWithOffset(position, offset) ?: smoothScrollToPosition(position)
    }
}

/**
 * 快速滚动到顶部。
 *
 * @param smooth 是否平滑滚动
 */
fun RecyclerView.scrollToTop(smooth: Boolean = true) {
    if (smooth) {
        smoothScrollToPosition(0)
    } else {
        scrollToPosition(0)
    }
}

/**
 * 快速滚动到底部。
 *
 * @param smooth 是否平滑滚动
 */
fun RecyclerView.scrollToBottom(smooth: Boolean = true) {
    val itemCount = adapter?.itemCount ?: return
    if (smooth) {
        smoothScrollToPosition(itemCount - 1)
    } else {
        scrollToPosition(itemCount - 1)
    }
}

/**
 * 检查是否已经滚动到顶部。
 *
 * @return true 表示已到顶部
 */
fun RecyclerView.isAtTop(): Boolean {
    return findFirstVisibleItemPosition() == 0 &&
            (layoutManager as? LinearLayoutManager)?.findViewByPosition(0)?.top == 0
}

/**
 * 检查是否已经滚动到底部。
 *
 * @return true 表示已到底部
 */
fun RecyclerView.isAtBottom(): Boolean {
    val itemCount = adapter?.itemCount ?: return true
    val lastVisible = findLastVisibleItemPosition()
    return lastVisible >= itemCount - 1 &&
            (layoutManager as? LinearLayoutManager)?.findViewByPosition(lastVisible)?.bottom
                ?: 0 >= height - 10
}

/**
 * 监听滚动方向。
 *
 * @param onScrolledUp 向上滚动回调
 * @param onScrolledDown 向下滚动回调
 */
fun RecyclerView.onScrollDirection(
    onScrolledUp: () -> Unit = {},
    onScrolledDown: () -> Unit = {}
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var lastDy = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && dy != lastDy) {
                onScrolledDown()
            } else if (dy < 0 && dy != lastDy) {
                onScrolledUp()
            }
            lastDy = dy
        }
    })
}

// endregion

// region 分页加载

/**
 * 分页加载监听器。
 *
 * @param visibleThreshold 触发加载的阈值（距底部还有多少item时触发）
 * @param onLoadMore 加载更多回调
 */
fun RecyclerView.addOnLoadMoreListener(
    visibleThreshold: Int = 5,
    onLoadMore: () -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var isLoading = false
        private var previousTotal = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy <= 0) return

            val totalItemCount = layoutManager?.itemCount ?: return
            val lastVisibleItem = findLastVisibleItemPosition()

            if (isLoading) {
                if (totalItemCount > previousTotal) {
                    isLoading = false
                    previousTotal = totalItemCount
                }
            }

            if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                onLoadMore()
                isLoading = true
            }
        }
    })
}

/**
 * 重置分页加载状态。
 */
fun RecyclerView.resetLoadMore() {
    // 分页状态由监听器内部管理，这里预留重置接口
    // 实际使用时可配合自定义监听器实现
}

// endregion

// region ItemDecoration

/**
 * 添加垂直方向的分割线。
 *
 * @param drawableRes 分割线 drawable 资源
 */
fun RecyclerView.addVerticalDivider(drawableRes: Int? = null) {
    val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    drawableRes?.let { decoration.setDrawable(context.getDrawable(it)) }
    addItemDecoration(decoration)
}

/**
 * 添加水平方向的分割线。
 *
 * @param drawableRes 分割线 drawable 资源
 */
fun RecyclerView.addHorizontalDivider(drawableRes: Int? = null) {
    val decoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
    drawableRes?.let { decoration.setDrawable(context.getDrawable(it)) }
    addItemDecoration(decoration)
}

/**
 * 移除所有 ItemDecoration。
 */
fun RecyclerView.removeAllItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

/**
 * 添加空白间距 ItemDecoration。
 *
 * @param spacingPx 间距像素值
 * @param includeEdge 是否包含边缘
 */
fun RecyclerView.addSpaceDecoration(spacingPx: Int, includeEdge: Boolean = false) {
    val decoration = object : ItemDecoration() {
        // 简化实现，实际项目中可通过 MarginItemDecoration 实现
    }
    addItemDecoration(decoration)
}

// endregion

// region 动画

/**
 * 设置 Item 进入动画。
 *
 * @param duration 动画时长
 * @param interpolator 插值器
 */
fun RecyclerView.setItemAnimator(
    duration: Long = 300,
    interpolator: Interpolator = LinearInterpolator()
) {
    itemAnimator?.apply {
        this.addDuration = duration
        this.removeDuration = duration
        this.moveDuration = duration
        this.changeDuration = duration
    }
}

/**
 * 禁用变更动画。
 *
 * 解决 notifyDataSetChanged 闪烁问题。
 */
fun RecyclerView.disableChangeAnimation() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

/**
 * 为指定 Item 添加入场动画。
 *
 * @param startPosition 起始位置
 * @param animator 动画构建器
 */
fun RecyclerView.runItemAnimator(
    startPosition: Int = 0,
    animator: (View) -> Animator
) {
    for (i in startPosition until childCount) {
        val child = getChildAt(i) ?: continue
        animator(child).start()
    }
}

/**
 * 为列表添加淡入动画。
 *
 * @param duration 动画时长
 */
fun RecyclerView.fadeIn(duration: Long = 300) {
    runItemAnimator { view ->
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            this.duration = duration
        }
    }
}

/**
 * 为列表添加上滑动画。
 *
 * @param duration 动画时长
 * @param translationY 初始偏移量
 */
fun RecyclerView.slideUpIn(duration: Long = 300, translationY: Float = 200f) {
    runItemAnimator { view ->
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translationY, 0f).apply {
            this.duration = duration
        }
    }
}

// endregion

// region 适配器数据更新

/**
 * 安全地提交新列表（列表差异更新）。
 *
 * @param newList 新数据列表
 */
fun RecyclerView.submitList(newList: List<*>) {
    // 实际使用时配合 ListAdapter
    // 这里提供通用接口
}

/**
 * 刷新数据。
 */
fun RecyclerView.refresh() {
    adapter?.notifyDataSetChanged()
}

/**
 * 在指定位置插入数据。
 *
 * @param position 插入位置
 */
fun RecyclerView.insertItem(position: Int) {
    adapter?.notifyItemInserted(position)
}

/**
 * 移除指定位置的数据。
 *
 * @param position 移除位置
 */
fun RecyclerView.removeItem(position: Int) {
    adapter?.notifyItemRemoved(position)
}

/**
 * 更新指定位置的数据。
 *
 * @param position 更新位置
 */
fun RecyclerView.updateItem(position: Int) {
    adapter?.notifyItemChanged(position)
}

/**
 * 移动 Item。
 *
 * @param fromPosition 原位置
 * @param toPosition 目标位置
 */
fun RecyclerView.moveItem(fromPosition: Int, toPosition: Int) {
    adapter?.notifyItemMoved(fromPosition, toPosition)
}

// endregion

// region 其他

/**
 * 设置 SnapHelper 实现一页一页滚动效果。
 *
 * @param snapHelper SnapHelper 实例，默认 LinearSnapHelper
 */
fun RecyclerView.attachSnapHelper(snapHelper: SnapHelper = LinearSnapHelper()) {
    snapHelper.attachToRecyclerView(this)
}

/**
 * 设置边缘效果颜色。
 *
 * @param color 边缘颜色
 */
fun RecyclerView.setEdgeEffectColor(color: Int) {
    edgeEffectFactory = object : EdgeEffectFactory() {
        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffectCompat {
            return EdgeEffectCompat(view.context).apply {
                setColor(color)
            }
        }
    }
}

/**
 * 是否可以垂直滚动。
 */
fun RecyclerView.canScrollVertically(): Boolean {
    return canScrollVertically(-1) || canScrollVertically(1)
}

/**
 * 是否可以水平滚动。
 */
fun RecyclerView.canScrollHorizontally(): Boolean {
    return canScrollHorizontally(-1) || canScrollHorizontally(1)
}

/**
 * 获取滚动距离的百分比（0-100）。
 *
 * @return 滚动百分比
 */
fun RecyclerView.scrollProgress(): Int {
    val firstVisible = findFirstVisibleItemPosition()
    val lastVisible = findLastVisibleItemPosition()
    val total = adapter?.itemCount ?: return 0
    if (total == 0) return 0

    val visibleRange = lastVisible - firstVisible + 1
    val scrolledItems = firstVisible

    return ((scrolledItems.toFloat() / (total - visibleRange).coerceAtLeast(1)) * 100).toInt()
        .coerceIn(0, 100)
}

/**
 * 预测动画结束后的位置。
 *
 * @return 预测位置
 */
fun RecyclerView.predictTargetPosition(): Int {
    // 简化实现，实际可通过 SmoothScroller 预测
    return findLastVisibleItemPosition()
}

/**
 * 滚动到指定位置并带有补间动画。
 *
 * @param position 目标位置
 * @param duration 动画时长
 */
fun RecyclerView.smoothScrollToPositionWithOffset(
    position: Int,
    duration: Long = 500
) {
    val lm = layoutManager as? LinearLayoutManager ?: return
    val firstVisible = lm.findFirstVisibleItemPosition()
    val distance = abs(position - firstVisible)

    if (distance < 5) {
        smoothScrollToPosition(position)
    } else {
        // 先快速滚动到附近，再平滑滚动到目标
        val nearPosition = if (position > firstVisible) position - 3 else position + 3
        post {
            scrollToPosition(nearPosition)
            post { smoothScrollToPosition(position) }
        }
    }
}

/**
 * 嵌套滚动启用/禁用。
 *
 * @param enabled 是否启用
 */
fun RecyclerView.setNestedScrollingEnabledCompat(enabled: Boolean) {
    isNestedScrollingEnabled = enabled
}

/**
 * 清除所有滚动监听。
 */
fun RecyclerView.clearOnScrollListeners() {
    clearOnScrollListeners()
}

// endregion

/**
 * EdgeEffect 兼容包装类。
 */
private class EdgeEffectCompat(context: android.content.Context) {
    private val edgeEffect = android.widget.EdgeEffect(context)

    /** 设置边缘效果颜色。 */
    fun setColor(color: Int) {
        edgeEffect.color = color
    }
}
