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
 * View 样式工具类。
 */
object ViewStyleUtil {

    /** 设置圆角背景。 */
    fun setRoundedBackground(view: View, color: Int, radiusDp: Float) {
        val density = view.resources.displayMetrics.density
        val drawable = GradientDrawable().apply {
            setColor(color)
            cornerRadius = radiusDp * density
        }
        view.background = drawable
    }

    /** 设置带边框的圆角背景。 */
    fun setRoundedStrokeBackground(view: View, color: Int, radiusDp: Float, strokeDp: Float, strokeColor: Int) {
        val density = view.resources.displayMetrics.density
        val drawable = GradientDrawable().apply {
            setColor(color)
            cornerRadius = radiusDp * density
            setStroke((strokeDp * density).toInt(), strokeColor)
        }
        view.background = drawable
    }

    /** 设置圆形背景。 */
    fun setCircleBackground(view: View, color: Int) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
        view.background = drawable
    }

    /** 设置文字颜色。 */
    fun setTextColor(textView: TextView, colorHex: String) {
        textView.setTextColor(Color.parseColor(colorHex))
    }
}
