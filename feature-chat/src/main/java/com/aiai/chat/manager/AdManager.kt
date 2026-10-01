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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 广告管理器
 *
 * 管理应用内广告展示策略。
 */
class AdManager(private val context: Context) {

    enum class AdType {
        BANNER,
        INTERSTITIAL,
        REWARDED,
        NATIVE
    }

    data class AdConfig(
        val enabled: Boolean = true,
        val showBanner: Boolean = true,
        val showInterstitial: Boolean = false,
        val interstitialInterval: Int = 5
    )

    private val _config = MutableStateFlow(AdConfig())
    val config: StateFlow<AdConfig> = _config.asStateFlow()

    private val _adLoaded = MutableStateFlow(false)
    val adLoaded: StateFlow<Boolean> = _adLoaded.asStateFlow()

    private var messageCountSinceInterstitial = 0

    fun shouldShowInterstitial(): Boolean {
        if (!_config.value.showInterstitial) return false
        messageCountSinceInterstitial++
        return messageCountSinceInterstitial >= _config.value.interstitialInterval
    }

    fun onMessageSent() {
        // 消息发送后计数
    }

    fun resetInterstitialCounter() {
        messageCountSinceInterstitial = 0
    }

    fun updateConfig(config: AdConfig) {
        _config.value = config
    }
}
