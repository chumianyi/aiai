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
package com.aiai.chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.chat.data.model.ModelConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 新建对话ViewModel
 *
 * 管理新建对话页面的状态：模型选择、系统提示词、参数调节。
 */
class NewConversationViewModel : ViewModel() {

    private val _selectedModel = MutableStateFlow<ModelConfig?>(null)
    val selectedModel: StateFlow<ModelConfig?> = _selectedModel.asStateFlow()

    private val _systemPrompt = MutableStateFlow("")
    val systemPrompt: StateFlow<String> = _systemPrompt.asStateFlow()

    private val _temperature = MutableStateFlow(0.7)
    val temperature: StateFlow<Double> = _temperature.asStateFlow()

    private val _maxTokens = MutableStateFlow(2048)
    val maxTokens: StateFlow<Int> = _maxTokens.asStateFlow()

    private val _topP = MutableStateFlow(1.0)
    val topP: StateFlow<Double> = _topP.asStateFlow()

    private val _frequencyPenalty = MutableStateFlow(0.0)
    val frequencyPenalty: StateFlow<Double> = _frequencyPenalty.asStateFlow()

    private val _presencePenalty = MutableStateFlow(0.0)
    val presencePenalty: StateFlow<Double> = _presencePenalty.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    /**
     * 选择模型
     */
    fun selectModel(model: ModelConfig) {
        _selectedModel.value = model
        // 同步默认参数
        _temperature.value = model.defaultTemperature
        _maxTokens.value = model.defaultMaxTokens
        _topP.value = model.defaultTopP
    }

    /**
     * 更新系统提示词
     */
    fun onSystemPromptChanged(prompt: String) {
        _systemPrompt.value = prompt
    }

    /**
     * 更新温度参数
     */
    fun onTemperatureChanged(value: Float) {
        _temperature.value = value.toDouble()
    }

    /**
     * 更新最大token数
     */
    fun onMaxTokensChanged(value: Int) {
        _maxTokens.value = value
    }

    /**
     * 更新Top-P参数
     */
    fun onTopPChanged(value: Float) {
        _topP.value = value.toDouble()
    }

    /**
     * 更新频率惩罚
     */
    fun onFrequencyPenaltyChanged(value: Float) {
        _frequencyPenalty.value = value.toDouble()
    }

    /**
     * 更新存在惩罚
     */
    fun onPresencePenaltyChanged(value: Float) {
        _presencePenalty.value = value.toDouble()
    }

    /**
     * 创建新会话
     */
    fun createConversation(onCreated: (conversationId: String) -> Unit) {
        viewModelScope.launch {
            _isCreating.value = true
            // 模拟创建过程
            kotlinx.coroutines.delay(300)
            val convId = "conv_${System.currentTimeMillis()}"
            _isCreating.value = false
            onCreated(convId)
        }
    }

    /**
     * 重置表单
     */
    fun reset() {
        _systemPrompt.value = ""
        _temperature.value = 0.7
        _maxTokens.value = 2048
        _topP.value = 1.0
        _frequencyPenalty.value = 0.0
        _presencePenalty.value = 0.0
    }
}
