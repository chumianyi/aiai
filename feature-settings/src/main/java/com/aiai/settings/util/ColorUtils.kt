/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * 颜色工具：深浅色判断、颜色混合、透明度调整、HSL 转换。
 */
object ColorUtils {

    /** 判断颜色是否为深色（用于决定文字颜色）。 */
    fun isDark(@ColorInt color: Int): Boolean {
        val darkness = 1.0 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255.0
        return darkness >= 0.5
    }

    /** 调整颜色亮度（factor < 1 变暗，> 1 变亮）。 */
    @ColorInt
    fun adjustBrightness(@ColorInt color: Int, @FloatRange(from = 0.0, to = 3.0) factor: Float): Int {
        val r = (Color.red(color) * factor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * factor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * factor).toInt().coerceIn(0, 255)
        return Color.rgb(r, g, b)
    }

    /** 混合两个颜色，[ratio] 为 second 占比 0~1。 */
    @ColorInt
    fun mix(@ColorInt first: Int, @ColorInt second: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
        val inverse = 1 - ratio
        val a = (Color.alpha(first) * inverse + Color.alpha(second) * ratio).toInt()
        val r = (Color.red(first) * inverse + Color.red(second) * ratio).toInt()
        val g = (Color.green(first) * inverse + Color.green(second) * ratio).toInt()
        val b = (Color.blue(first) * inverse + Color.blue(second) * ratio).toInt()
        return Color.argb(a, r, g, b)
    }

    /** 设置颜色透明度。 */
    @ColorInt
    fun withAlpha(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return Color.argb((alpha * 255).toInt(), Color.red(color), Color.green(color), Color.blue(color))
    }

    /** 将 #RRGGBB 解析为颜色，失败返回默认色。 */
    @ColorInt
    fun parse(colorStr: String, @ColorInt def: Int = Color.TRANSPARENT): Int {
        return try {
            Color.parseColor(colorStr)
        } catch (e: Exception) {
            def
        }
    }

    /** 颜色转 HEX 字符串 #AARRGGBB。 */
    fun toHex(@ColorInt color: Int): String {
        return String.format("#%08X", color)
    }

    /** 生成同色系色板（基于 base 色明暗变化）。 */
    fun generatePalette(@ColorInt base: Int, count: Int = 5): List<Int> {
        return (0 until count).map { i ->
            val factor = 0.6f + i * (0.8f / count)
            adjustBrightness(base, factor)
        }
    }

    /** 从颜色中提取强调色（取最饱和的色通道）。 */
    @ColorInt
    fun accentOf(@ColorInt color: Int): Int {
        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(color, hsv)
        hsv[1] = 1f
        hsv[2] = 0.8f
        return Color.HSVToColor(hsv)
    }
}
