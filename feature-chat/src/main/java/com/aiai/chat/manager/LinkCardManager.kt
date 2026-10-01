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
 * 消息链接管理管理器
 *
 * 管理消息中链接的预览卡片显示。
 */
class LinkCardManager(private val context: Context) {

    data class LinkCardConfig(
        val showLinkPreview: Boolean = true,
        val showFavicon: Boolean = true,
        val maxPreviewWidth: Int = 300
    )

    private val _config = MutableStateFlow(LinkCardConfig())
    val config: StateFlow<LinkCardConfig> = _config.asStateFlow()

    fun updateConfig(config: LinkCardConfig) {
        _config.value = config
    }

    fun shouldShowLinkPreview(): Boolean {
        return _config.value.showLinkPreview
    }
}
