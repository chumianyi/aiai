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
 * 快捷键管理器
 *
 * 管理键盘快捷键和手势操作的自定义映射。
 */
class ShortcutManager(private val context: Context) {

    data class ShortcutAction(
        val key: String,
        val action: String,
        val description: String
    )

    private val _shortcuts = MutableStateFlow<List<ShortcutAction>>(emptyList())
    val shortcuts: StateFlow<List<ShortcutAction>> = _shortcuts.asStateFlow()

    init {
        loadDefaultShortcuts()
    }

    private fun loadDefaultShortcuts() {
        _shortcuts.value = listOf(
            ShortcutAction("Ctrl+N", "new_conversation", "新建对话"),
            ShortcutAction("Ctrl+F", "search", "搜索对话"),
            ShortcutAction("Ctrl+Enter", "send_message", "发送消息"),
            ShortcutAction("Esc", "close_panel", "关闭面板"),
            ShortcutAction("Ctrl+1", "switch_model_1", "切换到模型1"),
            ShortcutAction("Ctrl+2", "switch_model_2", "切换到模型2")
        )
    }

    fun addShortcut(key: String, action: String, description: String) {
        val shortcut = ShortcutAction(key, action, description)
        _shortcuts.value = _shortcuts.value + shortcut
    }

    fun removeShortcut(key: String) {
        _shortcuts.value = _shortcuts.value.filterNot { it.key == key }
    }

    fun getActionForKey(key: String): String? {
        return _shortcuts.value.find { it.key == key }?.action
    }
}
