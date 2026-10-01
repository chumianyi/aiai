/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.util

import android.util.Log

/**
 * 主题设置工具。
 *
 * 管理应用主题模式相关设置。
 */
object ThemeHelper {

    private const val TAG = "ThemeHelper"

    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"

    /**
     * 获取所有可用主题模式。
     */
    fun getAvailableThemes(): List<String> = listOf(THEME_LIGHT, THEME_DARK, THEME_SYSTEM)

    /**
     * 获取主题显示名称。
     */
    fun getThemeDisplayName(theme: String): String {
        return when (theme) {
            THEME_LIGHT -> "浅色模式"
            THEME_DARK -> "深色模式"
            THEME_SYSTEM -> "跟随系统"
            else -> theme
        }
    }

    /**
     * 检查是否为有效的主题模式。
     */
    fun isValidTheme(theme: String): Boolean {
        return theme in getAvailableThemes()
    }
}

/**
 * 语言设置工具。
 */
object LanguageHelper {

    private const val TAG = "LanguageHelper"

    const val LANG_ZH_CN = "zh-CN"
    const val LANG_EN_US = "en-US"
    const val LANG_JA_JP = "ja-JP"

    fun getAvailableLanguages(): List<String> = listOf(LANG_ZH_CN, LANG_EN_US, LANG_JA_JP)

    fun getLanguageDisplayName(lang: String): String {
        return when (lang) {
            LANG_ZH_CN -> "简体中文"
            LANG_EN_US -> "English"
            LANG_JA_JP -> "日本語"
            else -> lang
        }
    }
}

/**
 * 气泡样式工具。
 */
object BubbleStyleHelper {

    const val STYLE_MODERN = "modern"
    const val STYLE_CLASSIC = "classic"
    const val STYLE_MINIMAL = "minimal"

    fun getAvailableStyles(): List<String> = listOf(STYLE_MODERN, STYLE_CLASSIC, STYLE_MINIMAL)

    fun getStyleDisplayName(style: String): String {
        return when (style) {
            STYLE_MODERN -> "现代"
            STYLE_CLASSIC -> "经典"
            STYLE_MINIMAL -> "极简"
            else -> style
        }
    }
}
