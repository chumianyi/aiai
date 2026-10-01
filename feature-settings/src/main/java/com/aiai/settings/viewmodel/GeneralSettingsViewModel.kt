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
import androidx.lifecycle.viewModelScope
import com.aiai.settings.manager.CacheManager
import com.aiai.settings.manager.LanguageManager
import com.aiai.settings.manager.SettingsManager
import com.aiai.settings.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 通用设置 UI 状态。 */
data class GeneralSettingsUiState(
    val language: AppLanguage = AppLanguage.SYSTEM,
    val notification: Boolean = true,
    val autoUpdate: Boolean = true,
    val sendMethod: Int = 0,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2048,
    val cacheSize: String = "0 B",
    val cleaning: Boolean = false,
    val cleanedMessage: String? = null
)

/**
 * 通用设置 ViewModel。
 */
class GeneralSettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsManager.get(app)
    private val cache = CacheManager.get(app)
    private val languageManager = LanguageManager.get(app)

    private val _uiState = MutableStateFlow(
        GeneralSettingsUiState(
            language = settings.language.value,
            notification = settings.notificationEnabled.value,
            autoUpdate = settings.autoUpdate.value,
            sendMethod = settings.sendMethod,
            temperature = settings.defaultTemperature,
            maxTokens = settings.defaultMaxTokens
        )
    )
    val uiState: StateFlow<GeneralSettingsUiState> = _uiState.asStateFlow()

    init {
        refreshCache()
    }

    fun setLanguage(lang: AppLanguage) {
        languageManager.setLanguage(lang)
        _uiState.value = _uiState.value.copy(language = lang)
    }

    fun setNotification(enabled: Boolean) {
        settings.setNotificationEnabled(enabled)
        _uiState.value = _uiState.value.copy(notification = enabled)
    }

    fun setAutoUpdate(enabled: Boolean) {
        settings.setAutoUpdate(enabled)
        _uiState.value = _uiState.value.copy(autoUpdate = enabled)
    }

    fun setSendMethod(method: Int) {
        settings.sendMethod = method
        _uiState.value = _uiState.value.copy(sendMethod = method)
    }

    fun setTemperature(t: Float) {
        settings.defaultTemperature = t
        _uiState.value = _uiState.value.copy(temperature = t)
    }

    fun setMaxTokens(n: Int) {
        settings.defaultMaxTokens = n
        _uiState.value = _uiState.value.copy(maxTokens = n)
    }

    /** 刷新缓存大小。 */
    fun refreshCache() {
        viewModelScope.launch {
            cache.refresh()
            _uiState.value = _uiState.value.copy(
                cacheSize = CacheManager.humanSize(cache.cacheSizeFlow.value)
            )
        }
    }

    /** 执行清理。 */
    fun cleanCache() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cleaning = true)
            val freed = cache.clean()
            _uiState.value = _uiState.value.copy(
                cleaning = false,
                cacheSize = "0 B",
                cleanedMessage = "已清理 ${CacheManager.humanSize(freed)}"
            )
        }
    }
}
