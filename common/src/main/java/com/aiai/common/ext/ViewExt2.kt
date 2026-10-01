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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.ext

import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * View 扩展函数集（扩展版）。
 */

/** View 显示。 */
fun View.visible() {
    visibility = View.VISIBLE
}

/** View 隐藏（占位置）。 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/** View 隐藏（不占位置）。 */
fun View.gone() {
    visibility = View.GONE
}

/** 按条件显示/隐藏。 */
fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/** 防抖动点击。 */
fun View.onClick(interval: Long = 500, action: (View) -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val now = System.currentTimeMillis()
        if (now - lastClick > interval) {
            lastClick = now
            action(it)
        }
    }
}

/** 设置 padding（dp）。 */
fun View.setPaddingDp(left: Int, top: Int, right: Int, bottom: Int) {
    val density = resources.displayMetrics.density
    setPadding(
        (left * density).toInt(),
        (top * density).toInt(),
        (right * density).toInt(),
        (bottom * density).toInt()
    )
}

/** 设置 margin（dp）。 */
fun View.setMarginDp(left: Int, top: Int, right: Int, bottom: Int) {
    val density = resources.displayMetrics.density
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.setMargins(
        (left * density).toInt(),
        (top * density).toInt(),
        (right * density).toInt(),
        (bottom * density).toInt()
    )
    layoutParams = lp
}

/** 设置宽度（dp）。 */
fun View.setWidthDp(width: Int) {
    val density = resources.displayMetrics.density
    layoutParams = layoutParams.apply { this.width = (width * density).toInt() }
}

/** 设置高度（dp）。 */
fun View.setHeightDp(height: Int) {
    val density = resources.displayMetrics.density
    layoutParams = layoutParams.apply { this.height = (height * density).toInt() }
}

/** TextView 设置文字颜色。 */
fun TextView.textColor(color: Int) {
    setTextColor(color)
}

/** TextView 设置文字大小（sp）。 */
fun TextView.textSizeSp(size: Float) {
    textSize = size
}
