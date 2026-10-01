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
 * 用户信息模型
 */
data class UserInfo(
    val id: String = "",
    val nickname: String = "爱Ai用户",
    val avatarUrl: String? = null,
    val email: String = "",
    val membershipType: MembershipType = MembershipType.FREE,
    val memberExpireDate: String = "",
    val totalQuota: Int = 100,
    val usedQuota: Int = 0,
    val favoriteModel: String = "gpt-4o-mini",
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * 获取剩余配额
     */
    fun getRemainingQuota(): Int = totalQuota - usedQuota

    /**
     * 配额使用百分比
     */
    fun getQuotaPercentage(): Float {
        return if (totalQuota > 0) usedQuota.toFloat() / totalQuota else 0f
    }

    /**
     * 是否为付费会员
     */
    fun isPro(): Boolean = membershipType != MembershipType.FREE
}

/**
 * 会员类型枚举
 */
enum class MembershipType(val displayName: String) {
    FREE("免费版"),
    PLUS("Plus版"),
    PRO("Pro版"),
    ENTERPRISE("企业版")
}
