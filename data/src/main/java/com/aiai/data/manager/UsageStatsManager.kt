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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.entity.ApiUsageEntity
import com.aiai.data.util.StatisticsCalculator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 用量统计管理器。
 *
 * 管理API使用统计和费用计算。
 */
class UsageStatsManager {

    companion object {
        private const val TAG = "UsageStatsManager"
    }

    /**
     * 计算每日统计。
     */
    fun calculateDailyStats(usages: List<ApiUsageEntity>): List<DailyUsageStat> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return usages.groupBy { sdf.format(Date(it.requestTime)) }
            .map { (date, list) ->
                DailyUsageStat(
                    date = date,
                    requestCount = list.size,
                    totalTokens = list.sumOf { it.totalTokens },
                    totalCost = list.sumOf { it.cost },
                    avgResponseTime = list.map { it.responseTime }.average().toLong(),
                )
            }
            .sortedBy { it.date }
    }

    /**
     * 计算模型统计。
     */
    fun calculateModelStats(usages: List<ApiUsageEntity>): List<ModelUsageStat> {
        return usages.groupBy { it.modelName }
            .map { (model, list) ->
                ModelUsageStat(
                    modelName = model,
                    requestCount = list.size,
                    totalTokens = list.sumOf { it.totalTokens },
                    totalCost = list.sumOf { it.cost },
                    successRate = list.count { it.status == "success" } * 100.0 / list.size,
                )
            }
            .sortedByDescending { it.totalCost }
    }

    /**
     * 获取本月总费用。
     */
    fun getMonthlyCost(usages: List<ApiUsageEntity>): Double {
        val (start, end) = StatisticsCalculator.getThisMonthRange()
        return usages.filter { it.requestTime in start..end }
            .sumOf { it.cost }
    }

    /**
     * 获取本月总token数。
     */
    fun getMonthlyTokens(usages: List<ApiUsageEntity>): Long {
        val (start, end) = StatisticsCalculator.getThisMonthRange()
        return usages.filter { it.requestTime in start..end }
            .sumOf { it.totalTokens.toLong() }
    }

    /**
     * 生成统计报告。
     */
    fun generateReport(usages: List<ApiUsageEntity>): String {
        val sb = StringBuilder()
        sb.appendLine("=== Usage Statistics Report ===")
        sb.appendLine("Total Records: ${usages.size}")
        sb.appendLine("Total Tokens: ${StatisticsCalculator.calculateTotalTokens(usages)}")
        sb.appendLine("Total Cost: $${"%.4f".format(StatisticsCalculator.calculateTotalCost(usages))}")
        sb.appendLine("Monthly Cost: $${"%.4f".format(getMonthlyCost(usages))}")
        sb.appendLine("Monthly Tokens: ${getMonthlyTokens(usages)}")
        sb.appendLine("================================")
        return sb.toString()
    }

    /**
     * 每日使用统计。
     */
    data class DailyUsageStat(
        val date: String,
        val requestCount: Int,
        val totalTokens: Int,
        val totalCost: Double,
        val avgResponseTime: Long,
    )

    /**
     * 模型使用统计。
     */
    data class ModelUsageStat(
        val modelName: String,
        val requestCount: Int,
        val totalTokens: Int,
        val totalCost: Double,
        val successRate: Double,
    )
}
