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
package com.aiai.chat.data.model

import com.aiai.chat.data.enums.ModelCapability

/**
 * AI模型配置数据模型
 *
 * 描述一个可用的AI模型的完整配置信息。
 *
 * @property id 模型唯一标识
 * @property name 模型显示名称
 * @property modelId 模型API标识
 * @property provider 模型提供方
 * @property description 模型描述
 * @property capabilities 模型支持的能力列表
 * @property maxContextTokens 最大上下文token数
 * @property maxOutputTokens 最大输出token数
 * @property supportsStreaming 是否支持流式输出
 * @property supportsFunctionCalling 是否支持函数调用
 * @property defaultTemperature 默认温度参数
 * @property defaultTopP 默认Top-P参数
 * @property defaultMaxTokens 默认最大token数
 * @property inputPrice 输入价格（每1K token）
 * @property outputPrice 输出价格（每1K token）
 * @property isCustom 是否为自定义添加的模型
 * @property isEnabled 是否启用
 * @property sortOrder 排序顺序
 * @property avatarUrl 模型头像URL
 * @property badges 模型标签（如"热门"、"新上线"）
 * @property createdAt 添加时间
 * @property updatedAt 更新时间
 */
data class ModelConfig(
    val id: String,
    val name: String,
    val modelId: String,
    val provider: String = "OpenAI",
    val description: String = "",
    val capabilities: Set<ModelCapability> = setOf(ModelCapability.TEXT_CHAT, ModelCapability.STREAMING),
    val maxContextTokens: Int = 4096,
    val maxOutputTokens: Int = 4096,
    val supportsStreaming: Boolean = true,
    val supportsFunctionCalling: Boolean = false,
    val defaultTemperature: Double = 0.7,
    val defaultTopP: Double = 1.0,
    val defaultMaxTokens: Int = 2048,
    val inputPrice: Double = 0.0,
    val outputPrice: Double = 0.0,
    val isCustom: Boolean = false,
    val isEnabled: Boolean = true,
    val sortOrder: Int = 0,
    val avatarUrl: String? = null,
    val badges: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * 判断是否支持某项能力
     */
    fun hasCapability(capability: ModelCapability): Boolean {
        return capabilities.contains(capability)
    }

    /**
     * 获取能力标签列表（用于UI展示）
     */
    fun getCapabilityLabels(): List<String> {
        return capabilities.map { cap ->
            when (cap) {
                ModelCapability.TEXT_CHAT -> "文本对话"
                ModelCapability.IMAGE_GENERATION -> "图片生成"
                ModelCapability.IMAGE_UNDERSTANDING -> "图片理解"
                ModelCapability.FUNCTION_CALL -> "函数调用"
                ModelCapability.STREAMING -> "流式输出"
                ModelCapability.LONG_CONTEXT -> "长上下文"
                ModelCapability.CODE_GENERATION -> "代码生成"
                ModelCapability.SPEECH_TO_TEXT -> "语音识别"
                ModelCapability.TEXT_TO_SPEECH -> "语音合成"
                ModelCapability.WEB_BROWSING -> "网页浏览"
            }
        }
    }

    companion object {
        /**
         * 预设模型列表
         */
        fun presetModels(): List<ModelConfig> {
            return listOf(
                ModelConfig(
                    id = "gpt-4o",
                    name = "GPT-4o",
                    modelId = "gpt-4o",
                    provider = "OpenAI",
                    description = "最新一代多模态模型，支持文本和图片理解，响应速度快",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.IMAGE_UNDERSTANDING,
                        ModelCapability.STREAMING,
                        ModelCapability.FUNCTION_CALL,
                        ModelCapability.CODE_GENERATION
                    ),
                    maxContextTokens = 128000,
                    maxOutputTokens = 4096,
                    defaultTemperature = 0.7,
                    inputPrice = 0.005,
                    outputPrice = 0.015,
                    sortOrder = 1,
                    badges = listOf("热门", "推荐")
                ),
                ModelConfig(
                    id = "gpt-4o-mini",
                    name = "GPT-4o mini",
                    modelId = "gpt-4o-mini",
                    provider = "OpenAI",
                    description = "轻量级多模态模型，性价比高，适合日常对话",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.IMAGE_UNDERSTANDING,
                        ModelCapability.STREAMING,
                        ModelCapability.FUNCTION_CALL
                    ),
                    maxContextTokens = 128000,
                    maxOutputTokens = 4096,
                    defaultTemperature = 0.7,
                    inputPrice = 0.00015,
                    outputPrice = 0.0006,
                    sortOrder = 2,
                    badges = listOf("经济")
                ),
                ModelConfig(
                    id = "gpt-4-turbo",
                    name = "GPT-4 Turbo",
                    modelId = "gpt-4-turbo",
                    provider = "OpenAI",
                    description = "强大的推理模型，支持长上下文和复杂任务",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.STREAMING,
                        ModelCapability.FUNCTION_CALL,
                        ModelCapability.LONG_CONTEXT,
                        ModelCapability.CODE_GENERATION
                    ),
                    maxContextTokens = 128000,
                    maxOutputTokens = 4096,
                    defaultTemperature = 0.7,
                    inputPrice = 0.01,
                    outputPrice = 0.03,
                    sortOrder = 3
                ),
                ModelConfig(
                    id = "dall-e-3",
                    name = "DALL-E 3",
                    modelId = "dall-e-3",
                    provider = "OpenAI",
                    description = "高质量图片生成模型，支持文本到图像创作",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.IMAGE_GENERATION
                    ),
                    maxContextTokens = 4096,
                    maxOutputTokens = 0,
                    supportsStreaming = false,
                    sortOrder = 4,
                    badges = listOf("画图")
                ),
                ModelConfig(
                    id = "claude-3-opus",
                    name = "Claude 3 Opus",
                    modelId = "claude-3-opus-20240229",
                    provider = "Anthropic",
                    description = "Claude系列最强模型，擅长分析和写作",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.IMAGE_UNDERSTANDING,
                        ModelCapability.STREAMING,
                        ModelCapability.LONG_CONTEXT
                    ),
                    maxContextTokens = 200000,
                    maxOutputTokens = 4096,
                    defaultTemperature = 0.7,
                    inputPrice = 0.015,
                    outputPrice = 0.075,
                    sortOrder = 5
                ),
                ModelConfig(
                    id = "claude-3-sonnet",
                    name = "Claude 3 Sonnet",
                    modelId = "claude-3-sonnet-20240229",
                    provider = "Anthropic",
                    description = "平衡性能与速度的模型，适合日常使用",
                    capabilities = setOf(
                        ModelCapability.TEXT_CHAT,
                        ModelCapability.IMAGE_UNDERSTANDING,
                        ModelCapability.STREAMING,
                        ModelCapability.LONG_CONTEXT
                    ),
                    maxContextTokens = 200000,
                    maxOutputTokens = 4096,
                    defaultTemperature = 0.7,
                    inputPrice = 0.003,
                    outputPrice = 0.015,
                    sortOrder = 6
                )
            )
        }
    }
}
