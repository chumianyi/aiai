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
package com.aiai.common.util.other

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.FrameLayout

/**
 * 布局参数工具类。
 *
 * 动态设置 View 的布局参数。
 */
object LayoutUtil {

    /** 设置 LinearLayout 权重。 */
    fun setLinearLayoutWeight(view: View, weight: Float) {
        val lp = view.layoutParams as? LinearLayout.LayoutParams ?: return
        lp.weight = weight
        view.layoutParams = lp
    }

    /** 设置 RelativeLayout 规则。 */
    fun setRelativeLayoutRule(view: View, verb: Int, subject: Int = RelativeLayout.TRUE) {
        val lp = view.layoutParams as? RelativeLayout.LayoutParams ?: return
        lp.addRule(verb, subject)
        view.layoutParams = lp
    }

    /** 设置 FrameLayout 位置。 */
    fun setFrameLayoutGravity(view: View, gravity: Int) {
        val lp = view.layoutParams as? FrameLayout.LayoutParams ?: return
        lp.gravity = gravity
        view.layoutParams = lp
    }

    /** 设置 margin（dp）。 */
    fun setMarginDp(view: View, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        val density = view.resources.displayMetrics.density
        val lp = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        lp.setMargins(
            (left * density).toInt(),
            (top * density).toInt(),
            (right * density).toInt(),
            (bottom * density).toInt()
        )
        view.layoutParams = lp
    }

    /** 设置 padding（dp）。 */
    fun setPaddingDp(view: View, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        val density = view.resources.displayMetrics.density
        view.setPadding(
            (left * density).toInt(),
            (top * density).toInt(),
            (right * density).toInt(),
            (bottom * density).toInt()
        )
    }

    /** 设置宽高（dp）。 */
    fun setSizeDp(view: View, width: Int, height: Int) {
        val density = view.resources.displayMetrics.density
        view.layoutParams = view.layoutParams.apply {
            this.width = (width * density).toInt()
            this.height = (height * density).toInt()
        }
    }
}
