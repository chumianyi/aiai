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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 模型信息响应模型。
 *
 * @property id 模型ID
 * @property name 模型名称
 * @property description 模型描述
 * @property contextLength 上下文长度
 * @property pricing 定价信息
 * @property maxTokens 最大输出token数
 * @property capabilities 模型能力列表
 * @property isAvailable 是否可用
 * @property owner 模型提供方
 * @property created 创建时间戳
 */
data class ModelResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("context_length")
    val contextLength: Int = 128000,

    @SerializedName("pricing")
    val pricing: PricingInfo = PricingInfo(),

    @SerializedName("max_tokens")
    val maxTokens: Int = 4096,

    @SerializedName("capabilities")
    val capabilities: List<String> = emptyList(),

    @SerializedName("is_available")
    val isAvailable: Boolean = true,

    @SerializedName("owner")
    val owner: String = "openai",

    @SerializedName("created")
    val created: Long = 0L,
)

/**
 * 模型定价信息。
 *
 * @property prompt 每1K prompt token价格
 * @property completion 每1K completion token价格
 * @property currency 货币单位
 */
data class PricingInfo(
    @SerializedName("prompt")
    val prompt: Double = 0.0,

    @SerializedName("completion")
    val completion: Double = 0.0,

    @SerializedName("currency")
    val currency: String = "USD",
)
