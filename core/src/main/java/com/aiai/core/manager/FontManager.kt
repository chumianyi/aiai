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
package com.aiai.core.manager

import android.content.Context
import android.graphics.Typeface
import android.util.Log

/**
 * 字体管理器。
 *
 * 提供字体选择、字体大小、字体加载等功能。
 */
object FontManager {

    private const val TAG = "FontManager"

    /**
     * 字体大小枚举。
     */
    enum class FontSize(val size: Float, val displayName: String) {
        /** 小 */
        SMALL(14f, "小"),
        /** 标准 */
        NORMAL(16f, "标准"),
        /** 大 */
        LARGE(18f, "大"),
        /** 特大 */
        EXTRA_LARGE(20f, "特大")
    }

    /**
     * 字体样式枚举。
     */
    enum class FontStyle(val style: Int, val displayName: String) {
        /** 正常 */
        NORMAL(Typeface.NORMAL, "正常"),
        /** 粗体 */
        BOLD(Typeface.BOLD, "粗体"),
        /** 斜体 */
        ITALIC(Typeface.ITALIC, "斜体"),
        /** 粗斜体 */
        BOLD_ITALIC(Typeface.BOLD_ITALIC, "粗斜体")
    }

    private var currentFontSize: FontSize = FontSize.NORMAL
    private var currentFontStyle: FontStyle = FontStyle.NORMAL
    private var onFontChangedListener: (() -> Unit)? = null

    /**
     * 初始化。
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        Log.d(TAG, "FontManager initialized")
    }

    /**
     * 设置字体大小。
     *
     * @param size 字体大小
     */
    fun setFontSize(size: FontSize) {
        if (currentFontSize != size) {
            currentFontSize = size
            Log.d(TAG, "Font size changed: ${size.displayName}")
            onFontChangedListener?.invoke()
        }
    }

    /**
     * 获取字体大小。
     *
     * @return 字体大小
     */
    fun getFontSize(): FontSize = currentFontSize

    /**
     * 获取字体大小值。
     *
     * @return 字体大小值（sp）
     */
    fun getFontSizeValue(): Float = currentFontSize.size

    /**
     * 设置字体样式。
     *
     * @param style 字体样式
     */
    fun setFontStyle(style: FontStyle) {
        if (currentFontStyle != style) {
            currentFontStyle = style
            Log.d(TAG, "Font style changed: ${style.displayName}")
            onFontChangedListener?.invoke()
        }
    }

    /**
     * 获取字体样式。
     *
     * @return 字体样式
     */
    fun getFontStyle(): FontStyle = currentFontStyle

    /**
     * 获取 Typeface。
     *
     * @return Typeface
     */
    fun getTypeface(): Typeface {
        return Typeface.defaultFromStyle(currentFontStyle.style)
    }

    /**
     * 设置字体变化监听。
     *
     * @param listener 监听器
     */
    fun setOnFontChangedListener(listener: () -> Unit) {
        onFontChangedListener = listener
    }

    /**
     * 重置为默认字体。
     */
    fun resetToDefault() {
        currentFontSize = FontSize.NORMAL
        currentFontStyle = FontStyle.NORMAL
        Log.d(TAG, "Font reset to default")
        onFontChangedListener?.invoke()
    }

    /**
     * 获取支持的字体大小列表。
     *
     * @return 字体大小列表
     */
    fun getSupportedFontSizes(): List<FontSize> {
        return FontSize.values().toList()
    }

    /**
     * 获取支持的字体样式列表。
     *
     * @return 字体样式列表
     */
    fun getSupportedFontStyles(): List<FontStyle> {
        return FontStyle.values().toList()
    }
}
