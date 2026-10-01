/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 主题管理器
 *
 * 管理应用的主题模式（浅色/深色/跟随系统）和自定义颜色。
 */
class ThemeManager(private val context: Context) {

    enum class ThemeMode(val displayName: String) {
        SYSTEM("跟随系统"),
        LIGHT("浅色模式"),
        DARK("深色模式")
    }

    data class ThemeConfig(
        val primaryColor: Int,
        val backgroundColor: Int,
        val textColor: Int,
        val bubbleUserColor: Int,
        val bubbleAiColor: Int
    )

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _config = MutableStateFlow(
        ThemeConfig(
            primaryColor = 0xFF667EEA.toInt(),
            backgroundColor = 0xFFF5F5F5.toInt(),
            textColor = 0xFF333333.toInt(),
            bubbleUserColor = 0xFF667EEA.toInt(),
            bubbleAiColor = 0xFFFFFFFF.toInt()
        )
    )
    val config: StateFlow<ThemeConfig> = _config.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun setPrimaryColor(color: Int) {
        _config.value = _config.value.copy(primaryColor = color, bubbleUserColor = color)
    }

    fun resetToDefault() {
        _themeMode.value = ThemeMode.SYSTEM
        _config.value = ThemeConfig(
            primaryColor = 0xFF667EEA.toInt(),
            backgroundColor = 0xFFF5F5F5.toInt(),
            textColor = 0xFF333333.toInt(),
            bubbleUserColor = 0xFF667EEA.toInt(),
            bubbleAiColor = 0xFFFFFFFF.toInt()
        )
    }
}
