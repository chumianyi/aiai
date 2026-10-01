/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.ext

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.IdRes

/**
 * ViewGroup 扩展函数集合。
 *
 * 提供子View遍历、批量设置、动画、测量等常用 ViewGroup 操作。
 */

// region 子View遍历

/**
 * 遍历所有子 View。
 *
 * @param action 对每个子 View 执行的操作
 */
inline fun ViewGroup.forEachChild(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        getChildAt(i)?.let(action)
    }
}

/**
 * 递归遍历所有子 View（包括嵌套 ViewGroup 中的 View）。
 *
 * @param action 对每个子 View 执行的操作
 */
inline fun ViewGroup.forEachChildRecursive(action: (View) -> Unit) {
    forEachChild { child ->
        action(child)
        if (child is ViewGroup) {
            child.forEachChildRecursive(action)
        }
    }
}

/**
 * 查找所有符合条件的子 View。
 *
 * @param predicate 筛选条件
 * @return 符合条件的 View 列表
 */
inline fun ViewGroup.findChildren(crossinline predicate: (View) -> Boolean): List<View> {
    val result = mutableListOf<View>()
    forEachChild { child ->
        if (predicate(child)) {
            result.add(child)
        }
        if (child is ViewGroup) {
            result.addAll(child.findChildren(predicate))
        }
    }
    return result
}

/**
 * 查找第一个符合条件的子 View。
 *
 * @param predicate 筛选条件
 * @return 符合条件的 View，未找到返回 null
 */
inline fun ViewGroup.findChild(crossinline predicate: (View) -> Boolean): View? {
    forEachChild { child ->
        if (predicate(child)) {
            return child
        }
        if (child is ViewGroup) {
            child.findChild(predicate)?.let { return it }
        }
    }
    return null
}

/**
 * 根据 ID 查找子 View（递归）。
 *
 * @param idRes 资源ID
 * @return 找到的 View，未找到返回 null
 */
fun ViewGroup.findViewRecursive(@IdRes idRes: Int): View? {
    return findChild { it.id == idRes }
}

// endregion

// region 批量设置

/**
 * 批量设置所有子 View 的点击监听。
 *
 * @param listener 点击监听
 */
fun ViewGroup.setChildrenClickable(listener: ((View) -> Unit)? = null) {
    forEachChild { child ->
        child.isClickable = listener != null
        listener?.let { clickListener ->
            child.setOnClickListener(clickListener)
        }
    }
}

/**
 * 批量设置所有子 View 的可见性。
 *
 * @param visibility 可见性状态
 */
fun ViewGroup.setChildrenVisibility(visibility: Int) {
    forEachChild { child ->
        child.visibility = visibility
    }
}

/**
 * 批量设置所有子 View 的透明度。
 *
 * @param alpha 透明度
 */
fun ViewGroup.setChildrenAlpha(alpha: Float) {
    forEachChild { child ->
        child.alpha = alpha
    }
}

/**
 * 批量启用/禁用所有子 View。
 *
 * @param enabled 是否启用
 */
fun ViewGroup.setChildrenEnabled(enabled: Boolean) {
    forEachChild { child ->
        child.isEnabled = enabled
    }
}

/**
 * 批量设置背景色。
 *
 * @param color 背景颜色
 */
fun ViewGroup.setChildrenBackgroundColor(color: Int) {
    forEachChild { child ->
        child.setBackgroundColor(color)
    }
}

/**
 * 批量设置padding。
 *
 * @param padding 内边距像素值
 */
fun ViewGroup.setChildrenPadding(padding: Int) {
    forEachChild { child ->
        child.setPadding(padding, padding, padding, padding)
    }
}

// endregion

// region 动画

/**
 * 为所有子 View 添加入场动画。
 *
 * @param animatorBuilder 动画构建器
 * @param staggerMillis 每个子 View 之间的延迟
 */
fun ViewGroup.childrenEnterAnimation(
    staggerMillis: Long = 50L,
    animatorBuilder: (View) -> Animator
) {
    forEachChildIndexed { index, child ->
        val animator = animatorBuilder(child)
        animator.startDelay = index * staggerMillis
        animator.start()
    }
}

/**
 * 子 View 淡入动画。
 *
 * @param duration 动画时长
 * @param staggerMillis 间隔
 */
fun ViewGroup.fadeInChildren(
    duration: Long = 300,
    staggerMillis: Long = 50L
) {
    childrenEnterAnimation(staggerMillis) { view ->
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            this.duration = duration
        }
    }
}

/**
 * 子 View 从下方滑入动画。
 *
 * @param duration 动画时长
 * @param translationY 偏移量
 * @param staggerMillis 间隔
 */
fun ViewGroup.slideInChildren(
    duration: Long = 300,
    translationY: Float = 200f,
    staggerMillis: Long = 50L
) {
    childrenEnterAnimation(staggerMillis) { view ->
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translationY, 0f)
                    .apply { this.duration = duration },
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
                    .apply { this.duration = duration }
            )
        }
    }
}

/**
 * 子 View 缩放动画。
 *
 * @param duration 动画时长
 * @param fromScale 起始缩放比例
 * @param staggerMillis 间隔
 */
fun ViewGroup.scaleInChildren(
    duration: Long = 300,
    fromScale: Float = 0.8f,
    staggerMillis: Long = 50L
) {
    childrenEnterAnimation(staggerMillis) { view ->
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, fromScale, 1f)
                    .apply { this.duration = duration },
                ObjectAnimator.ofFloat(view, View.SCALE_Y, fromScale, 1f)
                    .apply { this.duration = duration },
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
                    .apply { this.duration = duration }
            )
        }
    }
}

/**
 * 所有子 View 动画同时播放。
 *
 * @param animatorBuilder 动画构建器
 */
fun ViewGroup.animateChildrenTogether(
    animatorBuilder: (View) -> Animator
) {
    val set = AnimatorSet()
    val animators = mutableListOf<Animator>()
    forEachChild { child ->
        animators.add(animatorBuilder(child))
    }
    set.playTogether(animators)
    set.start()
}

// endregion

// region 测量与布局

/**
 * 获取所有子 View 的总宽度。
 *
 * @return 总宽度像素值
 */
fun ViewGroup.childrenTotalWidth(): Int {
    var total = 0
    forEachChild { child ->
        total += child.measuredWidth
    }
    return total
}

/**
 * 获取所有子 View 的总高度。
 *
 * @return 总高度像素值
 */
fun ViewGroup.childrenTotalHeight(): Int {
    var total = 0
    forEachChild { child ->
        total += child.measuredHeight
    }
    return total
}

/**
 * 获取可见子 View 的数量。
 *
 * @return 可见子 View 数量
 */
fun ViewGroup.visibleChildCount(): Int {
    var count = 0
    forEachChild { child ->
        if (child.visibility == View.VISIBLE) count++
    }
    return count
}

/**
 * 获取所有子 View 中最宽的宽度。
 *
 * @return 最宽子 View 的宽度
 */
fun ViewGroup.maxChildWidth(): Int {
    var max = 0
    forEachChild { child ->
        if (child.measuredWidth > max) max = child.measuredWidth
    }
    return max
}

/**
 * 获取所有子 View 中最高的高度。
 *
 * @return 最高子 View 的高度
 */
fun ViewGroup.maxChildHeight(): Int {
    var max = 0
    forEachChild { child ->
        if (child.measuredHeight > max) max = child.measuredHeight
    }
    return max
}

// endregion

// region 其他

/**
 * 带索引遍历子 View。
 *
 * @param action 带索引的遍历操作
 */
inline fun ViewGroup.forEachChildIndexed(action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        getChildAt(i)?.let { action(i, it) }
    }
}

/**
 * 移除所有子 View。
 */
fun ViewGroup.removeAllChildren() {
    removeAllViews()
}

/**
 * 添加多个子 View。
 *
 * @param views 要添加的 View 列表
 */
fun ViewGroup.addChildren(vararg views: View) {
    views.forEach { addView(it) }
}

/**
 * 判断是否包含指定 View。
 *
 * @param view 要检查的 View
 * @return true 表示包含
 */
fun ViewGroup.containsView(view: View): Boolean {
    forEachChild { child ->
        if (child == view) return true
        if (child is ViewGroup && child.containsView(view)) return true
    }
    return false
}

/**
 * 获取子 View 在父容器中的索引。
 *
 * @param view 要查找的 View
 * @return 索引位置，未找到返回 -1
 */
fun ViewGroup.indexOfChildSafe(view: View): Int {
    return indexOfChild(view)
}

/**
 * 替换指定位置的子 View。
 *
 * @param index 位置索引
 * @param newView 新的 View
 */
fun ViewGroup.replaceChild(index: Int, newView: View) {
    if (index in 0 until childCount) {
        removeViewAt(index)
        addView(newView, index)
    }
}

/**
 * 在指定 View 前插入新 View。
 *
 * @param targetView 目标 View
 * @param newView 要插入的新 View
 */
fun ViewGroup.addViewBefore(targetView: View, newView: View) {
    val index = indexOfChild(targetView)
    if (index >= 0) {
        addView(newView, index)
    } else {
        addView(newView)
    }
}

/**
 * 在指定 View 后插入新 View。
 *
 * @param targetView 目标 View
 * @param newView 要插入的新 View
 */
fun ViewGroup.addViewAfter(targetView: View, newView: View) {
    val index = indexOfChild(targetView)
    if (index >= 0) {
        addView(newView, index + 1)
    } else {
        addView(newView)
    }
}

/**
 * 抖动动画（用于错误提示）。
 *
 * @param duration 动画时长
 */
fun ViewGroup.shake(duration: Long = 500) {
    val animator = ObjectAnimator.ofFloat(this, "translationX", 0f, 10f, -10f, 10f, -10f, 0f)
    animator.duration = duration
    animator.interpolator = LinearInterpolator()
    animator.start()
}

/**
 * 清空焦点。
 */
fun ViewGroup.clearFocusRecursive() {
    forEachChildRecursive { child ->
        child.clearFocus()
    }
}

/**
 * 设置所有子 View 为可点击并添加波纹效果。
 */
fun ViewGroup.setChildrenClickableWithRipple() {
    forEachChild { child ->
        child.isClickable = true
    }
}

// endregion
