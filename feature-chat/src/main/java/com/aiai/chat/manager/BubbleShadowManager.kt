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
 * 消息阴影管理器
 *
 * 管理聊天气泡的阴影效果。
 */
class BubbleShadowManager(private val context: Context) {

    data class ShadowConfig(
        val showShadow: Boolean = true,
        val shadowElevation: Float = 2f,
        val shadowColor: Int = 0x33000000
    )

    private val _config = MutableStateFlow(ShadowConfig())
    val config: StateFlow<ShadowConfig> = _config.asStateFlow()

    fun updateConfig(config: ShadowConfig) {
        _config.value = config
    }

    fun shouldShowShadow(): Boolean {
        return _config.value.showShadow
    }
}
