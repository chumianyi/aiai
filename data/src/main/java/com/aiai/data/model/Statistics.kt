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
package com.aiai.data.model

/**
 * 统计域模型。
 *
 * 表示使用统计、趋势数据等统计信息。
 */
data class Statistics(
    val totalRequests: Long,
    val totalTokens: Long,
    val activeUsers: Int,
    val period: String,
    val points: List<StatisticsPoint>
)

/**
 * 统计数据点。
 *
 * @property timestamp 时间戳
 * @property value 数值
 * @property label 标签
 */
data class StatisticsPoint(
    val timestamp: Long,
    val value: Long,
    val label: String = ""
)

/**
 * 排行榜条目。
 *
 * @property rank 排名
 * @property name 名称
 * @property value 数值
 * @property avatar 头像
 */
data class RankingItem(
    val rank: Int,
    val name: String,
    val value: Long,
    val avatar: String = ""
)

/**
 * 概览统计数据。
 *
 * @property totalUsers 总用户数
 * @property todayActive 今日活跃
 * @property totalRequests 总请求数
 * @property totalModels 模型总数
 */
data class OverviewStatistics(
    val totalUsers: Long,
    val todayActive: Int,
    val totalRequests: Long,
    val totalModels: Int
)
