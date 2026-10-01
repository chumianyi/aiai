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
 * 消息自动滚动管理器
 *
 * 管理聊天列表的自动滚动到底部功能。
 */
class AutoScrollManager(private val context: Context) {

    data class AutoScrollConfig(
        val enabled: Boolean = true,
        val scrollThreshold: Int = 100,
        val smoothScroll: Boolean = true
    )

    private val _config = MutableStateFlow(AutoScrollConfig())
    val config: StateFlow<AutoScrollConfig> = _config.asStateFlow()

    private val _shouldAutoScroll = MutableStateFlow(true)
    val shouldAutoScroll: StateFlow<Boolean> = _shouldAutoScroll.asStateFlow()

    fun onScrolled(dy: Int, isAtBottom: Boolean) {
        if (dy < 0 && !isAtBottom) {
            _shouldAutoScroll.value = false
        } else if (isAtBottom) {
            _shouldAutoScroll.value = true
        }
    }

    fun forceScrollToBottom() {
        _shouldAutoScroll.value = true
    }

    fun updateConfig(config: AutoScrollConfig) {
        _config.value = config
    }

    fun isAutoScrollEnabled(): Boolean {
        return _config.value.enabled
    }
}
