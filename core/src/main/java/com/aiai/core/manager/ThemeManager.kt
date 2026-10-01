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

import android.app.Activity
import android.content.res.Configuration
import com.aiai.common.util.storage.MMKVManager

/**
 * 主题管理器。
 *
 * 支持：跟随系统 / 浅色 / 深色。
 */
object ThemeManager {

    const val MODE_SYSTEM = 0
    const val MODE_LIGHT = 1
    const val MODE_DARK = 2

    var themeMode: Int
        get() = MMKVManager.getInt("theme_mode", MODE_SYSTEM)
        set(value) = MMKVManager.putInt("theme_mode", value)

    /** 应用主题。 */
    fun applyTheme(activity: Activity) {
        val mode = when (themeMode) {
            MODE_LIGHT -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            MODE_DARK -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(mode)
    }

    /** 是否为深色模式。 */
    fun isDarkMode(context: android.content.Context): Boolean {
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }
}
