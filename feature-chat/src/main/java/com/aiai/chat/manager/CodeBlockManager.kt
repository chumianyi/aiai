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
 * 消息代码块管理器
 *
 * 管理消息中代码块的显示和交互。
 */
class CodeBlockManager(private val context: Context) {

    data class CodeBlockConfig(
        val showLineNumbers: Boolean = true,
        val showCopyButton: Boolean = true,
        val showLanguageLabel: Boolean = true,
        val maxCollapsedLines: Int = 10
    )

    private val _config = MutableStateFlow(CodeBlockConfig())
    val config: StateFlow<CodeBlockConfig> = _config.asStateFlow()

    private val _expandedBlocks = MutableStateFlow<Set<String>>(emptySet())
    val expandedBlocks: StateFlow<Set<String>> = _expandedBlocks.asStateFlow()

    fun expandBlock(blockId: String) {
        _expandedBlocks.value = _expandedBlocks.value + blockId
    }

    fun collapseBlock(blockId: String) {
        _expandedBlocks.value = _expandedBlocks.value - blockId
    }

    fun isExpanded(blockId: String): Boolean {
        return blockId in _expandedBlocks.value
    }

    fun toggleBlock(blockId: String) {
        if (isExpanded(blockId)) {
            collapseBlock(blockId)
        } else {
            expandBlock(blockId)
        }
    }

    fun updateConfig(config: CodeBlockConfig) {
        _config.value = config
    }
}
