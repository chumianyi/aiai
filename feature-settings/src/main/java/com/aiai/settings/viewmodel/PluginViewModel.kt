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
import com.aiai.settings.manager.PluginManager
import com.aiai.settings.model.PluginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 插件管理 UI 状态。 */
data class PluginUiState(
    val plugins: List<PluginInfo> = emptyList(),
    val installingId: String? = null
)

/**
 * 插件管理 ViewModel。
 */
class PluginViewModel(app: Application) : AndroidViewModel(app) {

    private val manager = PluginManager.get(app)

    private val _uiState = MutableStateFlow(PluginUiState(plugins = manager.plugins.value))
    val uiState: StateFlow<PluginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            manager.plugins.collect { list ->
                _uiState.value = _uiState.value.copy(plugins = list)
            }
        }
    }

    fun install(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(installingId = id)
            manager.install(id)
            _uiState.value = _uiState.value.copy(installingId = null)
        }
    }

    fun uninstall(id: String) {
        manager.uninstall(id)
    }

    fun toggleEnabled(plugin: PluginInfo) {
        val shouldEnable = plugin.status != com.aiai.settings.model.PluginStatus.ENABLED
        manager.setEnabled(plugin.id, shouldEnable)
    }
}
