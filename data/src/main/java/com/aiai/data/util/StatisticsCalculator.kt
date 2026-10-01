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
package com.aiai.data.util

import android.util.Log
import com.aiai.data.entity.ApiUsageEntity
import com.aiai.data.entity.MessageEntity
import java.util.Calendar
import java.util.Date

/**
 * 统计计算工具。
 *
 * 提供token使用统计、费用计算等功能。
 */
object StatisticsCalculator {

    private const val TAG = "StatisticsCalculator"

    /**
     * 模型定价表（每1K token，美元）。
     */
    private val modelPricing = mapOf(
        "gpt-4o" to Pair(0.005, 0.015),
        "gpt-4o-mini" to Pair(0.00015, 0.0006),
        "gpt-4-turbo" to Pair(0.01, 0.03),
        "gpt-3.5-turbo" to Pair(0.0005, 0.0015),
        "claude-3-opus" to Pair(0.015, 0.075),
        "claude-3-sonnet" to Pair(0.003, 0.015),
        "claude-3-haiku" to Pair(0.00025, 0.00125),
    )

    /**
     * 计算单次请求费用。
     *
     * @param modelName 模型名称
     * @param promptTokens 提示词token数
     * @param completionTokens 生成token数
     * @return 费用（美元）
     */
    fun calculateCost(modelName: String, promptTokens: Int, completionTokens: Int): Double {
        val pricing = modelPricing[modelName] ?: return 0.0
        val promptCost = promptTokens / 1000.0 * pricing.first
        val completionCost = completionTokens / 1000.0 * pricing.second
        return (promptCost + completionCost) * 10000.0 / 10000.0
    }

    /**
     * 计算使用记录列表的总费用。
     *
     * @param usages 使用记录列表
     * @return 总费用
     */
    fun calculateTotalCost(usages: List<ApiUsageEntity>): Double {
        return usages.sumOf { it.cost }
    }

    /**
     * 计算使用记录列表的总token数。
     *
     * @param usages 使用记录列表
     * @return 总token数
     */
    fun calculateTotalTokens(usages: List<ApiUsageEntity>): Long {
        return usages.sumOf { it.totalTokens.toLong() }
    }

    /**
     * 按模型分组统计token使用。
     *
     * @param usages 使用记录列表
     * @return 模型→token数映射
     */
    fun groupTokensByModel(usages: List<ApiUsageEntity>): Map<String, Long> {
        return usages.groupBy { it.modelName }
            .mapValues { (_, list) -> list.sumOf { it.totalTokens.toLong() } }
    }

    /**
     * 按日期分组统计token使用。
     *
     * @param usages 使用记录列表
     * @return 日期→token数映射
     */
    fun groupTokensByDate(usages: List<ApiUsageEntity>): Map<String, Long> {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        return usages.groupBy { sdf.format(Date(it.requestTime)) }
            .mapValues { (_, list) -> list.sumOf { it.totalTokens.toLong() } }
    }

    /**
     * 获取本周的时间范围。
     *
     * @return Pair(开始时间, 结束时间)
     */
    fun getThisWeekRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val start = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_WEEK, 7)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    /**
     * 获取本月的时间范围。
     *
     * @return Pair(开始时间, 结束时间)
     */
    fun getThisMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    /**
     * 计算消息列表的总token数。
     *
     * @param messages 消息列表
     * @return 总token数
     */
    fun calculateMessageTokens(messages: List<MessageEntity>): Int {
        return messages.sumOf { it.tokenCount }
    }

    /**
     * 计算上下文窗口使用率。
     *
     * @param usedTokens 已用token数
     * @param contextLength 上下文窗口大小
     * @return 百分比（0-100）
     */
    fun calculateContextUsagePercent(usedTokens: Int, contextLength: Int): Int {
        if (contextLength <= 0) return 0
        return (usedTokens * 100.0 / contextLength).toInt().coerceAtMost(100)
    }

    /**
     * 获取费用最高的模型。
     *
     * @param usages 使用记录列表
     * @return Pair(模型名, 费用)
     */
    fun getMostExpensiveModel(usages: List<ApiUsageEntity>): Pair<String, Double>? {
        return usages.groupBy { it.modelName }
            .mapValues { (_, list) -> list.sumOf { it.cost } }
            .maxByOrNull { it.value }
            ?.let { Pair(it.key, it.value) }
    }
}
