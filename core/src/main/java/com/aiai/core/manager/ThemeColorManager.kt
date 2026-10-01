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

import android.util.Log

/**
 * 主题颜色管理器。
 *
 * 提供主题颜色选择、自定义颜色等功能。
 */
object ThemeColorManager {

    private const val TAG = "ThemeColorManager"

    /**
     * 预定义颜色枚举。
     */
    enum class PresetColor(val hex: String, val displayName: String) {
        /** 蓝色 */
        BLUE("#2196F3", "蓝色"),
        /** 紫色 */
        PURPLE("#9C27B0", "紫色"),
        /** 绿色 */
        GREEN("#4CAF50", "绿色"),
        /** 橙色 */
        ORANGE("#FF9800", "橙色"),
        /** 红色 */
        RED("#F44336", "红色"),
        /** 粉色 */
        PINK("#E91E63", "粉色"),
        /** 青色 */
        TEAL("#009688", "青色"),
        /** 靛蓝 */
        INDIGO("#3F51B5", "靛蓝")
    }

    private var currentColor: PresetColor = PresetColor.BLUE
    private var customColor: String? = null
    private var onColorChangedListener: ((String) -> Unit)? = null

    /**
     * 设置预定义颜色。
     *
     * @param color 颜色
     */
    fun setColor(color: PresetColor) {
        currentColor = color
        customColor = null
        Log.d(TAG, "Theme color set: ${color.displayName}")
        onColorChangedListener?.invoke(color.hex)
    }

    /**
     * 设置自定义颜色。
     *
     * @param hex 颜色值
     */
    fun setCustomColor(hex: String) {
        customColor = hex
        Log.d(TAG, "Custom color set: $hex")
        onColorChangedListener?.invoke(hex)
    }

    /**
     * 获取当前颜色。
     *
     * @return 颜色值
     */
    fun getCurrentColor(): String {
        return customColor ?: currentColor.hex
    }

    /**
     * 获取预定义颜色。
     *
     * @return 预定义颜色
     */
    fun getCurrentPresetColor(): PresetColor = currentColor

    /**
     * 是否是自定义颜色。
     *
     * @return 是否自定义
     */
    fun isCustomColor(): Boolean = customColor != null

    /**
     * 获取所有预定义颜色。
     *
     * @return 预定义颜色列表
     */
    fun getPresetColors(): List<PresetColor> {
        return PresetColor.values().toList()
    }

    /**
     * 设置颜色变化监听。
     *
     * @param listener 监听器
     */
    fun setOnColorChangedListener(listener: (String) -> Unit) {
        onColorChangedListener = listener
    }

    /**
     * 重置为默认颜色。
     */
    fun resetToDefault() {
        currentColor = PresetColor.BLUE
        customColor = null
        Log.d(TAG, "Theme color reset to default")
        onColorChangedListener?.invoke(currentColor.hex)
    }
}
