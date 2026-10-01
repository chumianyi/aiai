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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 统计响应模型集合。
 *
 * 包含使用统计、趋势数据、排行榜等统计相关响应数据结构。
 */
sealed class StatisticsResponse {

    /**
     * 基础响应。
     */
    open class BaseResponse(
        @SerializedName("code")
        open val code: Int = 0,
        @SerializedName("message")
        open val message: String = ""
    )

    /**
     * 使用统计数据点。
     *
     * @property date 日期
     * @property value 数值
     */
    data class UsagePoint(
        @SerializedName("date")
        val date: String = "",
        @SerializedName("value")
        val value: Long = 0
    )

    /**
     * 使用统计响应。
     *
     * @property totalRequests 总请求数
     * @property totalTokens 总Token数
     * @property activeUsers 活跃用户数
     * @property points 数据点列表
     */
    data class UsageStatisticsResponse(
        @SerializedName("total_requests")
        val totalRequests: Long = 0,
        @SerializedName("total_tokens")
        val totalTokens: Long = 0,
        @SerializedName("active_users")
        val activeUsers: Int = 0,
        @SerializedName("points")
        val points: List<UsagePoint> = emptyList()
    ) : BaseResponse()

    /**
     * 趋势数据点。
     *
     * @property timestamp 时间戳
     * @property value 数值
     * @property label 标签
     */
    data class TrendPoint(
        @SerializedName("timestamp")
        val timestamp: Long = 0,
        @SerializedName("value")
        val value: Float = 0f,
        @SerializedName("label")
        val label: String = ""
    )

    /**
     * 趋势数据响应。
     *
     * @property metric 指标名称
     * @property points 数据点列表
     */
    data class TrendDataResponse(
        @SerializedName("metric")
        val metric: String = "",
        @SerializedName("points")
        val points: List<TrendPoint> = emptyList()
    ) : BaseResponse()

    /**
     * 排行榜条目。
     *
     * @property rank 排名
     * @property name 名称
     * @property value 数值
     * @property avatar 头像
     */
    data class RankingItem(
        @SerializedName("rank")
        val rank: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("value")
        val value: Long = 0,
        @SerializedName("avatar")
        val avatar: String = ""
    )

    /**
     * 排行榜响应。
     *
     * @property type 排行类型
     * @property items 排行条目列表
     */
    data class RankingResponse(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("items")
        val items: List<RankingItem> = emptyList()
    ) : BaseResponse()

    /**
     * 概览数据。
     *
     * @property totalUsers 总用户数
     * @property todayActive 今日活跃
     * @property totalRequests 总请求数
     * @property totalModels 模型总数
     */
    data class OverviewResponse(
        @SerializedName("total_users")
        val totalUsers: Long = 0,
        @SerializedName("today_active")
        val todayActive: Int = 0,
        @SerializedName("total_requests")
        val totalRequests: Long = 0,
        @SerializedName("total_models")
        val totalModels: Int = 0
    ) : BaseResponse()

    /**
     * 实时统计响应。
     *
     * @property onlineUsers 在线用户数
     * @property requestsPerSecond 每秒请求数
     * @property avgLatency 平均延迟
     */
    data class RealtimeStatisticsResponse(
        @SerializedName("online_users")
        val onlineUsers: Int = 0,
        @SerializedName("requests_per_second")
        val requestsPerSecond: Float = 0f,
        @SerializedName("avg_latency")
        val avgLatency: Long = 0
    ) : BaseResponse()
}
