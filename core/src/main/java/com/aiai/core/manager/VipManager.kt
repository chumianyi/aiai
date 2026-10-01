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
package com.aiai.core.manager

import android.util.Log

/**
 * VIP 会员管理器。
 *
 * 提供会员状态管理、权益判断、套餐管理等功能。
 */
object VipManager {

    private const val TAG = "VipManager"

    /**
     * 会员等级枚举。
     */
    enum class VipLevel(val level: Int, val displayName: String) {
        /** 普通用户 */
        NORMAL(0, "普通用户"),
        /** 月度会员 */
        MONTHLY(1, "月度会员"),
        /** 季度会员 */
        QUARTERLY(2, "季度会员"),
        /** 年度会员 */
        YEARLY(3, "年度会员"),
        /** 终身会员 */
        LIFETIME(4, "终身会员")
    }

    /**
     * 会员权益数据类。
     *
     * @property maxConversations 最大对话数
     * @property maxTokensPerDay 每日最大Token数
     * @property advancedModels 高级模型
     * @property prioritySupport 优先客服
     * @property noAds 无广告
     */
    data class VipBenefits(
        val maxConversations: Int,
        val maxTokensPerDay: Int,
        val advancedModels: Boolean,
        val prioritySupport: Boolean,
        val noAds: Boolean
    )

    private var vipLevel: VipLevel = VipLevel.NORMAL
    private var expireTime: Long = 0L
    private var onVipChangedListener: (() -> Unit)? = null

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "VipManager initialized")
    }

    /**
     * 设置会员等级。
     *
     * @param level 会员等级
     * @param expireTime 到期时间
     */
    fun setVipLevel(level: VipLevel, expireTime: Long = 0L) {
        vipLevel = level
        this.expireTime = expireTime
        Log.d(TAG, "VIP level set: ${level.displayName}, expire=$expireTime")
        onVipChangedListener?.invoke()
    }

    /**
     * 获取会员等级。
     *
     * @return 会员等级
     */
    fun getVipLevel(): VipLevel {
        // 检查是否过期
        if (vipLevel != VipLevel.NORMAL && vipLevel != VipLevel.LIFETIME) {
            if (expireTime > 0 && System.currentTimeMillis() > expireTime) {
                vipLevel = VipLevel.NORMAL
                Log.d(TAG, "VIP expired, downgraded to normal")
                onVipChangedListener?.invoke()
            }
        }
        return vipLevel
    }

    /**
     * 是否是 VIP。
     *
     * @return 是否 VIP
     */
    fun isVip(): Boolean {
        return getVipLevel() != VipLevel.NORMAL
    }

    /**
     * 是否是终身会员。
     *
     * @return 是否终身会员
     */
    fun isLifetimeVip(): Boolean {
        return getVipLevel() == VipLevel.LIFETIME
    }

    /**
     * 获取会员到期时间。
     *
     * @return 到期时间戳
     */
    fun getExpireTime(): Long = expireTime

    /**
     * 获取会员权益。
     *
     * @return 会员权益
     */
    fun getVipBenefits(): VipBenefits {
        return when (getVipLevel()) {
            VipLevel.NORMAL -> VipBenefits(
                maxConversations = 10,
                maxTokensPerDay = 10000,
                advancedModels = false,
                prioritySupport = false,
                noAds = false
            )
            VipLevel.MONTHLY -> VipBenefits(
                maxConversations = 100,
                maxTokensPerDay = 100000,
                advancedModels = true,
                prioritySupport = false,
                noAds = true
            )
            VipLevel.QUARTERLY -> VipBenefits(
                maxConversations = 500,
                maxTokensPerDay = 500000,
                advancedModels = true,
                prioritySupport = true,
                noAds = true
            )
            VipLevel.YEARLY -> VipBenefits(
                maxConversations = -1, // 无限制
                maxTokensPerDay = -1,
                advancedModels = true,
                prioritySupport = true,
                noAds = true
            )
            VipLevel.LIFETIME -> VipBenefits(
                maxConversations = -1,
                maxTokensPerDay = -1,
                advancedModels = true,
                prioritySupport = true,
                noAds = true
            )
        }
    }

    /**
     * 设置会员变化监听。
     *
     * @param listener 监听器
     */
    fun setOnVipChangedListener(listener: () -> Unit) {
        onVipChangedListener = listener
    }

    /**
     * 重置为普通用户。
     */
    fun reset() {
        vipLevel = VipLevel.NORMAL
        expireTime = 0L
        Log.d(TAG, "VIP reset to normal")
        onVipChangedListener?.invoke()
    }
}
