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
 * 登录响应模型。
 *
 * @property accessToken 访问令牌
 * @property refreshToken 刷新令牌
 * @property tokenType 令牌类型
 * @property expiresIn 过期时间（秒）
 * @property userId 用户ID
 */
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String = "",

    @SerializedName("refresh_token")
    val refreshToken: String = "",

    @SerializedName("token_type")
    val tokenType: String = "Bearer",

    @SerializedName("expires_in")
    val expiresIn: Long = 7200,

    @SerializedName("user_id")
    val userId: String = "",
)

/**
 * 注册响应模型。
 *
 * @property userId 用户ID
 * @property username 用户名
 * @property email 邮箱
 * @property createdAt 创建时间
 */
data class RegisterResponse(
    @SerializedName("user_id")
    val userId: String = "",

    @SerializedName("username")
    val username: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",
)

/**
 * Token刷新响应模型。
 *
 * @property accessToken 新的访问令牌
 * @property expiresIn 过期时间（秒）
 */
data class RefreshTokenResponse(
    @SerializedName("access_token")
    val accessToken: String = "",

    @SerializedName("expires_in")
    val expiresIn: Long = 7200,
)

/**
 * 模型定价信息响应。
 *
 * @property modelName 模型名称
 * @property promptPrice 提示词价格（每1K token）
 * @property completionPrice 生成价格（每1K token）
 * @property currency 货币单位
 * @property contextWindow 上下文窗口大小
 * @property maxOutputTokens 最大输出token数
 */
data class ModelPricingResponse(
    @SerializedName("model_name")
    val modelName: String = "",

    @SerializedName("prompt_price")
    val promptPrice: Double = 0.0,

    @SerializedName("completion_price")
    val completionPrice: Double = 0.0,

    @SerializedName("currency")
    val currency: String = "USD",

    @SerializedName("context_window")
    val contextWindow: Int = 128000,

    @SerializedName("max_output_tokens")
    val maxOutputTokens: Int = 4096,
)

/**
 * 使用统计响应。
 *
 * @property period 统计周期
 * @property totalRequests 总请求数
 * @property totalTokens 总token数
 * @property totalCost 总费用
 * @property dailyStats 每日统计列表
 */
data class UsageStatsResponse(
    @SerializedName("period")
    val period: String = "month",

    @SerializedName("total_requests")
    val totalRequests: Long = 0L,

    @SerializedName("total_tokens")
    val totalTokens: Long = 0L,

    @SerializedName("total_cost")
    val totalCost: Double = 0.0,

    @SerializedName("daily_stats")
    val dailyStats: List<DailyStat> = emptyList(),
)

/**
 * 每日统计数据。
 *
 * @property date 日期
 * @property requests 请求数
 * @property tokens token数
 * @property cost 费用
 */
data class DailyStat(
    @SerializedName("date")
    val date: String = "",

    @SerializedName("requests")
    val requests: Long = 0L,

    @SerializedName("tokens")
    val tokens: Long = 0L,

    @SerializedName("cost")
    val cost: Double = 0.0,
)

/**
 * 健康检查响应。
 *
 * @property status 服务状态
 * @property version 服务版本
 * @property uptime 运行时间（秒）
 * @property timestamp 检查时间戳
 */
data class HealthCheckResponse(
    @SerializedName("status")
    val status: String = "healthy",

    @SerializedName("version")
    val version: String = "",

    @SerializedName("uptime")
    val uptime: Long = 0L,

    @SerializedName("timestamp")
    val timestamp: Long = 0L,
)

/**
 * 服务器时间响应。
 *
 * @property serverTime 服务器时间戳
 * @property timezone 时区
 */
data class ServerTimeResponse(
    @SerializedName("server_time")
    val serverTime: Long = 0L,

    @SerializedName("timezone")
    val timezone: String = "UTC",
)

/**
 * 功能开关响应。
 *
 * @property features 功能开关Map
 */
data class FeatureFlagsResponse(
    @SerializedName("features")
    val features: Map<String, Boolean> = emptyMap(),
)
