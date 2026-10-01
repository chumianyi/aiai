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
 * 顶部栏样式管理器
 *
 * 管理聊天页面顶部栏的样式和显示。
 */
class TopBarStyleManager(private val context: Context) {

    data class TopBarConfig(
        val showModelName: Boolean = true,
        val showOnlineStatus: Boolean = true,
        val showMoreButton: Boolean = true,
        val showBackButton: Boolean = true
    )

    private val _config = MutableStateFlow(TopBarConfig())
    val config: StateFlow<TopBarConfig> = _config.asStateFlow()

    fun updateConfig(config: TopBarConfig) {
        _config.value = config
    }

    fun shouldShowModelName(): Boolean {
        return _config.value.showModelName
    }

    fun shouldShowOnlineStatus(): Boolean {
        return _config.value.showOnlineStatus
    }
}
