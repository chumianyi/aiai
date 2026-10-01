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
 * 广告管理器。
 *
 * 提供广告加载、展示、统计等功能。
 */
object AdManager {

    private const val TAG = "AdManager"

    /**
     * 广告类型枚举。
     */
    enum class AdType(val displayName: String) {
        /** 横幅广告 */
        BANNER("横幅广告"),
        /** 插屏广告 */
        INTERSTITIAL("插屏广告"),
        /** 激励视频 */
        REWARDED("激励视频"),
        /** 原生广告 */
        NATIVE("原生广告")
    }

    /**
     * 广告状态。
     */
    sealed class AdState {
        /** 加载中。 */
        object Loading : AdState()

        /** 加载成功。 */
        object Loaded : AdState()

        /** 加载失败。 */
        data class Error(val message: String) : AdState()

        /** 展示中。 */
        object Showing : AdState()

        /** 展示完成。 */
        object Completed : AdState()
    }

    private var isEnabled: Boolean = true
    private var isVipUser: Boolean = false
    private var adCount: Int = 0
    private var onAdStateChangedListener: ((AdType, AdState) -> Unit)? = null

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "AdManager initialized")
    }

    /**
     * 设置广告开关。
     *
     * @param enabled 是否启用
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        Log.d(TAG, "Ads enabled: $enabled")
    }

    /**
     * 是否启用广告。
     *
     * @return 是否启用
     */
    fun isEnabled(): Boolean {
        return isEnabled && !isVipUser
    }

    /**
     * 设置 VIP 用户。
     *
     * @param isVip 是否 VIP
     */
    fun setVipUser(isVip: Boolean) {
        isVipUser = isVip
        Log.d(TAG, "VIP user: $isVip")
    }

    /**
     * 加载广告。
     *
     * @param type 广告类型
     */
    fun loadAd(type: AdType) {
        if (!isEnabled()) {
            Log.d(TAG, "Ads disabled, skip loading")
            return
        }
        Log.d(TAG, "Loading ad: ${type.displayName}")
        onAdStateChangedListener?.invoke(type, AdState.Loading)
        // 实际实现需要调用广告SDK
    }

    /**
     * 展示广告。
     *
     * @param type 广告类型
     */
    fun showAd(type: AdType) {
        if (!isEnabled()) {
            Log.d(TAG, "Ads disabled, skip showing")
            return
        }
        Log.d(TAG, "Showing ad: ${type.displayName}")
        adCount++
        onAdStateChangedListener?.invoke(type, AdState.Showing)
    }

    /**
     * 设置广告状态监听。
     *
     * @param listener 监听器
     */
    fun setOnAdStateChangedListener(listener: (AdType, AdState) -> Unit) {
        onAdStateChangedListener = listener
    }

    /**
     * 获取广告展示次数。
     *
     * @return 展示次数
     */
    fun getAdCount(): Int = adCount

    /**
     * 重置统计。
     */
    fun resetStats() {
        adCount = 0
        Log.d(TAG, "Ad stats reset")
    }
}
