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
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aiai.settings.manager.BubbleStyleManager
import com.aiai.settings.manager.FontScaleManager
import com.aiai.settings.manager.SettingsManager
import com.aiai.settings.manager.ThemeManager
import com.aiai.settings.model.BubbleStyle
import com.aiai.settings.model.FontScale
import com.aiai.settings.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 外观设置 UI 状态。 */
data class AppearanceUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val fontScale: FontScale = FontScale.MEDIUM,
    val bubbleStyle: BubbleStyle = BubbleStyle.ROUNDED,
    val themeColor: Int = 0,
    val animEnabled: Boolean = true
)

/**
 * 外观设置 ViewModel。
 */
class AppearanceSettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val themeManager = ThemeManager.get(app)
    private val fontManager = FontScaleManager.get(app)
    private val bubbleManager = BubbleStyleManager.get(app)
    private val settings = SettingsManager.get(app)

    private val _uiState = MutableStateFlow(
        AppearanceUiState(
            themeMode = themeManager.themeMode,
            fontScale = fontManager.current,
            bubbleStyle = bubbleManager.current,
            themeColor = themeManager.themeColor,
            animEnabled = settings.animEnabled.value
        )
    )
    val uiState: StateFlow<AppearanceUiState> = _uiState.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        themeManager.applyTheme(mode)
        _uiState.value = _uiState.value.copy(themeMode = mode)
    }

    fun setFontScale(scale: FontScale) {
        fontManager.setScale(scale)
        _uiState.value = _uiState.value.copy(fontScale = scale)
    }

    fun setBubbleStyle(style: BubbleStyle) {
        bubbleManager.setStyle(style)
        _uiState.value = _uiState.value.copy(bubbleStyle = style)
    }

    fun setThemeColor(color: Int) {
        themeManager.setThemeColor(color)
        _uiState.value = _uiState.value.copy(themeColor = color)
    }

    fun setAnimEnabled(enabled: Boolean) {
        settings.setAnimEnabled(enabled)
        _uiState.value = _uiState.value.copy(animEnabled = enabled)
    }
}
