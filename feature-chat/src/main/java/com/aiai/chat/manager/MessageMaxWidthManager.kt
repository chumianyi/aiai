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
 * 消息最大宽度管理器
 *
 * 管理聊天气泡的最大宽度限制。
 */
class MessageMaxWidthManager(private val context: Context) {

    data class MaxWidthConfig(
        val maxWidthRatio: Float = 0.75f,
        val minWidth: Int = 80
    )

    private val _config = MutableStateFlow(MaxWidthConfig())
    val config: StateFlow<MaxWidthConfig> = _config.asStateFlow()

    fun updateConfig(config: MaxWidthConfig) {
        _config.value = config
    }

    fun getMaxWidthRatio(): Float {
        return _config.value.maxWidthRatio
    }
}
