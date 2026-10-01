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
package com.aiai.network.api

import com.aiai.network.model.response.StatisticsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 统计 API 服务接口。
 *
 * 提供使用统计、趋势数据、排行榜等统计相关接口。
 */
interface StatisticsApiService {

    /**
     * 获取使用统计。
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 使用统计响应
     */
    @GET("statistics/usage")
    suspend fun getUsageStatistics(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<StatisticsResponse.UsageStatisticsResponse>

    /**
     * 获取趋势数据。
     *
     * @param metric 指标名称
     * @param period 时间周期（day/week/month）
     * @return 趋势数据响应
     */
    @GET("statistics/trend")
    suspend fun getTrendData(
        @Query("metric") metric: String,
        @Query("period") period: String = "day"
    ): Response<StatisticsResponse.TrendDataResponse>

    /**
     * 获取排行榜。
     *
     * @param type 排行类型
     * @param limit 数量限制
     * @return 排行榜响应
     */
    @GET("statistics/ranking")
    suspend fun getRanking(
        @Query("type") type: String,
        @Query("limit") limit: Int = 10
    ): Response<StatisticsResponse.RankingResponse>

    /**
     * 获取概览数据。
     *
     * @return 概览响应
     */
    @GET("statistics/overview")
    suspend fun getOverview(): Response<StatisticsResponse.OverviewResponse>

    /**
     * 上报统计事件。
     *
     * @param eventName 事件名称
     * @param properties 事件属性
     * @return 上报响应
     */
    @GET("statistics/track")
    suspend fun trackEvent(
        @Query("event_name") eventName: String,
        @Query("properties") properties: String? = null
    ): Response<StatisticsResponse.BaseResponse>

    /**
     * 获取实时统计。
     *
     * @return 实时统计响应
     */
    @GET("statistics/realtime")
    suspend fun getRealtimeStatistics(): Response<StatisticsResponse.RealtimeStatisticsResponse>
}
