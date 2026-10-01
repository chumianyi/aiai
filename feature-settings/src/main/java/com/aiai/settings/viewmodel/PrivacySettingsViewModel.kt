/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.settings.manager.PrivacyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 隐私设置 UI 状态。 */
data class PrivacyUiState(
    val dataCollection: Boolean = false,
    val saveChatHistory: Boolean = true,
    val clearing: Boolean = false,
    val message: String? = null
)

/**
 * 隐私设置 ViewModel。
 */
class PrivacySettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val manager = PrivacyManager.get(app)

    private val _uiState = MutableStateFlow(
        PrivacyUiState(
            dataCollection = manager.dataCollectionEnabled,
            saveChatHistory = manager.saveChatHistory
        )
    )
    val uiState: StateFlow<PrivacyUiState> = _uiState.asStateFlow()

    fun setDataCollection(enabled: Boolean) {
        manager.dataCollectionEnabled = enabled
        _uiState.value = _uiState.value.copy(dataCollection = enabled)
    }

    fun setSaveChatHistory(enabled: Boolean) {
        manager.saveChatHistory = enabled
        _uiState.value = _uiState.value.copy(saveChatHistory = enabled)
    }

    /** 清除全部数据。 */
    fun clearAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(clearing = true)
            manager.clearAllUserData {
                _uiState.value = _uiState.value.copy(clearing = false, message = "数据已清除")
            }
        }
    }
}
