/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.core.manager

import android.util.Log

/**
 * AI 模型管理器。
 *
 * 提供模型列表、模型选择、模型配置等功能。
 */
object ModelManager {

    private const val TAG = "ModelManager"

    /**
     * 模型信息数据类。
     *
     * @property id 模型ID
     * @property name 模型名称
     * @property description 描述
     * @property maxTokens 最大Token数
     * @property isVip 是否VIP模型
     * @property provider 提供商
     */
    data class ModelInfo(
        val id: String,
        val name: String,
        val description: String,
        val maxTokens: Int,
        val isVip: Boolean = false,
        val provider: String = "openai"
    )

    private val models = mutableListOf<ModelInfo>()
    private var currentModelId: String = "gpt-3.5-turbo"

    init {
        // 初始化默认模型
        models.add(ModelInfo(
            id = "gpt-3.5-turbo",
            name = "GPT-3.5 Turbo",
            description = "快速、高效的通用模型",
            maxTokens = 4096,
            isVip = false
        ))
        models.add(ModelInfo(
            id = "gpt-4",
            name = "GPT-4",
            description = "更强大、更准确的模型",
            maxTokens = 8192,
            isVip = true
        ))
        models.add(ModelInfo(
            id = "gpt-4-turbo",
            name = "GPT-4 Turbo",
            description = "GPT-4 的增强版本",
            maxTokens = 128000,
            isVip = true
        ))
        models.add(ModelInfo(
            id = "claude-3",
            name = "Claude 3",
            description = "Anthropic 的先进模型",
            maxTokens = 200000,
            isVip = true,
            provider = "anthropic"
        ))
    }

    /**
     * 获取所有模型。
     *
     * @return 模型列表
     */
    fun getAllModels(): List<ModelInfo> {
        return models.toList()
    }

    /**
     * 获取可用模型（根据VIP状态过滤）。
     *
     * @param isVip 是否VIP
     * @return 可用模型列表
     */
    fun getAvailableModels(isVip: Boolean): List<ModelInfo> {
        return if (isVip) {
            models.toList()
        } else {
            models.filter { !it.isVip }
        }
    }

    /**
     * 根据ID获取模型。
     *
     * @param id 模型ID
     * @return 模型信息
     */
    fun getModelById(id: String): ModelInfo? {
        return models.find { it.id == id }
    }

    /**
     * 设置当前模型。
     *
     * @param modelId 模型ID
     * @return 是否成功
     */
    fun setCurrentModel(modelId: String): Boolean {
        val model = getModelById(modelId) ?: return false
        currentModelId = modelId
        Log.d(TAG, "Current model set: ${model.name}")
        return true
    }

    /**
     * 获取当前模型ID。
     *
     * @return 当前模型ID
     */
    fun getCurrentModelId(): String = currentModelId

    /**
     * 获取当前模型。
     *
     * @return 当前模型
     */
    fun getCurrentModel(): ModelInfo? {
        return getModelById(currentModelId)
    }

    /**
     * 添加自定义模型。
     *
     * @param model 模型信息
     */
    fun addCustomModel(model: ModelInfo) {
        models.add(model)
        Log.d(TAG, "Custom model added: ${model.name}")
    }

    /**
     * 删除自定义模型。
     *
     * @param modelId 模型ID
     */
    fun removeCustomModel(modelId: String) {
        models.removeIf { it.id == modelId && it.provider != "openai" }
        Log.d(TAG, "Custom model removed: $modelId")
    }

    /**
     * 模型数量。
     */
    fun modelCount(): Int = models.size
}
