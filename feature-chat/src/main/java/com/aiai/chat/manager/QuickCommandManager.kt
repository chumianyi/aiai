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
import com.aiai.chat.data.model.PromptSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 快捷指令管理器
 *
 * 管理用户自定义的快捷指令和常用短语。
 */
class QuickCommandManager(private val context: Context) {

    data class QuickCommand(
        val id: String,
        val title: String,
        val content: String,
        val icon: String = "📝",
        val sortOrder: Int = 0
    )

    private val _commands = MutableStateFlow<List<QuickCommand>>(emptyList())
    val commands: StateFlow<List<QuickCommand>> = _commands.asStateFlow()

    init {
        loadDefaultCommands()
    }

    private fun loadDefaultCommands() {
        _commands.value = listOf(
            QuickCommand("1", "翻译", "请将以下内容翻译成英文：", "🌐", 1),
            QuickCommand("2", "总结", "请总结以下内容的要点：", "📋", 2),
            QuickCommand("3", "润色", "请帮我润色以下文字：", "✍️", 3),
            QuickCommand("4", "解释", "请用通俗易懂的语言解释这个概念：", "💡", 4),
            QuickCommand("5", "写代码", "请帮我写一个功能：", "💻", 5),
            QuickCommand("6", "改bug", "请帮我检查以下代码的问题：", "🐛", 6)
        )
    }

    fun addCommand(title: String, content: String, icon: String = "📝") {
        val command = QuickCommand(
            id = "cmd_${System.currentTimeMillis()}",
            title = title,
            content = content,
            icon = icon
        )
        _commands.value = _commands.value + command
    }

    fun updateCommand(command: QuickCommand) {
        _commands.value = _commands.value.map {
            if (it.id == command.id) command else it
        }
    }

    fun removeCommand(commandId: String) {
        _commands.value = _commands.value.filterNot { it.id == commandId }
    }

    fun searchCommands(keyword: String): List<QuickCommand> {
        if (keyword.isBlank()) return _commands.value
        return _commands.value.filter {
            it.title.contains(keyword, ignoreCase = true) ||
            it.content.contains(keyword, ignoreCase = true)
        }
    }
}
