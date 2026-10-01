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
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息导出管理器
 *
 * 管理单条消息的导出和分享。
 */
class MessageExportManager(private val context: Context) {

    enum class ExportType {
        TEXT,
        MARKDOWN,
        IMAGE,
        PDF
    }

    data class ExportResult(
        val success: Boolean,
        val filePath: String? = null,
        val error: String? = null
    )

    private val _exporting = MutableStateFlow(false)
    val exporting: StateFlow<Boolean> = _exporting.asStateFlow()

    fun exportAsText(message: ChatMessage): String {
        return """
            角色: ${message.role.name}
            时间: ${System.currentTimeMillis()}
            
            内容:
            ${message.content}
        """.trimIndent()
    }

    fun exportAsMarkdown(message: ChatMessage): String {
        val roleEmoji = when (message.role.name) {
            "USER" -> "🧑"
            "ASSISTANT" -> "🤖"
            else -> "⚙️"
        }
        return "## $roleEmoji ${message.role.name}\n\n${message.content}\n"
    }

    fun shareMessage(message: ChatMessage) {
        // 调用系统分享
    }
}
