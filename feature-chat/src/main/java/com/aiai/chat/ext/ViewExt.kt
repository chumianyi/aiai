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
package com.aiai.chat.ext

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.TextView

/**
 * View扩展函数集合
 *
 * 提供View的常用扩展方法，简化UI操作。
 */

/**
 * 显示View（VISIBLE）
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * 隐藏View（INVISIBLE，保留占位）
 */
fun View.hide() {
    visibility = View.INVISIBLE
}

/**
 * 消失View（GONE，不占位）
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 根据条件显示或隐藏
 */
fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

/**
 * 设置margin
 */
fun View.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}

/**
 * 设置padding
 */
fun View.setPaddingDp(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val density = resources.displayMetrics.density
    setPadding(
        (left * density).toInt(),
        (top * density).toInt(),
        (right * density).toInt(),
        (bottom * density).toInt()
    )
}

/**
 * 淡入动画
 */
fun View.fadeIn(duration: Long = 300) {
    startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/**
 * 淡出动画
 */
fun View.fadeOut(duration: Long = 300) {
    startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/**
 * 从底部滑入
 */
fun View.slideUp(duration: Long = 300) {
    startAnimation(TranslateAnimation(0f, 0f, height.toFloat(), 0f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}

/**
 * 从顶部滑入
 */
fun View.slideDown(duration: Long = 300) {
    startAnimation(TranslateAnimation(0f, 0f, -height.toFloat(), 0f).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
    })
}
