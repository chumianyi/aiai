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
 * 消息时间戳管理器
 *
 * 管理消息时间戳的显示和格式化。
 */
class TimestampDisplayManager(private val context: Context) {

    data class TimestampConfig(
        val showTimestamp: Boolean = true,
        val showRelativeTime: Boolean = true,
        val showDateDivider: Boolean = true
    )

    private val _config = MutableStateFlow(TimestampConfig())
    val config: StateFlow<TimestampConfig> = _config.asStateFlow()

    fun updateConfig(config: TimestampConfig) {
        _config.value = config
    }

    fun shouldShowTimestamp(): Boolean {
        return _config.value.showTimestamp
    }

    fun shouldShowDateDivider(): Boolean {
        return _config.value.showDateDivider
    }
}
