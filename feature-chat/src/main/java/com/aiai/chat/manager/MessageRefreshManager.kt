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
 * 消息刷新管理器
 *
 * 管理聊天页面的下拉刷新功能。
 */
class MessageRefreshManager(private val context: Context) {

    data class RefreshConfig(
        val enablePullToRefresh: Boolean = true,
        val refreshTarget: RefreshTarget = RefreshTarget.OLDER_MESSAGES
    )

    enum class RefreshTarget {
        OLDER_MESSAGES,
        LATEST_MESSAGES
    }

    private val _config = MutableStateFlow(RefreshConfig())
    val config: StateFlow<RefreshConfig> = _config.asStateFlow()

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing.asStateFlow()

    fun startRefresh() {
        _refreshing.value = true
    }

    fun stopRefresh() {
        _refreshing.value = false
    }

    fun isRefreshing(): Boolean {
        return _refreshing.value
    }

    fun updateConfig(config: RefreshConfig) {
        _config.value = config
    }
}
