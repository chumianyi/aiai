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
package com.aiai.core.manager

import android.content.Context
import android.content.res.Configuration
import com.aiai.common.util.storage.MMKVManager

/**
 * 主题管理器。
 *
 * 支持三种模式：
 * - MODE_SYSTEM: 跟随系统
 * - MODE_LIGHT: 浅色模式
 * - MODE_DARK: 深色模式
 */
object ThemeManager {

    const val MODE_SYSTEM = 0
    const val MODE_LIGHT = 1
    const val MODE_DARK = 2

    private const val KEY_THEME_MODE = "theme_mode"

    /** 当前主题模式。 */
    var themeMode: Int
        get() = MMKVManager.getInt(KEY_THEME_MODE, MODE_SYSTEM)
        set(value) = MMKVManager.putInt(KEY_THEME_MODE, value)

    /** 是否为深色模式。 */
    fun isDarkMode(context: Context): Boolean {
        return when (themeMode) {
            MODE_LIGHT -> false
            MODE_DARK -> true
            else -> {
                val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                nightMode == Configuration.UI_MODE_NIGHT_YES
            }
        }
    }

    /** 获取对应的 AppCompatDelegate 模式。 */
    fun getDelegateMode(): Int {
        return when (themeMode) {
            MODE_LIGHT -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            MODE_DARK -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    /** 应用主题。 */
    fun applyTheme() {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(getDelegateMode())
    }
}
