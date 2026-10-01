/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.aiai.settings.model.ThemeMode

/**
 * 主题管理。
 *
 * 负责在 日间 / 夜间 / AMOLED 之间切换，并把 [ThemeMode] 映射到
 * [AppCompatDelegate.MODE_NIGHT_*]。主题色通过 [SettingsManager] 持久化。
 */
class ThemeManager(private val settings: SettingsManager) {

    /** 当前主题模式。 */
    val themeMode: ThemeMode get() = settings.themeMode.value

    /** 当前主题色。 */
    val themeColor: Int get() = settings.themeColor.value

    /**
     * 应用主题模式到全局（应在 Application.attachBaseContext 或 Activity.onCreate 调用）。
     */
    fun applyTheme(mode: ThemeMode) {
        settings.setThemeMode(mode)
        AppCompatDelegate.setDefaultNightMode(
            when (mode) {
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeMode.AMOLED -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

    /**
     * 设置主题色。
     */
    fun setThemeColor(color: Int) {
        settings.setThemeColor(color)
    }

    /**
     * 判断当前是否处于深色（含 AMOLED）。
     */
    fun isDark(context: Context): Boolean {
        return when (themeMode) {
            ThemeMode.SYSTEM -> {
                val ui = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                ui == Configuration.UI_MODE_NIGHT_YES
            }
            ThemeMode.DARK, ThemeMode.AMOLED -> true
            ThemeMode.LIGHT -> false
        }
    }

    /**
     * AMOLED 模式下是否使用纯黑背景。
     */
    fun isAmoled(): Boolean = themeMode == ThemeMode.AMOLED

    /**
     * 重建 Activity 以立即应用主题切换。
     */
    fun recreateActivity(activity: Activity) {
        activity.recreate()
    }

    companion object {
        @Volatile
        private var instance: ThemeManager? = null

        fun get(context: Context): ThemeManager {
            return instance ?: synchronized(this) {
                instance ?: ThemeManager(SettingsManager.get(context)).also { instance = it }
            }
        }
    }
}
