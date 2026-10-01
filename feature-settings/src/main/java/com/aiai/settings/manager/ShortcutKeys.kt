/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 快捷键列表。
 */
object ShortcutKeys {

    data class Shortcut(val action: String, val key: String)

    fun all(): List<Shortcut> = listOf(
        Shortcut("新建对话", "Ctrl+N"),
        Shortcut("搜索", "Ctrl+K"),
        Shortcut("设置", "Ctrl+,"),
        Shortcut("语音输入", "Ctrl+Shift+V"),
        Shortcut("导出", "Ctrl+E")
    )
}
