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

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView

/**
 * UI 工具类。
 */
object UIUtil {

    /** 设置圆角背景。 */
    fun setRoundBackground(view: View, color: Int, radius: Float) {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = radius
        view.background = drawable
    }

    /** 设置圆形背景。 */
    fun setCircleBackground(view: View, color: Int) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setColor(color)
        view.background = drawable
    }

    /** 设置带边框圆角背景。 */
    fun setRoundStrokeBackground(view: View, color: Int, radius: Float, strokeWidth: Int, strokeColor: Int) {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = radius
        drawable.setStroke(strokeWidth, strokeColor)
        view.background = drawable
    }

    /** 设置文字颜色。 */
    fun setTextColor(textView: TextView, colorHex: String) {
        textView.setTextColor(Color.parseColor(colorHex))
    }

    /** dp 转 px。 */
    fun dp2px(context: android.content.Context, dp: Float): Float {
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }
}
