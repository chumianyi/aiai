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
 * 模型配置管理器
 *
 * 管理所有可用AI模型的配置，包括预设模型和用户自定义模型。
 * 提供模型增删改查、切换当前模型等功能。
 */
class ModelConfigManager(private val context: Context) {

    private val _models = MutableStateFlow<List<ModelConfig>>(emptyList())
    val models: StateFlow<List<ModelConfig>> = _models.asStateFlow()

    private val _currentModelId = MutableStateFlow("gpt-4o-mini")
    val currentModelId: StateFlow<String> = _currentModelId.asStateFlow()

    private val _customModels = MutableStateFlow<List<ModelConfig>>(emptyList())
    val customModels: StateFlow<List<ModelConfig>> = _customModels.asStateFlow()

    init {
        loadPresetModels()
    }

    /**
     * 加载预设模型列表
     */
    private fun loadPresetModels() {
        _models.value = ModelConfig.presetModels()
    }

    /**
     * 获取当前选中的模型
     */
    fun getCurrentModel(): ModelConfig? {
        return _models.value.find { it.id == _currentModelId.value }
    }

    /**
     * 切换当前模型
     */
    fun switchModel(modelId: String) {
        _currentModelId.value = modelId
    }

    /**
     * 添加自定义模型
     */
    fun addCustomModel(model: ModelConfig) {
        val custom = model.copy(isCustom = true)
        _customModels.value = _customModels.value + custom
        _models.value = _models.value + custom
    }

    /**
     * 删除自定义模型
     */
    fun removeCustomModel(modelId: String) {
        _customModels.value = _customModels.value.filterNot { it.id == modelId }
        _models.value = _models.value.filterNot { it.id == modelId || !it.isCustom }
    }

    /**
     * 更新模型配置
     */
    fun updateModel(model: ModelConfig) {
        _models.value = _models.value.map {
            if (it.id == model.id) model else it
        }
    }

    /**
     * 根据ID获取模型
     */
    fun getModelById(modelId: String): ModelConfig? {
        return _models.value.find { it.id == modelId }
    }

    /**
     * 按提供方筛选模型
     */
    fun getModelsByProvider(provider: String): List<ModelConfig> {
        return _models.value.filter { it.provider == provider }
    }

    /**
     * 获取所有提供方
     */
    fun getProviders(): List<String> {
        return _models.value.map { it.provider }.distinct()
    }
}
