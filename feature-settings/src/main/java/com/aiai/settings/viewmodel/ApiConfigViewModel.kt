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
import com.aiai.settings.manager.ApiConfigManager
import com.aiai.settings.model.ApiConfig
import com.aiai.settings.model.ApiTestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** API 配置页 UI 状态。 */
data class ApiConfigUiState(
    val config: ApiConfig = ApiConfig.default(),
    val testing: Boolean = false,
    val testResult: ApiTestResult? = null,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

/**
 * API 配置 ViewModel。
 *
 * 负责：字段校验、连接测试（协程）、加密保存。
 */
class ApiConfigViewModel(app: Application) : AndroidViewModel(app) {

    private val manager = ApiConfigManager.get(app)

    private val _uiState = MutableStateFlow(ApiConfigUiState())
    val uiState: StateFlow<ApiConfigUiState> = _uiState.asStateFlow()

    /** 加载指定配置，[configId] 为空则新建。 */
    fun load(configId: String?) {
        val cfg = configId?.let { manager.getById(it) } ?: ApiConfig.default()
        _uiState.value = _uiState.value.copy(config = cfg, error = null, testResult = null)
    }

    fun updateBaseUrl(url: String) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(baseUrl = url))
    }

    fun updateApiKey(key: String) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(apiKey = key))
    }

    fun updateModel(model: String) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(modelName = model))
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(name = name))
    }

    fun updateTemperature(t: Float) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(temperature = t))
    }

    fun updateMaxTokens(n: Int) {
        _uiState.value = _uiState.value.copy(config = _uiState.value.config.copy(maxTokens = n))
    }

    /**
     * 测试连接。
     */
    fun test() {
        val cfg = _uiState.value.config
        manager.validate(cfg)?.let { err ->
            _uiState.value = _uiState.value.copy(error = err)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(testing = true, testResult = null, error = null)
            val result = manager.testConnection(cfg)
            _uiState.value = _uiState.value.copy(testing = false, testResult = result)
        }
    }

    /**
     * 保存配置（密钥自动加密）。
     */
    fun save() {
        val cfg = _uiState.value.config
        manager.validate(cfg)?.let { err ->
            _uiState.value = _uiState.value.copy(error = err)
            return
        }
        manager.save(cfg)
        if (!manager.configs.value.any { it.isActive }) {
            manager.setActive(cfg.id)
        }
        _uiState.value = _uiState.value.copy(saveSuccess = true, error = null)
    }
}
