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
 * 会话重命名管理器
 *
 * 管理会话标题的自动生成和手动编辑。
 */
class ConversationRenameManager(private val context: Context) {

    data class RenameConfig(
        val autoRenameEnabled: Boolean = true,
        val maxTitleLength: Int = 30,
        val useFirstMessageAsTitle: Boolean = true
    )

    private val _config = MutableStateFlow(RenameConfig())
    val config: StateFlow<RenameConfig> = _config.asStateFlow()

    fun generateTitle(firstMessage: String): String {
        val maxLen = _config.value.maxTitleLength
        var title = firstMessage.trim()
        // 移除Markdown标记
        title = title.replace(Regex("```[\\s\\S]*?```"), "")
        title = title.replace(Regex("\\s+"), " ").trim()
        return if (title.length > maxLen) {
            title.take(maxLen) + "..."
        } else {
            title
        }
    }

    fun updateConfig(config: RenameConfig) {
        _config.value = config
    }

    fun shouldAutoRename(): Boolean {
        return _config.value.autoRenameEnabled
    }
}
