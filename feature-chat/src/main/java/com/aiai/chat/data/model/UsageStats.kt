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

/**
 * Token使用统计模型
 */
data class UsageStats(
    val promptTokens: Int = 0,
    val completionTokens: Int = 0,
    val totalTokens: Int = 0
) {
    /**
     * 获取总token数
     */
    fun getTotal(): Int = totalTokens

    /**
     * 计算费用（美元）
     */
    fun calculateCost(inputPricePer1k: Double, outputPricePer1k: Double): Double {
        val inputCost = (promptTokens / 1000.0) * inputPricePer1k
        val outputCost = (completionTokens / 1000.0) * outputPricePer1k
        return inputCost + outputCost
    }
}
