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
 * 消息行距管理器
 *
 * 管理聊天消息之间的垂直间距。
 */
class MessageSpacingManager(private val context: Context) {

    data class SpacingConfig(
        val messageSpacing: Int = 8,
        val avatarTextSpacing: Int = 8,
        val bubblePaddingHorizontal: Int = 12,
        val bubblePaddingVertical: Int = 8
    )

    private val _config = MutableStateFlow(SpacingConfig())
    val config: StateFlow<SpacingConfig> = _config.asStateFlow()

    fun updateConfig(config: SpacingConfig) {
        _config.value = config
    }

    fun getMessageSpacing(): Int {
        return _config.value.messageSpacing
    }
}
