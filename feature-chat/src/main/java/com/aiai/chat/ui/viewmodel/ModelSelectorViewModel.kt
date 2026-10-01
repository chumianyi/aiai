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
import com.aiai.chat.manager.ModelConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 模型选择ViewModel
 *
 * 管理模型选择器页面：模型列表、详情、参数配置、自定义模型添加。
 */
class ModelSelectorViewModel : ViewModel() {

    private val modelManager = ModelConfigManager(androidx.core.content.ContextCompat.getSystemService(androidx.appcompat.app.AppLoader())!!)

    private val _models = MutableStateFlow<List<ModelConfig>>(emptyList())
    val models: StateFlow<List<ModelConfig>> = _models.asStateFlow()

    private val _selectedModel = MutableStateFlow<ModelConfig?>(null)
    val selectedModel: StateFlow<ModelConfig?> = _selectedModel.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showCustomDialog = MutableStateFlow(false)
    val showCustomDialog: StateFlow<Boolean> = _showCustomDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadModels()
    }

    /**
     * 加载模型列表
     */
    fun loadModels() {
        viewModelScope.launch {
            _isLoading.value = true
            _models.value = ModelConfig.presetModels()
            _isLoading.value = false
        }
    }

    /**
     * 搜索模型
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadModels()
        } else {
            _models.value = ModelConfig.presetModels().filter {
                it.name.contains(query, ignoreCase = true) ||
                it.provider.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
        }
    }

    /**
     * 选择模型
     */
    fun selectModel(model: ModelConfig) {
        _selectedModel.value = model
    }

    /**
     * 显示添加自定义模型对话框
     */
    fun showAddCustomModel() {
        _showCustomDialog.value = true
    }

    /**
     * 隐藏添加自定义模型对话框
     */
    fun hideAddCustomModel() {
        _showCustomDialog.value = false
    }

    /**
     * 添加自定义模型
     */
    fun addCustomModel(
        name: String,
        modelId: String,
        provider: String,
        apiEndpoint: String
    ) {
        viewModelScope.launch {
            val customModel = ModelConfig(
                id = "custom_${System.currentTimeMillis()}",
                name = name,
                modelId = modelId,
                provider = provider,
                isCustom = true,
                description = "自定义模型"
            )
            _models.value = _models.value + customModel
            hideAddCustomModel()
        }
    }

    /**
     * 删除自定义模型
     */
    fun deleteCustomModel(modelId: String) {
        _models.value = _models.value.filterNot { it.id == modelId && it.isCustom }
    }
}
