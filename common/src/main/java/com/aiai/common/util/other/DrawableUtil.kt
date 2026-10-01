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
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView

/**
 * Drawable 工具类。
 */
object DrawableUtil {

    /** 创建圆角矩形 Drawable。 */
    fun createRoundRect(color: Int, radius: Float): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }
    }

    /** 创建带边框圆角矩形。 */
    fun createRoundRectWithStroke(color: Int, radius: Float, strokeWidth: Int, strokeColor: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
            setStroke(strokeWidth, strokeColor)
        }
    }

    /** 创建圆形 Drawable。 */
    fun createOval(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
    }

    /** 设置 View 背景为圆角矩形。 */
    fun setRoundBackground(view: View, colorHex: String, radiusDp: Float) {
        val density = view.resources.displayMetrics.density
        view.background = createRoundRect(Color.parseColor(colorHex), radiusDp * density)
    }

    /** 设置 TextView 文字颜色（十六进制）。 */
    fun setTextColor(textView: TextView, colorHex: String) {
        textView.setTextColor(Color.parseColor(colorHex))
    }
}
