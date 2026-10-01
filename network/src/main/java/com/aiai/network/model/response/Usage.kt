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
 * Token使用量统计模型。
 *
 * @property promptTokens 提示词消耗的token数
 * @property completionTokens 生成内容消耗的token数
 * @property totalTokens 总token数
 * @property cachedTokens 缓存命中的token数
 * @property reasoningTokens 推理消耗的token数
 */
data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int = 0,

    @SerializedName("completion_tokens")
    val completionTokens: Int = 0,

    @SerializedName("total_tokens")
    val totalTokens: Int = 0,

    @SerializedName("cached_tokens")
    val cachedTokens: Int = 0,

    @SerializedName("reasoning_tokens")
    val reasoningTokens: Int = 0,
) {
    /**
     * 计算总token数（如果未提供）。
     *
     * @return 总token数
     */
    fun calculatedTotal(): Int {
        return if (totalTokens > 0) totalTokens else promptTokens + completionTokens
    }

    /**
     * 估算本次请求费用（美元）。
     *
     * @param promptPrice 每1K prompt token价格
     * @param completionPrice 每1K completion token价格
     * @return 费用（美元）
     */
    fun estimateCost(promptPrice: Double, completionPrice: Double): Double {
        val promptCost = promptTokens / 1000.0 * promptPrice
        val completionCost = completionTokens / 1000.0 * completionPrice
        return promptCost + completionCost
    }
}
