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
import com.aiai.chat.data.model.ModelConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * API配置管理器
 *
 * 管理API连接配置，包括Base URL、API Key、超时设置等。
 * 支持多配置切换和自定义端点。
 */
class ApiConfigManager(private val context: Context) {

    data class ApiConfig(
        val name: String,
        val baseUrl: String,
        val apiKey: String,
        val timeoutSeconds: Long = 30,
        val maxRetries: Int = 3,
        val headers: Map<String, String> = emptyMap()
    )

    private val _currentConfig = MutableStateFlow<ApiConfig?>(null)
    val currentConfig: StateFlow<ApiConfig?> = _currentConfig.asStateFlow()

    private val _configs = MutableStateFlow<List<ApiConfig>>(emptyList())
    val configs: StateFlow<List<ApiConfig>> = _configs.asStateFlow()

    init {
        loadDefaultConfigs()
    }

    private fun loadDefaultConfigs() {
        val defaults = listOf(
            ApiConfig(
                name = "OpenAI官方",
                baseUrl = "https://api.openai.com/v1",
                apiKey = ""
            ),
            ApiConfig(
                name = "Azure OpenAI",
                baseUrl = "",
                apiKey = ""
            ),
            ApiConfig(
                name = "自定义",
                baseUrl = "",
                apiKey = ""
            )
        )
        _configs.value = defaults
        _currentConfig.value = defaults.first()
    }

    fun switchConfig(config: ApiConfig) {
        _currentConfig.value = config
    }

    fun addConfig(config: ApiConfig) {
        _configs.value = _configs.value + config
    }

    fun removeConfig(name: String) {
        _configs.value = _configs.value.filterNot { it.name == name }
    }

    fun updateCurrentConfig(baseUrl: String, apiKey: String) {
        val current = _currentConfig.value ?: return
        _currentConfig.value = current.copy(baseUrl = baseUrl, apiKey = apiKey)
    }

    fun isConfigured(): Boolean {
        return !_currentConfig.value?.apiKey.isNullOrEmpty()
    }
}
