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
 * 用户信息响应模型。
 *
 * @property id 用户ID
 * @property username 用户名
 * @property avatarUrl 头像URL
 * @property email 邮箱
 * @property plan 订阅计划
 * @property expiresAt 会员到期时间
 * @property totalTokensUsed 累计使用token数
 * @property quota 配额信息
 * @property createdAt 注册时间
 * @property settings 用户设置
 */
data class UserInfoResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("username")
    val username: String = "",

    @SerializedName("avatar_url")
    val avatarUrl: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("plan")
    val plan: String = "free",

    @SerializedName("expires_at")
    val expiresAt: String = "",

    @SerializedName("total_tokens_used")
    val totalTokensUsed: Long = 0L,

    @SerializedName("quota")
    val quota: UserQuota = UserQuota(),

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("settings")
    val settings: Map<String, Any> = emptyMap(),
)

/**
 * 用户配额信息。
 *
 * @property dailyTokenLimit 每日token限额
 * @property dailyTokenUsed 每日已用token
 * @property monthlyTokenLimit 每月token限额
 * @property monthlyTokenUsed 每月已用token
 * @property remainingDays 剩余天数
 */
data class UserQuota(
    @SerializedName("daily_token_limit")
    val dailyTokenLimit: Long = 100_000L,

    @SerializedName("daily_token_used")
    val dailyTokenUsed: Long = 0L,

    @SerializedName("monthly_token_limit")
    val monthlyTokenLimit: Long = 3_000_000L,

    @SerializedName("monthly_token_used")
    val monthlyTokenUsed: Long = 0L,

    @SerializedName("remaining_days")
    val remainingDays: Int = 30,
) {
    /**
     * 计算每日剩余配额。
     *
     * @return 剩余token数
     */
    fun dailyRemaining(): Long = (dailyTokenLimit - dailyTokenUsed).coerceAtLeast(0L)

    /**
     * 计算每日配额使用百分比。
     *
     * @return 百分比（0-100）
     */
    fun dailyUsagePercent(): Int {
        if (dailyTokenLimit == 0L) return 0
        return (dailyTokenUsed * 100 / dailyTokenLimit).toInt().coerceAtMost(100)
    }
}
