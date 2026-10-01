/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 模型配置请求模型。
 *
 * 用于更新模型的参数配置。
 *
 * @property modelName 模型名称
 * @property baseUrl API基础URL
 * @property apiKey API密钥
 * @property temperature 默认温度
 * @property topP 默认top_p
 * @property maxTokens 默认最大token数
 * @property isDefault 是否为默认模型
 * @property isActive 是否激活
 * @property contextLength 上下文长度
 * @property description 模型描述
 */
data class ModelConfigRequest(
    @SerializedName("model_name")
    val modelName: String = "gpt-4o",

    @SerializedName("base_url")
    val baseUrl: String = "https://api.aiai.com/",

    @SerializedName("api_key")
    val apiKey: String = "",

    @SerializedName("temperature")
    val temperature: Double = 0.7,

    @SerializedName("top_p")
    val topP: Double = 1.0,

    @SerializedName("max_tokens")
    val maxTokens: Int = 4096,

    @SerializedName("is_default")
    val isDefault: Boolean = false,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("context_length")
    val contextLength: Int = 128000,

    @SerializedName("description")
    val description: String = "",
) {
    init {
        require(modelName.isNotBlank()) { "model_name must not be blank" }
        require(temperature in 0.0..2.0) { "temperature must be between 0 and 2" }
        require(contextLength > 0) { "context_length must be positive" }
    }
}
