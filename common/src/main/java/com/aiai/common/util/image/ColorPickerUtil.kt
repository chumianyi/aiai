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
package com.aiai.common.util.image

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * 颜色提取工具类。
 *
 * 从 Bitmap 中提取主色调、平均色等。
 */
object ColorPickerUtil {

    /**
     * 提取 Bitmap 的平均颜色。
     */
    @ColorInt
    fun averageColor(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height
        var r = 0L
        var g = 0L
        var b = 0L
        var count = 0L
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        for (pixel in pixels) {
            r += Color.red(pixel)
            g += Color.green(pixel)
            b += Color.blue(pixel)
            count++
        }
        return Color.rgb((r / count).toInt(), (g / count).toInt(), (b / count).toInt())
    }

    /**
     * 提取主色调（简化版：采样网格统计）。
     */
    @ColorInt
    fun dominantColor(bitmap: Bitmap): Int {
        val step = 10
        val colorMap = mutableMapOf<Int, Int>()
        for (x in 0 until bitmap.width step step) {
            for (y in 0 until bitmap.height step step) {
                val pixel = bitmap.getPixel(x, y)
                val quantized = Color.rgb(
                    Color.red(pixel) / 32 * 32,
                    Color.green(pixel) / 32 * 32,
                    Color.blue(pixel) / 32 * 32
                )
                colorMap[quantized] = (colorMap[quantized] ?: 0) + 1
            }
        }
        return colorMap.maxByOrNull { it.value }?.key ?: Color.GRAY
    }
}
