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
 * View 样式工具。
 */
object ViewHelper {

    fun setRoundBg(v: View, color: Int, radius: Float) {
        v.background = GradientDrawable().apply { setColor(color); cornerRadius = radius }
    }

    fun setRoundStrokeBg(v: View, color: Int, radius: Float, sw: Int, sc: Int) {
        v.background = GradientDrawable().apply { setColor(color); cornerRadius = radius; setStroke(sw, sc) }
    }

    fun setCircleBg(v: View, color: Int) {
        v.background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(color) }
    }

    fun setTextColor(tv: TextView, hex: String) {
        tv.setTextColor(Color.parseColor(hex))
    }

    fun visible(v: View) { v.visibility = View.VISIBLE }
    fun gone(v: View) { v.visibility = View.GONE }
    fun invisible(v: View) { v.visibility = View.INVISIBLE }
}
