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

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.core.view.doOnLayout
import kotlin.math.abs

/**
 * View 相关扩展函数集合。
 *
 * 提供显隐切换、点击防抖、圆角背景、阴影、布局参数、动画、
 * 截图、焦点管理等常用 UI 操作。
 */

// region 显示隐藏

/** 将 View 设为 VISIBLE。 */
fun View.visible() { visibility = View.VISIBLE }

/** 将 View 设为 INVISIBLE（保留占位）。 */
fun View.invisible() { visibility = View.INVISIBLE }

/** 将 View 设为 GONE（不占位）。 */
fun View.gone() { visibility = View.GONE }

/** 按 [visible] 布尔值切换 VISIBLE / GONE。 */
fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/** 按 [visible] 布尔值切换 VISIBLE / INVISIBLE。 */
fun View.visibleOrInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/** 当前是否为 VISIBLE。 */
fun View.isVisible(): Boolean = visibility == View.VISIBLE

/** 当前是否为 GONE。 */
fun View.isGone(): Boolean = visibility == View.GONE

// endregion

// region 点击防抖

/** 最近一次点击时间记录，按 View 实例隔离。 */
private const val DEBOUNCE_INTERVAL = 500L

/**
 * 防重复点击：默认 500ms 内只响应一次。
 */
fun View.onSingleClick(interval: Long = DEBOUNCE_INTERVAL, action: (View) -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClick >= interval) {
            lastClick = now
            action(it)
        }
    }
}

/**
 * 带额外标签的防抖点击，可区分不同按钮。
 */
fun View.onSingleClickTag(tag: Any, interval: Long = DEBOUNCE_INTERVAL, action: (View) -> Unit) {
    setOnClickListener {
        val now = SystemClock.elapsedRealtime()
        val last = (it.getTag(tag.hashCode()) as? Long) ?: 0L
        if (now - last >= interval) {
            it.setTag(tag.hashCode(), now)
            action(it)
        }
    }
}

// endregion

// region 圆角 & 背景

/**
 * 给 View 设置圆角背景（纯色 + 圆角）。
 *
 * @param color 背景色
 * @param radius 圆角半径（像素）
 */
fun View.setRoundedBackground(@ColorInt color: Int, radius: Float) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
    }
}

/**
 * 设置带边框的圆角背景。
 */
fun View.setRoundedStrokeBackground(
    @ColorInt color: Int,
    radius: Float,
    strokeWidth: Float,
    @ColorInt strokeColor: Int
) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
        setStroke(strokeWidth.toInt(), strokeColor)
    }
}

/**
 * 设置左侧圆角、右侧直角的背景（常见于 Tab 选中）。
 */
fun View.setLeftRoundedBackground(@ColorInt color: Int, radius: Float) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
    }
}

/**
 * 设置右侧圆角、左侧直角的背景。
 */
fun View.setRightRoundedBackground(@ColorInt color: Int, radius: Float) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
    }
}

/**
 * 动态设置 Elevation 阴影。
 */
fun View.setElevationDp(elevation: Float) {
    ViewCompat.setElevation(this, elevation)
}

// endregion

// region 布局参数

/** 动态设置宽度（像素）。 */
fun View.widthPx(width: Int) {
    layoutParams = layoutParams.apply { this.width = width }
}

/** 动态设置高度（像素）。 */
fun View.heightPx(height: Int) {
    layoutParams = layoutParams.apply { this.height = height }
}

/** 动态设置宽高。 */
fun View.sizePx(width: Int, height: Int) {
    layoutParams = layoutParams.apply {
        this.width = width
        this.height = height
    }
}

/** 设置 margins（像素）。 */
fun View.marginsPx(left: Int, top: Int, right: Int, bottom: Int) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.setMargins(left, top, right, bottom)
    layoutParams = lp
}

/** 设置左右 margin。 */
fun View.horizontalMarginPx(margin: Int) = marginsPx(margin, 0, margin, 0)

/** 设置上下 margin。 */
fun View.verticalMarginPx(margin: Int) = marginsPx(0, margin, 0, margin)

/** 设置 padding（像素）。 */
fun View.paddingPx(left: Int, top: Int, right: Int, bottom: Int) {
    setPadding(left, top, right, bottom)
}

// endregion

// region 动画

/** 淡入动画。 */
fun View.fadeIn(duration: Long = 300) {
    startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/** 淡出动画。 */
fun View.fadeOut(duration: Long = 300) {
    startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/** 缩放动画（从 0.9 到 1.0）。 */
fun View.scaleIn(duration: Long = 200) {
    startAnimation(ScaleAnimation(0.9f, 1f, 0.9f, 1f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/** 从资源加载动画。 */
fun View.startAnim(@DimenRes animRes: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, animRes))
}

/** 横向位移动画。 */
fun View.translationX(dp: Float) {
    animate().translationX(dp).setDuration(200).start()
}

/** 纵向位移动画。 */
fun View.translationY(dp: Float) {
    animate().translationY(dp).setDuration(200).start()
}

/** 旋转动画。 */
fun View.rotate(degrees: Float, duration: Long = 300) {
    animate().rotation(degrees).setDuration(duration).start()
}

// endregion

// region 测量 & 截图

/**
 * 在布局完成后执行 [block]，可获取测量后的宽高。
 */
fun View.afterLayout(block: View.() -> Unit) {
    doOnLayout { it.block() }
}

/**
 * 获取 View 的截图 Bitmap。
 */
fun View.screenshot(): Bitmap? {
    return try {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        bitmap
    } catch (e: Exception) {
        null
    }
}

/**
 * 测量并获取 View 的实际宽高（在尚未 layout 时使用）。
 */
fun View.measureSize(): IntArray {
    measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    return intArrayOf(measuredWidth, measuredHeight)
}

// endregion

// region 焦点 & 触摸

/** 请求焦点并弹出键盘。 */
fun View.focusAndShowKeyboard() {
    requestFocus()
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
            as? android.view.inputmethod.InputMethodManager
    imm?.showSoftInput(this, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
}

/** 清除焦点。 */
fun View.clearFocusSafe() {
    clearFocus()
}

/**
 * 设置触摸反馈背景波纹（RippleEffect）。
 */
fun View.setRippleBackground() {
    val attrs = intArrayOf(android.R.attr.selectableItemBackground)
    val typedArray = context.obtainStyledAttributes(attrs)
    val resId = typedArray.getResourceId(0, 0)
    typedArray.recycle()
    if (resId != 0) setBackgroundResource(resId)
    isClickable = true
}

/**
 * 判断 View 是否在屏幕中完整可见。
 */
fun View.isVisiblyShownOnScreen(): Boolean {
    val rect = android.graphics.Rect()
    val isVisible = getGlobalVisibleRect(rect)
    return isVisible && rect.height() > 0 && rect.width() > 0
}

/**
 * 为 View 设置圆形水波纹裁剪背景。
 */
fun View.setCircleRippleBackground() {
    val drawable = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(Color.parseColor("#22000000"))
    }
    background = drawable
}

/**
 * 获取 View 在屏幕上的位置坐标 [x, y]。
 */
fun View.locationOnScreen(): IntArray {
    val loc = IntArray(2)
    getLocationOnScreen(loc)
    return loc
}

/**
 * 为 View 设置部分圆角（左上、右上、左下、右下分别控制）。
 */
fun View.setCornerRadiiBackground(
    @ColorInt color: Int,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float
) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadii = floatArrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft
        )
    }
}

/**
 * 绘制一个圆形 Bitmap 作为背景。
 */
fun View.setCircleBackground(@ColorInt color: Int, radius: Float) {
    val bitmap = Bitmap.createBitmap(radius.toInt() * 2, radius.toInt() * 2, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
    canvas.drawRoundRect(RectF(0f, 0f, radius * 2, radius * 2), radius, radius, paint)
    setImageBitmapCompat(bitmap)
}

private fun View.setImageBitmapCompat(bitmap: Bitmap) {
    if (this is android.widget.ImageView) setImageBitmap(bitmap)
}

/**
 * 遍历所有子 View 执行 [block]。
 */
fun ViewGroup.forEachChild(action: (View) -> Unit) {
    for (i in 0 until childCount) action(getChildAt(i))
}

/**
 * 递归遍历所有后代 View 执行 [block]。
 */
fun ViewGroup.forEachDescendant(action: (View) -> Unit) {
    forEachChild { child ->
        action(child)
        if (child is ViewGroup) child.forEachDescendant(action)
    }
}

/**
 * 判断点击位置是否落在 View 范围内。
 */
fun View.isTouchWithin(ev: MotionEvent): Boolean {
    val loc = locationOnScreen()
    val x = ev.rawX - loc[0]
    val y = ev.rawY - loc[1]
    return x >= 0 && x <= width && y >= 0 && y <= height
}

/**
 * 计算与 [other] View 的距离（dp 近似）。
 */
fun View.distanceTo(other: View): Float {
    val a = locationOnScreen()
    val b = other.locationOnScreen()
    val dx = (a[0] - b[0]).toFloat()
    val dy = (a[1] - b[1]).toFloat()
    return kotlin.math.hypot(dx, dy)
}

/**
 * 隐藏软键盘（通过当前 View 的 windowToken）。
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
            as? android.view.inputmethod.InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

// endregion
